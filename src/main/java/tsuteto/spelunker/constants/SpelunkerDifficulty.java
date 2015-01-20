package tsuteto.spelunker.constants;

import com.google.common.collect.Maps;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Defines various parameters on Spelunker's difficulty
 * @author Tsuteto
 *
 */
public enum SpelunkerDifficulty
{
    PEACEFUL(20, 15, 0.9D,  5.0D,  0, true),
    EASY    (20, 10, 1.0D,  5.0D, 10, false),
    NORMAL  (10,  6, 0.9D, 10.0D, 20, false),
    HARD    (15,  4, 0.6D, 10.0D, 40, false);

    private static Map<EnumDifficulty, SpelunkerDifficulty> gameToSpelunkerMapping = Maps.newEnumMap(EnumDifficulty.class);

    static
    {
        PEACEFUL.allowProjectileBlocking(null);
        EASY.setGhostSpawn(0.75F).allowProjectileBlocking(null);
        NORMAL.setGhostSpawn(1.0F).allowProjectileBlocking(new PotionEffect(Potion.moveSlowdown.getId(), 5));
        HARD.setGhostSpawn(1.5F);

        gameToSpelunkerMapping.put(EnumDifficulty.PEACEFUL, PEACEFUL);
        gameToSpelunkerMapping.put(EnumDifficulty.EASY, EASY);
        gameToSpelunkerMapping.put(EnumDifficulty.NORMAL, NORMAL);
        gameToSpelunkerMapping.put(EnumDifficulty.HARD, HARD);
    }

    public int trials;
    public int lightLevel;
    public double chanceOfEnergy;
    public double spawnDensity;
    public int rarityNoItem;
    public boolean spawnInSight;
    public boolean allowProjectileBlocking;
    public PotionEffect debuffOnProjectileBlocking;
    public double ghostSpawnRate = 0;

    SpelunkerDifficulty(int trials, int lightLevel, double chanceOfEnergy, double spawnDensity, int rarityNoItem, boolean spawnInSight)
    {
        this.trials = trials;
        this.lightLevel = lightLevel;
        this.chanceOfEnergy = chanceOfEnergy;
        this.spawnDensity = spawnDensity;
        this.rarityNoItem = rarityNoItem;
        this.spawnInSight = spawnInSight;
    }

    public SpelunkerDifficulty setGhostSpawn(double rate)
    {
        this.ghostSpawnRate = rate;
        return this;
    }

    public void allowProjectileBlocking(@Nullable PotionEffect debuff)
    {
        this.debuffOnProjectileBlocking = debuff;
        this.allowProjectileBlocking = true;
    }

    public static SpelunkerDifficulty getSettings(EnumDifficulty gameDifficulty)
    {
        return gameToSpelunkerMapping.get(gameDifficulty);
    }

    public static SpelunkerDifficulty getSettings(World world)
    {
        return getSettings(world.difficultySetting);
    }
}
