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
import tsuteto.spelunker.block.SpelunkerBlocks;
import tsuteto.spelunker.blockaspect.BlockAspectHC;
import tsuteto.spelunker.command.CommandSpedeath;
import tsuteto.spelunker.command.CommandSpehisc;
import tsuteto.spelunker.command.CommandSperank;
import tsuteto.spelunker.constants.SpelunkerGameMode;
import tsuteto.spelunker.data.SpelunkerMultiWorldInfo;
import tsuteto.spelunker.data.SpelunkerSaveHandler;
import tsuteto.spelunker.data.SpelunkerSaveHandlerMulti;
import tsuteto.spelunker.dimension.SpelunkerLevelManager;
import tsuteto.spelunker.entity.SpelunkerEntity;
import tsuteto.spelunker.eventhandler.*;
import tsuteto.spelunker.gui.ScreenRendererGameOverlay;
import tsuteto.spelunker.gui.SpelunkerGuiHandler;
import tsuteto.spelunker.item.SpelunkerItem;
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
import tsuteto.spelunker.world.SpelunkerBiomes;
import tsuteto.spelunker.world.WorldProviderSpelunker;

import java.io.File;
import java.util.Map;
import java.util.UUID;

/**
 * This is the Main Class of the Spelunker Mod!
 *
 * @author Tsuteto
 *
 */
@Mod(modid = SpelunkerMod.modId, useMetadata = true, version = "2.3.4.5-MC1.7.10", acceptedMinecraftVersions = "[1.7.2,1.8)")
public class SpelunkerMod
{
    public static final String modId = "SpelunkerMod";
    public static final String resourceDomain = "spelunker:";

    public static final String levelMapFileDir = "Spelunker Maps";

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

    public static boolean isBgmMainAvailable = false;
    public static boolean isBgm2xScoreAvailable = false;
    public static boolean isBgmInvincibleAvailable = false;
    public static boolean isBgmSpeedPotionAvailable = false;
    public static boolean isBgmGhostComingAvailable = false;

    public static Map<UUID, EntityPlayerMP> deadPlayerStorage = Maps.newHashMap();

    public static ChestGenHooks hardcoreBonusChest;
    public static CreativeTabs tabLevelComponents = new CreativeTabs("spelunker.levelComponents")
    {
        @Override
        public Item getTabIconItem() { return SpelunkerItem.itemHelmet; }
    };

    private Configuration cfg;
    private Settings settings = new Settings();
    public UpdateNotification update = null;

    private ScreenRendererGameOverlay renderScreen = new ScreenRendererGameOverlay();
    private SpelunkerSaveHandler saveHandler = null;
    private SpelunkerSaveHandlerMulti saveHandlerMulti = null;
    private SpelunkerMultiWorldInfo multiWorldInfo = null;
    private SpelunkerLevelManager levelManager = null;
    private SpelunkerMapManager mapManager = null;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ModLog.mod = this.getClass().getSimpleName();
        ModLog.isDebug = Settings.debug;

        ModInfo.load(metadata);

        this.renameConfigFile(event.getSuggestedConfigurationFile());
        this.cfg = new Configuration(event.getSuggestedConfigurationFile());
        this.cfg.load();

        settings.load(this.cfg, event.getSide());
        UpdateNotification.initialize(this.cfg, metadata);

        this.cfg.save();

        SpelunkerItem.load();
        SpelunkerBlocks.load();

        hardcoreBonusChest = new ChestGenHooks(ChestGenHooks.BONUS_CHEST,
                new WeightedRandomChestContent[]{new WeightedRandomChestContent(SpelunkerItem.itemGoldenStatue, 0, 1, 1, 100)}, 100, 100);

        // Register entities
        SpelunkerEntity.register(this, this.settings);

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
        SpelunkerBiomes.register(cfg);

        // Register dimension type
        DimensionManager.registerProviderType(settings.dimTypeId, WorldProviderSpelunker.class, false);

        // Spelunker Level Map Manager
        mapManager = new SpelunkerMapManager(getMapDataDir());
    }

    private void renameConfigFile(File newCfg)
    {
        File oldCfg = new File(newCfg.getParent(), "SpelunkerMod2.cfg");
        if (oldCfg.exists() && oldCfg.isFile() && !newCfg.exists())
        {
            oldCfg.renameTo(newCfg);
        }
    }

    @Mod.EventHandler
    public void modsLoaded(FMLPostInitializationEvent event)
    {
        SpelunkerItem.modsLoaded();
        SpelunkerPotion.register(cfg);

        // Replace absorption effect
        new PotionBonusScore(Potion.field_76444_x.id, false, 16284963).setPotionName("potion.bonusScore");
        new PotionBonusScore(Potion.field_76434_w.id, false, 2445989).setPotionName("potion.bonusScore");

        cfg.save();
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandSpehisc());
        event.registerServerCommand(new CommandSpedeath());
        event.registerServerCommand(new CommandSperank());

        // Set up SaveHandler for players
        SaveHandler saveHandler = (SaveHandler)event.getServer().worldServerForDimension(0).getSaveHandler();

        SpelunkerSaveHandler spelunkerSaveHandler = new SpelunkerSaveHandler(saveHandler.getWorldDirectory(), event.getServer().isSinglePlayer());

        this.saveHandler = spelunkerSaveHandler;

        // Set up SaveHandler for world
        SpelunkerSaveHandlerMulti multiSaveHandler = new SpelunkerSaveHandlerMulti(saveHandler.getWorldDirectory());
        this.saveHandlerMulti = multiSaveHandler;

        multiWorldInfo = multiSaveHandler.loadData();
        if (multiWorldInfo == null)
        {
            multiWorldInfo = new SpelunkerMultiWorldInfo();
            saveHandlerMulti.saveData(multiWorldInfo);
        }

        // Set up SpelunkerLevelManager
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        {
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

    public static SpelunkerSaveHandlerMulti getSaveHandlerMulti()
    {
        return instance.saveHandlerMulti;
    }

    public static SpelunkerGameMode getGamemodeMulti()
    {
        return instance.multiWorldInfo.getMode();
    }

    public static int getTotalLivesLeft()
    {
        return instance.multiWorldInfo.totalLives;
    }

    public static void decreaseTotalLivesLeft()
    {
        instance.multiWorldInfo.totalLives -= 1;
        instance.saveHandlerMulti.saveData(instance.multiWorldInfo);
    }

    public static void increaseTotalLivesLeft(int i)
    {
        instance.multiWorldInfo.totalLives += i;
        instance.saveHandlerMulti.saveData(instance.multiWorldInfo);
    }

    public static boolean isGameToBeFinished()
    {
        return instance.multiWorldInfo.isGameToBeFinished;
    }
    public static boolean setGameToBeFinished(boolean flag)
    {
        return instance.multiWorldInfo.isGameToBeFinished = flag;
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

    /**
     * This is server-side use only!
     */
    public static boolean isHardcore()
    {
        return instance.multiWorldInfo.hardcore;
    }

    public static File getMapDataDir()
    {
        return new File(sidedProxy.getMapDataDir(), levelMapFileDir);
    }
}
