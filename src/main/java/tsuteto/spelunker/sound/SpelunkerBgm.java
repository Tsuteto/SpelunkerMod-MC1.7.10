package tsuteto.spelunker.sound;

import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import tsuteto.spelunker.SpelunkerMod;

import java.util.LinkedList;
import java.util.Map;

public class SpelunkerBgm
{
    public static final ResourceLocation resLocMain = new ResourceLocation("spelunker:main_bgm");
    public static final ResourceLocation resLoc2xScore = new ResourceLocation("spelunker:2xscore_bgm");
    public static final ResourceLocation resLocInvincible = new ResourceLocation("spelunker:invincible_bgm");
    public static final ResourceLocation resLocSpeedPotion = new ResourceLocation("spelunker:speedpotion_bgm");
    public static final ResourceLocation resLocGhostComing = new ResourceLocation("spelunker:ghost_bgm");
    public static final ResourceLocation resLocCheckPoint = new ResourceLocation("spelunker:checkpoint");
    public static final ResourceLocation resLocAllCleared = new ResourceLocation("spelunker:allcleared");

    public static final Map<ResourceLocation, LinkedList<ModSound>> bgmPlaying = Maps.newHashMap();

    public static void addBgmPlaying(ModSound sound)
    {
        ResourceLocation resLoc = sound.getSoundLocation();
        if (!bgmPlaying.containsKey(resLoc))
        {
            bgmPlaying.put(resLoc, new LinkedList<ModSound>());
        }
        bgmPlaying.get(sound.getPositionedSoundLocation()).push(sound);
    }

    public static LinkedList<ModSound> pullBgmPlaying(ResourceLocation resLoc)
    {
        LinkedList<ModSound> list = bgmPlaying.get(resLoc);
        bgmPlaying.remove(resLoc);
        return list;
    }

    public static ModSound getMain()
    {
        return ModSound.bgm(resLocMain, 1.0F);
    }

    public static ModSound get2xScore()
    {
        return ModSound.bgm(resLoc2xScore, getVolume());
    }

    public static ModSound getInvincible()
    {
        return ModSound.bgm(resLocInvincible, getVolume());
    }

    public static ModSound getSpeedPotion()
    {
        return ModSound.bgm(resLocSpeedPotion, getVolume());
    }

    public static ModSound getGhostComing()
    {
        return ModSound.bgm(resLocGhostComing, getVolume());
    }

    public static ModSound getCheckPoint()
    {
        return ModSound.bgmWithoutRepeat(resLocCheckPoint, getVolume());
    }

    public static ModSound getAllCleared()
    {
        return ModSound.bgm(resLocAllCleared, getVolume());
    }

    private static float getVolume()
    {
        return SpelunkerMod.isBgmMainAvailable ? 1.0F : 0.75F;
    }
}
