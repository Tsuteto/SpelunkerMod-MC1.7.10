package tsuteto.spelunker;

import api.player.server.ServerPlayerAPI;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import tsuteto.spelunker.blockaspect.BlockAspectHC;
import tsuteto.spelunker.command.CommandSpedeath;
import tsuteto.spelunker.command.CommandSpehisc;
import tsuteto.spelunker.command.CommandSperank;
import tsuteto.spelunker.constants.SpelunkerGameMode;
import tsuteto.spelunker.data.SpelunkerMultiWorldInfo;
import tsuteto.spelunker.data.SpelunkerSaveHandler;
import tsuteto.spelunker.data.SpelunkerSaveHandlerMulti;
import tsuteto.spelunker.entity.*;
import tsuteto.spelunker.eventhandler.*;
import tsuteto.spelunker.gui.ScreenRendererGameover;
import tsuteto.spelunker.item.SpelunkerItem;
import tsuteto.spelunker.packet.PacketPipeline;
import tsuteto.spelunker.packet.SpelunkerPacketHandler;
import tsuteto.spelunker.player.ISpelunkerPlayer;
import tsuteto.spelunker.player.SpelunkerPlayerMP;
import tsuteto.spelunker.potion.PotionBonusScore;
import tsuteto.spelunker.potion.SpelunkerPotion;
import tsuteto.spelunker.sidedproxy.ISidedProxy;
import tsuteto.spelunker.util.ModLog;
import tsuteto.spelunker.util.UpdateNotification;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the Main Class of the Spelunker Mod!
 *
 * @author Tsuteto
 *
 */
@Mod(modid = SpelunkerMod.modId, useMetadata = true, version = "2.2.4-MC1.7.10", acceptedMinecraftVersions = "[1.7.2,1.8)")
public class SpelunkerMod
{
    public static final String modId = "SpelunkerMod2";
    public static final String resourceDomain = "spelunker:";

    @Mod.Instance(modId)
    private static SpelunkerMod instance;

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

    public static Map<String, EntityPlayerMP> deadPlayerStorage = new HashMap<String, EntityPlayerMP>();

    public static ChestGenHooks hardcoreBonusChest;
    public static PacketPipeline packetPipeline = new PacketPipeline();

    private Configuration cfg;
    private Settings settings = new Settings();
    public UpdateNotification update = null;

    private ScreenRendererGameover renderScreen = new ScreenRendererGameover();
    private SpelunkerSaveHandler saveHandler = null;
    private SpelunkerSaveHandlerMulti saveHandlerMulti = null;
    private SpelunkerMultiWorldInfo multiWorldInfo = null;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ModLog.mod = this.getClass().getSimpleName();
        ModLog.isDebug = Settings.debug;

        this.cfg = new Configuration(event.getSuggestedConfigurationFile());
        this.cfg.load();

        settings.load(this.cfg, event.getSide());
        UpdateNotification.initialize(this.cfg, metadata);

        this.cfg.save();

        // Install sound files
        sidedProxy.installSoundFiles();

        // Load items
        SpelunkerItem.load();

        hardcoreBonusChest = new ChestGenHooks(ChestGenHooks.BONUS_CHEST,
                new WeightedRandomChestContent[]{new WeightedRandomChestContent(SpelunkerItem.itemGoldenStatue, 0, 1, 1, 100)}, 100, 100);

        //sidedProxy.registerWavFiles();

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

        BlockAspectHC.init();

        // Register entity
        EntityRegistry.registerGlobalEntityID(EntityGunBullet.class, "gunBullet",
                settings.entityGunBulletId == -1 ? EntityRegistry.findGlobalUniqueEntityId() : settings.entityGunBulletId);
        EntityRegistry.registerModEntity(EntityGunBullet.class, "gunBullet", 1, this, 64, 20, false);

        EntityRegistry.registerGlobalEntityID(EntityBatDroppings.class, "batDroppings",
                settings.entityBatDroppingsId == -1 ? EntityRegistry.findGlobalUniqueEntityId() : settings.entityBatDroppingsId);
        EntityRegistry.registerModEntity(EntityBatDroppings.class, "batDroppings", 2, this, 64, 10, true);

        EntityRegistry.registerGlobalEntityID(EntityFlashBullet.class, "flashBullet",
                settings.entityFlashBulletId == -1 ? EntityRegistry.findGlobalUniqueEntityId() : settings.entityFlashBulletId);
        EntityRegistry.registerModEntity(EntityFlashBullet.class, "flashBullet", 3, this, 64, 5, true);

        EntityRegistry.registerGlobalEntityID(EntityFlash.class, "flash",
                settings.entityFlashId == -1 ? EntityRegistry.findGlobalUniqueEntityId() : settings.entityFlashId);
        EntityRegistry.registerModEntity(EntityFlash.class, "flash", 4, this, 64, 5, true);

        EntityRegistry.registerGlobalEntityID(EntitySpelunkerItem.class, "speItem",
                settings.spelunkerItemId == -1 ? EntityRegistry.findGlobalUniqueEntityId() : settings.spelunkerItemId);
        EntityRegistry.registerModEntity(EntitySpelunkerItem.class, "speItem", 5, this, 64, 5, true);

        // Register sided components
        sidedProxy.registerComponent(this);

        // Initialize network handler
        packetPipeline.initalise();
        packetPipeline.registerPacket(SpelunkerPacketHandler.class);

        // Check BGM sound file
        sidedProxy.checkBgmSoundFile();

        // Register the Spelunker class for PlayerAPI
        ServerPlayerAPI.register(modId, SpelunkerPlayerMP.class);
    }

    @Mod.EventHandler
    public void modsLoaded(FMLPostInitializationEvent event)
    {
        SpelunkerItem.modsLoaded();
        SpelunkerPotion.register(cfg);

        // Replace absorption effect
        new PotionBonusScore(Potion.field_76444_x.id, false, 16284963).setPotionName("potion.bonusScore");
        new PotionBonusScore(Potion.field_76434_w.id, false, 2445989).setPotionName("potion.bonusScore");

        packetPipeline.postInitialise();
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandSpehisc());
        event.registerServerCommand(new CommandSpedeath());
        event.registerServerCommand(new CommandSperank());

        // Set up SaveHandler for players
        SaveHandler saveHandler = (SaveHandler)event.getServer().worldServers[0].getSaveHandler();

        SpelunkerSaveHandler spelunkerSaveHandler = new SpelunkerSaveHandler(
                saveHandler.getWorldDirectory(),
                MinecraftServer.getServer().isSinglePlayer());

        this.saveHandler = spelunkerSaveHandler;
        ModLog.debug("loaded savehandler for single: " + saveHandler.getWorldDirectory());

        // Set up SaveHandler for world
        SpelunkerSaveHandlerMulti multiSaveHandler = new SpelunkerSaveHandlerMulti(saveHandler.getWorldDirectory());
        this.saveHandlerMulti = multiSaveHandler;

        multiWorldInfo = multiSaveHandler.loadData();
        if (multiWorldInfo == null)
        {
            multiWorldInfo = new SpelunkerMultiWorldInfo();
            saveHandlerMulti.saveData(multiWorldInfo);
        }

        if (isHardcore())
        {
//            EntityRegistry.addSpawn(EntityBat.class, 10, 32, 32, EnumCreatureType.ambient, (BiomeGenBase[])BiomeGenBase.explorationBiomesList.toArray(new BiomeGenBase[0]));
        }

        // Notify if update is available
        UpdateNotification.instance().onServerStarting(event);

        ModLog.debug("loaded savehandler for multi: " + saveHandler.getWorldDirectory());

    }

    /**
     * Returns Spelunker instance by PlayerAPI
     *
     * @param player
     * @return
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

    public ScreenRendererGameover getScreenRenderer()
    {
        return renderScreen;
    }

    public static boolean isGameToBeFinished()
    {
        return instance.multiWorldInfo.isGameToBeFinished;
    }
    public static boolean setGameToBeFinished(boolean flag)
    {
        return instance.multiWorldInfo.isGameToBeFinished = flag;
    }

    /**
     * This is server-side only!
     * @return
     */
    public static boolean isHardcore()
    {
        return instance.multiWorldInfo.hardcore;
    }
}
