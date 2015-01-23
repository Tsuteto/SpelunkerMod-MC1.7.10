package tsuteto.spelunker.sound;

import net.minecraft.util.ResourceLocation;
import tsuteto.spelunker.SpelunkerMod;

public class SpelunkerBgm
{
    public static final ModSound bgmMain;
    public static final ModSound bgm2xScore;
    public static final ModSound bgmInvincible;
    public static final ModSound bgmSpeedPotion;
    public static final ModSound bgmAllCleared;
    public static final ModSound bgmGhostComing;

    static
    {
        bgmMain = ModSound.bgm(new ResourceLocation("spelunker:main_bgm"));
        bgmAllCleared = ModSound.bgm(new ResourceLocation("spelunker:allcleared"));

        float volume = SpelunkerMod.isBgmMainAvailable ? 1.0F : 0.75F;
        bgm2xScore = ModSound.bgm(new ResourceLocation("spelunker:2xscore_bgm"), volume);
        bgmInvincible = ModSound.bgm(new ResourceLocation("spelunker:invincible_bgm"), volume);
        bgmSpeedPotion = ModSound.bgm(new ResourceLocation("spelunker:speedpotion_bgm"), volume);
        bgmGhostComing = ModSound.bgm(new ResourceLocation("spelunker:ghost_bgm"), volume);
    }
}
