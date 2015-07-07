package tsuteto.spelunker.player;

import net.minecraft.util.DamageSource;
import tsuteto.spelunker.data.ScoreManager;

/**
 * Spelunker interface non-sided
 *
 * @author Tsuteto
 *
 */
public interface ISpelunkerPlayer
{
    ScoreManager getSpelunkerScore();

    int getGunInUseCount();

    void setGunInUseCount(int i);

    int getGunDamageCount();

    void setGunDamageCount(int i);

    boolean isUsingEnergy();

    void decreaseEnergy(int i);

    void addSpelunkerScore(int amount);

    boolean is2xScore();

    boolean isInvincible();

    boolean isSpeedPotion();

    boolean isGhostComing();

    void killInstantly(DamageSource dmgsrc);

    boolean isHardcore();
}
