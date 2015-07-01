package tsuteto.spelunker;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.potion.Potion;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.Level;
import tsuteto.spelunker.constants.SpelunkerGameMode;
import tsuteto.spelunker.potion.SpelunkerPotion;
import tsuteto.spelunker.util.ModLog;
import tsuteto.spelunker.world.SpelunkerBiomes;

/**
 * Loads and stores Spelunker Mod settings
 *
 * @author Tsuteto
 *
 */
public class Settings
{
    public static final String CAT_GAME = "game";
    public static final String CAT_GAME_START = "game_start";
    public static final String CAT_DISPLAY = "display";
    public static final String CAT_SERVER = "server";
    public static final String CAT_LEVEL_BUILDING = "levelbuilding";
    public static final String CAT_ENTITY = "entity";
    public static final String CAT_BIOME = "biome";
    public static final String CAT_POTION = "potion";
    public static final String CAT_MISC = "misc";

    public static final String[] CATEGORIES = new String[]{
            CAT_GAME, CAT_GAME_START, CAT_DISPLAY, CAT_SERVER, CAT_LEVEL_BUILDING, CAT_ENTITY, CAT_BIOME, CAT_POTION, CAT_MISC};

    public static boolean debug = Boolean.parseBoolean(System.getProperty("spelunkerMod.debug", "false"));

    private Configuration cfg;
    private Side side;

    public boolean hardcore = false;
    public boolean fullHP = false;
    public boolean forceDeath = false;
    public boolean moodyDeathCause = true;

    public SpelunkerGameMode gameMode = SpelunkerGameMode.Adventure;
    public int initialLives = 5;

    public boolean displayDeaths = true;
    public boolean displayScore = true;

    public boolean playerStatusInPublic = true;

    public int clientHelmetLightInterval = 1;
    public int serverHelmetLightInterval = 5;

    public int entityGunBulletId = -1;
    public int entityBatDroppingsId = -1;
    public int entityFlashId = -1;
    public int entityFlashBulletId = -1;
    public int entitySpelunkerItemId = -1;
    public int entityElevatorId = -1;
    public int entityLiftId = -1;
    public int entitySteamId = -1;
    public int entityFlameId = -1;
    public int entityStillBatId = -1;
    public int entityDynamiteId = -1;
    public int entityGhostId = -1;
    public int entityWaterLiftId = -1;
    public int entitySpelunkartId = -1;
    public int entitySpeBoatId = -1;

    public int dimTypeId = 1301;

    public int biomeSpelunkerCaveId;

    public int potionChokedId;
    public int potionSunstrokeId;

    public String[] cursedWords = new String[]{
            "進捗どうですか", "sintyokudoudesuka", "sinntyokudoudesuka", "つらい", "turai",
            "How's the progress", "How is the progress", "How about the progress", "How 'bout the progress", "hard"};
    public int goldenSpelunkers = 0;
    public boolean goldenSpelunkerRecipe;

    public boolean updateCheck = true;
    public boolean restoreSample = false;

    private Property propRestoreSample;

    public Settings(Configuration cfg, Side side)
    {
        this.cfg = cfg;
        this.side = side;
        cfg.load();
        this.main();
        this.biome();
        this.potion();
    }

    public void main()
    {
        // Game
        cfg.getCategory(CAT_GAME)
                .setLanguageKey("Spelunker.options.category.game")
                .setComment("Options effecting to the game");

        fullHP = cfg.get(CAT_GAME, "fullHP", fullHP,
                "If true, the Spelunker's maximum HP becomes 10 hearts. For people who can't bare this severe Spelunker difficulty")
                .setLanguageKey("Spelunker.options.fullHP")
                .getBoolean(fullHP);
        forceDeath = cfg.get(CAT_GAME, "forceDeath", forceDeath,
                "If true, the Spelunker is forced to die when damaged. This option is for the mods which modify the health system not to kill him by a single attack, such as TFC and DQM.")
                .setLanguageKey("Spelunker.options.forceDeath")
                .getBoolean(forceDeath);
        moodyDeathCause = cfg.get(CAT_GAME, "moodyDeathCause", moodyDeathCause,
                "If true, death cause of the Spelunker becomes Spelunker mood. Or else it remains Minecraft.")
                .setLanguageKey("Spelunker.options.moodyDeathCause")
                .getBoolean(moodyDeathCause);

        cursedWords = cfg.get(CAT_GAME, "cursedWords", cursedWords,
                "For hardcore. Partial matching, non-case-sensitive, ignoring space, quotations and commas. Blank to disable")
                .setLanguageKey("Spelunker.options.cursedWords")
                .getStringList();

        Property propGoldenSpelunkers = cfg.get(CAT_GAME, "goldenSpelunkers", goldenSpelunkers,
                "For hardcore. How many Golden Spelunker (Fake) do players have?")
                .setLanguageKey("Spelunker.options.goldenSpelunkers")
                .setMinValue(0).setMaxValue(36);
        goldenSpelunkers = propGoldenSpelunkers.getInt();

        goldenSpelunkerRecipe = cfg.get(CAT_GAME, "enableGoldenSpelunkerRecipe", side.isClient(),
                "If true, Golden Spelunker (Fake)'s recipe is enabled")
                .setLanguageKey("Spelunker.options.goldenSpelunkerRecipe")
                .getBoolean(side.isClient());

        // Game Start
        cfg.getCategory(CAT_GAME_START)
                .setLanguageKey("Spelunker.options.category.game_start")
                .setComment("Options when the game starts");
        // Game Mode for multiplayer
        if (side.isServer())
        {
            Property propInitialMode = cfg.get(CAT_GAME_START, "initialMode", gameMode.ordinal(),
                    "Game mode in Multiplayer. 0=Spelunker Mode, 1=Minna De Spelunker (Life-Sharing) Mode.");
            gameMode = SpelunkerGameMode.values()[propInitialMode.getInt()];

            Property propHardcore = cfg.get(CAT_GAME_START, "hardcore", false,
                    "Hardcore Spelunker in Multiplayer. Are you all really going to challenge such insane hard rules?!");
            hardcore = propHardcore.getBoolean(false);
        }

        initialLives = cfg.get(CAT_GAME_START, "initialLives", initialLives,
                "For Score Trial Mode. How many lives will the Spelunker have when the game starts")
                .setLanguageKey("Spelunker.options.initialLives")
                .setMinValue(1)
                .getInt();

        // Display
        cfg.getCategory(CAT_DISPLAY)
                .setLanguageKey("Spelunker.options.category.display")
                .setComment("Related to game screen rendering");
        displayDeaths = cfg.get(CAT_DISPLAY, "deaths", displayDeaths,
                "Whether the death counter shows on the game screen")
                .setLanguageKey("Spelunker.options.deaths")
                .getBoolean(displayDeaths);
        displayScore = cfg.get(CAT_DISPLAY, "score", displayScore,
                "Whether the score shows on the game screen")
                .setLanguageKey("Spelunker.options.score")
                .getBoolean(displayScore);
        clientHelmetLightInterval = cfg.get(CAT_DISPLAY, "helmetLightInterval", clientHelmetLightInterval,
                "Ticks of update interval for the Spelunker helmet's light on client side")
                .setLanguageKey("Spelunker.options.helmetLightIntervalCl")
                .getInt();

        // Server
        cfg.getCategory(CAT_SERVER)
                .setLanguageKey("Spelunker.options.category.server")
                .setComment("For server side, but also effective on single player");
        playerStatusInPublic = cfg.get(CAT_SERVER, "playerStatusInPublic", playerStatusInPublic,
                "Whether the commands of Spelunker mod such as /sperank and /spehisc shows all player's status to everyone.")
                .setLanguageKey("Spelunker.options.playerStatusInPublic")
                .getBoolean(playerStatusInPublic);
        serverHelmetLightInterval = cfg.get(CAT_SERVER, "helmetLightIntervalSv", serverHelmetLightInterval,
                "Ticks of update interval for the Spelunker helmet's light on server side. This is effective on single player")
                .setLanguageKey("Spelunker.options.helmetLightInterval")
                .getInt();

        // Entity
        cfg.getCategory(CAT_ENTITY)
                .setLanguageKey("Spelunker.options.category.entity")
                .setComment("Entity IDs. -1 for auto allocation");
        entityBatDroppingsId = cfg.get(CAT_ENTITY, "batDroppingsId", entityBatDroppingsId)
                .setRequiresMcRestart(true).getInt();
        entityFlashBulletId  = cfg.get(CAT_ENTITY, "flashBulletId", entityFlashBulletId)
                .setRequiresMcRestart(true).getInt();
        entityFlashId = cfg.get(CAT_ENTITY, "flashId", entityFlashId)
                .setRequiresMcRestart(true).getInt();
        entityGunBulletId  = cfg.get(CAT_ENTITY, "gunBulletId", entityGunBulletId)
                .setRequiresMcRestart(true).getInt();
        entitySpelunkerItemId = cfg.get(CAT_ENTITY, "spelunkerItemId", entitySpelunkerItemId)
                .setRequiresMcRestart(true).getInt();
        entityElevatorId = cfg.get(CAT_ENTITY, "elevatorId", entityElevatorId)
                .setRequiresMcRestart(true).getInt();
        entityLiftId = cfg.get(CAT_ENTITY, "liftId", entityLiftId)
                .setRequiresMcRestart(true).getInt();
        entitySteamId = cfg.get(CAT_ENTITY, "steamId", entitySteamId)
                .setRequiresMcRestart(true).getInt();
        entityFlameId = cfg.get(CAT_ENTITY, "flameId", entityFlameId)
                .setRequiresMcRestart(true).getInt();
        entityStillBatId = cfg.get(CAT_ENTITY, "stillBatId", entityStillBatId)
                .setRequiresMcRestart(true).getInt();
        entityDynamiteId = cfg.get(CAT_ENTITY, "dynamiteId", entityDynamiteId)
                .setRequiresMcRestart(true).getInt();
        entityGhostId = cfg.get(CAT_ENTITY, "ghostId", entityGhostId)
                .setRequiresMcRestart(true).getInt();
        entityWaterLiftId = cfg.get(CAT_ENTITY, "waterLiftId", entityWaterLiftId)
                .setRequiresMcRestart(true).getInt();
        entitySpelunkartId = cfg.get(CAT_ENTITY, "spelunkartId", entitySpelunkartId)
                .setRequiresMcRestart(true).getInt();
        entitySpeBoatId = cfg.get(CAT_ENTITY, "speBoatId", entitySpeBoatId)
                .setRequiresMcRestart(true).getInt();

        // Level Building
        cfg.getCategory(CAT_LEVEL_BUILDING)
                .setLanguageKey("Spelunker.options.category.levelbuilding")
                .setComment("Related to Spelunker World generating");
        dimTypeId = cfg.get(CAT_LEVEL_BUILDING, "dimensionTypeId", dimTypeId,
                "Dimension Type ID for Spelunker World")
                .setLanguageKey("Spelunker.options.dimensionTypeId")
                .getInt();

        // Misc
        cfg.getCategory(CAT_MISC)
                .setLanguageKey("Spelunker.options.category.misc");
        updateCheck = cfg.get(CAT_MISC, "updateCheck", updateCheck,
                "Enable update check of this mod")
                .setLanguageKey("Spelunker.options.updateCheck")
                .setRequiresMcRestart(true)
                .getBoolean(updateCheck);

        this.propRestoreSample = cfg.get(CAT_MISC, "restoreSampleMap", restoreSample,
                "If true, sample map files for Spelunker World will be restored once at the next start-up and the flag will be back to false.")
                .setLanguageKey("Spelunker.options.restoreSampleMap")
                .setRequiresMcRestart(true);
        restoreSample = propRestoreSample.getBoolean(restoreSample);
    }

    public void biome()
    {
        int maxId = BiomeGenBase.getBiomeGenArray().length - 1;

        cfg.getCategory(CAT_BIOME)
                .setLanguageKey("Spelunker.options.category.biome")
                .setComment("Biome IDs. They must be " + maxId + " or less");

        try
        {
            Property prop = cfg.get(CAT_BIOME, "spelunkerCave", -1)
                    .setMinValue(-1).setMaxValue(maxId).setRequiresMcRestart(true);
            biomeSpelunkerCaveId = SpelunkerBiomes.assignId(prop);
        }
        catch (Exception e)
        {
            ModLog.log(Level.WARN, e, e.getLocalizedMessage());
        }
    }

    public void potion()
    {
        int maxId = Potion.potionTypes.length - 1;

        cfg.getCategory(CAT_POTION)
                .setLanguageKey("Spelunker.options.category.potion")
                .setComment("Potion IDs. They must be " + maxId + " or less");

        try
        {
            potionChokedId = cfg.get(CAT_POTION, "choked", SpelunkerPotion.assignId())
                    .setMinValue(-1)
                    .setMaxValue(maxId)
                    .setRequiresMcRestart(true).getInt();
            potionSunstrokeId = cfg.get(CAT_POTION, "sunstroke", SpelunkerPotion.assignId())
                    .setMinValue(-1)
                    .setMaxValue(maxId)
                    .setRequiresMcRestart(true).getInt();
        }
        catch (Exception e)
        {
            ModLog.log(Level.WARN, e, e.getLocalizedMessage());
        }

    }

    public void syncConfig()
    {
        main();

        if (cfg.hasChanged())
        {
            cfg.save();
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs)
    {
        if(eventArgs.modID.equals(SpelunkerMod.modId))
        {
            this.syncConfig();
        }
    }

    public void onSampleMapsInstalled()
    {
        this.propRestoreSample.set(false);
    }

    public Configuration getCfg()
    {
        return this.cfg;
    }

    public void saveCfg()
    {
        this.cfg.save();
    }
}
