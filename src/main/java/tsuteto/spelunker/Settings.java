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
    public int spelunkerItemId = -1;

    public String[] cursedWords = new String[]{"進捗どうですか", "sintyokudoudesuka", "sinntyokudoudesuka", "つらい", "turai"};
    public int goldenSpelunkers = 0;
    public boolean goldenSpelunkerRecipe;


    public void load(Configuration cfg, Side side)
    {

        fullHP = cfg.get("game", "fullHP", fullHP).getBoolean(fullHP);
        forceDeath = cfg.get("game", "forceDeath", forceDeath,
                "If true, the Spelunker is forced to die when damaged. This option is for the mods which modify the health system not to kill him by a single attack, such as TFC.").getBoolean(forceDeath);
        moodyDeathCause = cfg.get("game", "moodyDeathCause", moodyDeathCause).getBoolean(moodyDeathCause);

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

        displayDeaths = cfg.get("display", "deaths", displayDeaths).getBoolean(displayDeaths);
        displayScore = cfg.get("display", "score", displayScore).getBoolean(displayScore);

        playerStatusInPublic = cfg.get("server", "playerStatusInPublic", playerStatusInPublic).getBoolean(playerStatusInPublic);

        clientHelmetLightInterval = cfg.get("display", "helmetLightInterval", clientHelmetLightInterval).getInt();
        serverHelmetLightInterval = cfg.get("server", "helmetLightInterval", serverHelmetLightInterval).getInt();

        Property propBatDroppingsId = cfg.get("entity", "batDroppingsId", entityBatDroppingsId);
        propBatDroppingsId.comment = "Followings are entity IDs to be allocated. -1 for auto allocation.";
        entityBatDroppingsId = propBatDroppingsId.getInt();
        entityFlashBulletId  = cfg.get("entity", "flashBulletId", entityFlashBulletId).getInt();
        entityFlashId = cfg.get("entity", "flashId", entityFlashId).getInt();
        entityGunBulletId  = cfg.get("entity", "gunBulletId", entityGunBulletId).getInt();

        cursedWords = cfg.get("game", "cursedWords", cursedWords,
                "For hardcore. Partial matching, non-case-sensitive, ignoring space characters. Blank to disable").getStringList();

        Property propGoldenSpelunkers = cfg.get("game", "goldenSpelunkers", goldenSpelunkers,
                "For hardcore. How many Golden Spelunker (Fake) do players have?");
        goldenSpelunkers = propGoldenSpelunkers.getInt();

        goldenSpelunkerRecipe = cfg.get("game", "enableGoldenSpelunkerRecipe", side.isClient(),
                "If true, Golden Spelunker (Fake)'s recipe is enabled").getBoolean(side.isClient());

        cfg.save();
    }
}
