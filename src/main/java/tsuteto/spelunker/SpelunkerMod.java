package tsuteto.spelunker;

import api.player.server.ServerPlayerAPI;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import tsuteto.spelunker.blockaspect.BlockAspectHC;
import tsuteto.spelunker.command.*;
import tsuteto.spelunker.constants.SpelunkerGameMode;
import tsuteto.spelunker.data.*;
import tsuteto.spelunker.dimension.SpelunkerLevelManager;
import tsuteto.spelunker.eventhandler.*;
import tsuteto.spelunker.gui.ScreenRendererGameOverlay;
import tsuteto.spelunker.gui.SpelunkerGuiHandler;
import tsuteto.spelunker.init.*;
import tsuteto.spelunker.levelmap.SpelunkerMapManager;
import tsuteto.spelunker.network.PacketManager;
import tsuteto.spelunker.network.SpelunkerClientPacketHandler;
import tsuteto.spelunker.network.SpelunkerServerPacketHandler;
import tsuteto.spelunker.network.packet.*;
import tsuteto.spelunker.player.ISpelunkerPlayer;
import tsuteto.spelunker.player.SpelunkerPlayerMP;
import tsuteto.spelunker.potion.PotionBonusScore;
import tsuteto.spelunker.potion.SpelunkerPotion;
import tsuteto.spelunker.sidedproxy.ISidedProxy;
import tsuteto.spelunker.util.ModLog;
import tsuteto.spelunker.util.UpdateNotification;
import tsuteto.spelunker.util.Utils;
import tsuteto.spelunker.world.SpelunkerBiomes;
import tsuteto.spelunker.world.WorldProviderSpelunker;
import tsuteto.spelunker.world.gen.WorldGenEventHandler;

import java.io.File;
import java.util.Map;
import java.util.UUID;

/**
 * This is the Main Class of the Spelunker Mod!
 *
 * @author Tsuteto
 *
 */
@Mod(modid = SpelunkerMod.modId, useMetadata = true, version = "3.0.0-MC1.7.10",
        acceptedMinecraftVersions = "[1.7.2,1.8)",
        guiFactory = "tsuteto.spelunker.config.SpeConfigGuiFactory")
public class SpelunkerMod
{
    public static final String modId = "SpelunkerMod";
    public static final String resourceDomain = "spelunker:";

    public static String spelunkerDir = "spelunker-mod";
    public static final String levelMapFileDir = "maps";

    @Mod.Instance(modId)
    public static SpelunkerMod instance;

    @Mod.Metadata(modId)
    private static ModMetadata metadata;

    @SidedProxy(clientSide = "tsuteto.spelunker.sidedproxy.ClientProxy", serverSide = "tsuteto.spelunker.sidedproxy.ServerProxy")
    public static ISidedProxy sidedProxy;

    public static final int energyCostDig = 5;
    public static final int energyCostJump = 5;
    public static final int energyCostAttack = 20;
    public static final float energyCostSprint = 0.2F;

    public static final int bonusEnergyMax = 1000;
    public static final int bonusSpeLvlClearedBase = 20000;
    public static final int bonusSpeLvlClearedChkPt = 10000;
    public static final int bonusSpeCheckPoint = 10000;
    public static final int bonusAllCleared = 500000;

    public static final int restorationTime = 36000;

    public static Map<UUID, EntityPlayerMP> deadPlayerStorage = Maps.newHashMap();

    public static ChestGenHooks hardcoreBonusChest;
    public static CreativeTabs tabLevelComponents = new CreativeTabs("spelunker.levelComponents")
    {
        @Override
        public Item getTabIconItem() { return SpelunkerItems.itemHelmet; }
    };

    private Settings settings;
    public UpdateNotification update = null;

    private ScreenRendererGameOverlay renderScreen = new ScreenRendererGameOverlay();
    private SpelunkerSaveHandler saveHandler = null;
    private SpelunkerSaveHandlerWorld saveHandlerWorld = null;
    private SpelunkerGeneralInfo worldInfo = null;
    private SpelunkerLevelManager levelManager = null;
    private SpelunkerMapManager mapManager = null;
    private SpeLevelRecordManager recordManager = null;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ModLog.mod = this.getClass().getSimpleName();
        ModLog.isDebug = Settings.debug;

        ModInfo.load(metadata);

        // Create Spelunker data directory
        if (!getSpelunkerDir().exists() && !getSpelunkerDir().mkdir())
        {
            throw new RuntimeException("Failed to create Spelunker Mod's data directory at: " + getSpelunkerDir().getAbsolutePath());
        }

        this.migrate(event.getSuggestedConfigurationFile());

        Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
        this.settings = new Settings(cfg, event.getSide());
        UpdateNotification.initialize(metadata);

        SpelunkerItems.load();
        SpelunkerBlocks.load();
        SpelunkerRecipes.load();

        hardcoreBonusChest = new ChestGenHooks(ChestGenHooks.BONUS_CHEST,
                new WeightedRandomChestContent[]{new WeightedRandomChestContent(SpelunkerItems.itemGoldenStatue, 0, 1, 1, 100)}, 100, 100);

        // Register entities
        SpelunkerEntity.register(this, this.settings);

        // Achievements
        SpeAchievementList.register();

        // Update check!
        UpdateNotification.instance().checkUpdate();
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event)
    {
        // Register event handlers
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
        MinecraftForge.EVENT_BUS.register(new LivingEventHandler());
        MinecraftForge.EVENT_BUS.register(new ServerChatHandler());
        MinecraftForge.EVENT_BUS.register(new EventPlayerInteract());
        MinecraftForge.EVENT_BUS.register(new ItemEventHandler());
        MinecraftForge.EVENT_BUS.register(new BlockEventHandler());
        MinecraftForge.EVENT_BUS.register(new WorldGenEventHandler());
        MinecraftForge.EVENT_BUS.register(new WorldEventHandler());
        FMLCommonHandler.instance().bus().register(this.settings);
        FMLCommonHandler.instance().bus().register(new PlayerEventHandler());
        FMLCommonHandler.instance().bus().register(new ServerTickHandler());
        FMLCommonHandler.instance().bus().register(new ConnectionEventHandler());
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new SpelunkerGuiHandler());

        BlockAspectHC.init();

        // Register sided components
        sidedProxy.registerComponent(this);

        // Initialize network handler
        PacketManager.init(modId)
                .registerPacket(SpelunkerClientPacketHandler.class)
                .registerPacket(SpelunkerServerPacketHandler.class)
                .registerPacket(PacketElevatorControlCl.class)
                .registerPacket(PacketElevatorControlSv.class)
                .registerPacket(PacketElevatorState.class)
                .registerPacket(PacketDimRegistration.class)
                .registerPacket(PacketContainerData.class)
                .registerPacket(PacketGuiControl.class)
                .registerEventHandler(new ConnectionEventHandler());

        // Check if BGM sound files exist
        sidedProxy.checkBgmSoundFile();

        // Register the Spelunker class for PlayerAPI
        ServerPlayerAPI.register(modId, SpelunkerPlayerMP.class);

        // Biome
        SpelunkerBiomes.register();

        // Register dimension type
        DimensionManager.registerProviderType(settings.dimTypeId, WorldProviderSpelunker.class, false);

        // Spelunker Level Map Manager
        mapManager = new SpelunkerMapManager(new File(getSpelunkerDir(), levelMapFileDir));

        // Spelunker Record
        SpelunkerSaveHandlerRecords saveHandlerRecords = new SpelunkerSaveHandlerRecords(getSpelunkerDir());
        this.recordManager = saveHandlerRecords.loadData();
    }

    private void migrate(File newCfg)
    {
        // Config file
        File oldCfg = new File(newCfg.getParent(), "SpelunkerMod2.cfg");
        Utils.renameFileSafely(oldCfg, newCfg);

        // Spelunker Maps
        File oldMapDir = sidedProxy.getDataDir("Spelunker Maps");
        File newMapDir = new File(getSpelunkerDir(), levelMapFileDir);
        Utils.renameDirectorySafely(oldMapDir, newMapDir);
    }

    @Mod.EventHandler
    public void modsLoaded(FMLPostInitializationEvent event)
    {
        SpelunkerItems.modsLoaded();
        SpelunkerPotion.register();

        // Replace absorption effect
        new PotionBonusScore(Potion.field_76444_x.id, false, 16284963).setPotionName("potion.bonusScore");
        new PotionBonusScore(Potion.field_76434_w.id, false, 2445989).setPotionName("potion.bonusScore");

        settings.saveCfg();
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandSpehisc());
        event.registerServerCommand(new CommandSpedeath());
        event.registerServerCommand(new CommandSperank());
        event.registerServerCommand(new CommandResetGhost());
        event.registerServerCommand(new CommandSpeHardcore());
        event.registerServerCommand(new CommandSpeLevelRecord());

        // Set up SaveHandler for players
        SaveHandler saveHandler = (SaveHandler)event.getServer().worldServerForDimension(0).getSaveHandler();

        SpelunkerSaveHandler spelunkerSaveHandler = new SpelunkerSaveHandler(saveHandler.getWorldDirectory(), event.getServer().isSinglePlayer());
        this.saveHandler = spelunkerSaveHandler;

        // Set up SaveHandler for world
        SpelunkerSaveHandlerWorld worldSaveHandler = new SpelunkerSaveHandlerWorld(saveHandler.getWorldDirectory());
        this.saveHandlerWorld = worldSaveHandler;
        this.worldInfo = worldSaveHandler.loadData();

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        {
            // Set up SpelunkerLevelManager
            levelManager = new SpelunkerLevelManager(settings.dimTypeId, saveHandler.getWorldDirectory());
            levelManager.loadLevelData();
            levelManager.registerAll();
        }

        // Notify if update is available
        UpdateNotification.instance().onServerStarting(event);

    }

    @Mod.EventHandler
    public void onServerStopped(FMLServerStoppedEvent event)
    {
        ModLog.debug("Processing for the closing server");

        levelManager.unregisterAll();
        levelManager = null;

        GhostSpawnHandler.onWorldClosed();
        deadPlayerStorage.clear();

        saveHandler = null;
        saveHandlerWorld = null;
        worldInfo = null;
    }

    /**
     * Returns Spelunker instance by PlayerAPI
     */
    public static <T extends ISpelunkerPlayer> T getSpelunkerPlayer(EntityPlayer player)
    {
        return SpelunkerMod.sidedProxy.getSpelunkerPlayer(player);
    }

    public static Settings settings()
    {
        return instance.settings;
    }

    public static SpelunkerSaveHandler getSaveHandler()
    {
        return instance.saveHandler;
    }

    public static void setSaveHandler(SpelunkerSaveHandler saveHandler)
    {
        instance.saveHandler = saveHandler;
    }

    public static SpelunkerGeneralInfo getWorldInfo()
    {
        return instance.worldInfo;
    }

    public static void saveWorldInfo()
    {
        instance.saveHandlerWorld.saveData(instance.worldInfo);
    }

    public static boolean isWorldInitialized()
    {
        return instance.worldInfo.isWorldInitialized;
    }

    public static void setWorldInitialized()
    {
        instance.worldInfo.isWorldInitialized = true;
    }

    public static SpelunkerGameMode getGamemodeMulti()
    {
        return instance.worldInfo.getMode();
    }

    public static int getTotalLivesLeft()
    {
        return instance.worldInfo.totalLives;
    }

    public static void decreaseTotalLivesLeft()
    {
        instance.worldInfo.totalLives -= 1;
        saveWorldInfo();
    }

    public static void increaseTotalLivesLeft(int i)
    {
        instance.worldInfo.totalLives += i;
        saveWorldInfo();
    }

    public static boolean isGameToBeFinished()
    {
        return instance.worldInfo.isGameToBeFinished;
    }
    public static boolean setGameToBeFinished(boolean flag)
    {
        return instance.worldInfo.isGameToBeFinished = flag;
    }

    public ScreenRendererGameOverlay gameScreenRenderer()
    {
        return renderScreen;
    }

    public static SpelunkerLevelManager levelManager()
    {
        return instance.levelManager;
    }

    public static SpelunkerMapManager mapManager()
    {
        return instance.mapManager;
    }

    public static SpeLevelRecordManager recordManager()
    {
        return instance.recordManager;
    }

    public static boolean isSinglePlayer()
    {
        return sidedProxy.isSinglePlayer();
    }

    /**
     * This is server-side use only!
     */
    public static boolean isHardcore()
    {
        return instance.worldInfo.hardcore;
    }

    public static File getSpelunkerDir()
    {
        return sidedProxy.getDataDir(SpelunkerMod.spelunkerDir);
    }
}
