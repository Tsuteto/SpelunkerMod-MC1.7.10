package tsuteto.spelunker;

import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import tsuteto.spelunker.constants.SpelunkerGameMode;

/**
 * Loads and stores Spelunker Mod settings
 *
 * @author Tsuteto
 *
 */
public class Settings
{
    public static boolean debug = Boolean.parseBoolean(System.getProperty("spelunkerMod.debug", "false"));

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

    public int dimTypeId = 1301;

    public String[] cursedWords = new String[]{
            "進捗どうですか", "sintyokudoudesuka", "sinntyokudoudesuka", "つらい", "turai",
            "How's the progress", "How is the progress", "How about the progress", "How 'bout the progress", "hard"};
    public int goldenSpelunkers = 0;
    public boolean goldenSpelunkerRecipe;

    public boolean updateCheck = true;

    public void load(Configuration cfg, Side side)
    {

        // Game
        fullHP = cfg.get("game", "fullHP", fullHP).getBoolean(fullHP);
        forceDeath = cfg.get("game", "forceDeath", forceDeath,
                "If true, the Spelunker is forced to die when damaged. This option is for the mods which modify the health system not to kill him by a single attack, such as TFC.").getBoolean(forceDeath);
        moodyDeathCause = cfg.get("game", "moodyDeathCause", moodyDeathCause).getBoolean(moodyDeathCause);

        cursedWords = cfg.get("game", "cursedWords", cursedWords,
                "For hardcore. Partial matching, non-case-sensitive, ignoring space, quotations and commas. Blank to disable").getStringList();

        Property propGoldenSpelunkers = cfg.get("game", "goldenSpelunkers", goldenSpelunkers,
                "For hardcore. How many Golden Spelunker (Fake) do players have?");
        goldenSpelunkers = propGoldenSpelunkers.getInt();

        goldenSpelunkerRecipe = cfg.get("game", "enableGoldenSpelunkerRecipe", side.isClient(),
                "If true, Golden Spelunker (Fake)'s recipe is enabled").getBoolean(side.isClient());

        // Game Start
        // Game Mode for multiplayer
        if (side.isServer())
        {
            Property propInitialMode = cfg.get("game_start", "initialMode", gameMode.ordinal(),
                    "Game mode in Multiplayer. 0=Spelunker Mode, 1=Minna De Spelunker (Life-Sharing) Mode.");
            gameMode = SpelunkerGameMode.values()[propInitialMode.getInt()];

            Property propHardcore = cfg.get("game_start", "hardcore", false,
                    "Hardcore Spelunker in Multiplayer. Are you all really going to challenge such insane hard rules?!");
            hardcore = propHardcore.getBoolean(false);
        }

        initialLives = cfg.get("game_start", "initialLives", initialLives).getInt();

        // Display
        displayDeaths = cfg.get("display", "deaths", displayDeaths).getBoolean(displayDeaths);
        displayScore = cfg.get("display", "score", displayScore).getBoolean(displayScore);
        clientHelmetLightInterval = cfg.get("display", "helmetLightInterval", clientHelmetLightInterval).getInt();

        // Server
        playerStatusInPublic = cfg.get("server", "playerStatusInPublic", playerStatusInPublic).getBoolean(playerStatusInPublic);
        serverHelmetLightInterval = cfg.get("server", "helmetLightInterval", serverHelmetLightInterval).getInt();

        // Entity
        Property propBatDroppingsId = cfg.get("entity", "batDroppingsId", entityBatDroppingsId);
        propBatDroppingsId.comment = "Followings are entity IDs to be allocated. -1 for auto allocation.";
        entityBatDroppingsId = propBatDroppingsId.getInt();
        entityFlashBulletId  = cfg.get("entity", "flashBulletId", entityFlashBulletId).getInt();
        entityFlashId = cfg.get("entity", "flashId", entityFlashId).getInt();
        entityGunBulletId  = cfg.get("entity", "gunBulletId", entityGunBulletId).getInt();
        entitySpelunkerItemId = cfg.get("entity", "spelunkerItemId", entitySpelunkerItemId).getInt();
        entityElevatorId = cfg.get("entity", "elevatorId", entityElevatorId).getInt();
        entityLiftId = cfg.get("entity", "liftId", entityLiftId).getInt();
        entitySteamId = cfg.get("entity", "steamId", entitySteamId).getInt();
        entityFlameId = cfg.get("entity", "flameId", entityFlameId).getInt();
        entityStillBatId = cfg.get("entity", "stillBatId", entityStillBatId).getInt();
        entityDynamiteId = cfg.get("entity", "dynamiteId", entityDynamiteId).getInt();
        entityGhostId = cfg.get("entity", "ghostId", entityGhostId).getInt();

        // Level Building
        dimTypeId = cfg.get("levelbuilding", "dimensionTypeId", dimTypeId).getInt();
    }
}
