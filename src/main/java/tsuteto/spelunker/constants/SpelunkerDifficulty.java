package tsuteto.spelunker.constants;

import net.minecraft.world.EnumDifficulty;

/**
 * Defines various parameters on Spelunker's difficulty
 * @author Tsuteto
 *
 */
public enum SpelunkerDifficulty
{
    PEACEFUL(EnumDifficulty.PEACEFUL, 20, 15, 0.9D,  5.0D, 0, true),
    EASY    (EnumDifficulty.EASY    , 20, 10, 1.0D,  5.0D, 10, false),
    NORMAL  (EnumDifficulty.NORMAL  , 10,  6, 0.9D, 10.0D, 20, false),
    HARD    (EnumDifficulty.HARD    , 15,  4, 0.6D, 10.0D, 40, false);

    public EnumDifficulty gameDifficulty;
    public int trials;
    public int lightLevel;
    public double chanceOfEnergy;
    public double spawnDensity;
    public int rarityNoItem;
    public boolean spawnInSight;

    SpelunkerDifficulty(EnumDifficulty gameDifficulty, int trials, int lightLevel, double chanceOfEnergy, double spawnDensity, int rarityNoItem, boolean spawnInSight)
    {
        this.gameDifficulty = gameDifficulty;
        this.trials = trials;
        this.lightLevel = lightLevel;
        this.chanceOfEnergy = chanceOfEnergy;
        this.spawnDensity = spawnDensity;
        this.rarityNoItem = rarityNoItem;
        this.spawnInSight = spawnInSight;
    }

    public static SpelunkerDifficulty getSettings(EnumDifficulty gameDifficulty)
    {
        for (SpelunkerDifficulty settings : SpelunkerDifficulty.values())
        {
            if (settings.gameDifficulty == gameDifficulty) return settings;
        }
        return null;
    }
}
