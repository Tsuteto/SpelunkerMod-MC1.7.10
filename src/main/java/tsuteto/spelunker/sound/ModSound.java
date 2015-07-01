package tsuteto.spelunker.sound;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ModSound extends PositionedSound
{
    private static net.minecraft.client.audio.SoundHandler soundHandler;
    private static ModSound bgmNowPlaying = null;

    public static void init()
    {
        soundHandler = FMLClientHandler.instance().getClient().getSoundHandler();
    }

    public static ModSound bgm(ResourceLocation resourceLocation, float volume)
    {
        return bgm(resourceLocation, volume, true);
    }

    public static ModSound bgmWithoutRepeat(ResourceLocation resourceLocation, float volume)
    {
        return bgm(resourceLocation, volume, false);
    }

    public static ModSound bgm(ResourceLocation resourceLocation, float volume, boolean repeat)
    {
        return new ModSound(resourceLocation, volume, 1.0F, repeat, 0, AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
    }

    public synchronized static void playBgm(ModSound sound)
    {
        if (bgmNowPlaying != null) return;

        soundHandler.playSound(sound);
        SpelunkerBgm.addBgmPlaying(sound);
        bgmNowPlaying = sound;
    }

    public static void interruptBgm(ModSound sound)
    {
        if (bgmNowPlaying != null && bgmNowPlaying.equals(sound)) return;

        stopCurrentBgm();
        soundHandler.playSound(sound);
        SpelunkerBgm.addBgmPlaying(sound);
        bgmNowPlaying = sound;
    }

//    public static void playSound(ResourceLocation resourceLocation, double x, double y, double z, float volume, float pitch)
//    {
//        soundHandler.playSound(new PositionedSoundRecord(resourceLocation, (float) x, (float) y, (float) z, volume, pitch));
//    }

    public static void stopBgm(ResourceLocation resLoc)
    {
        List<ModSound> bgmList = SpelunkerBgm.pullBgmPlaying(resLoc);
        if (bgmList != null)
        {
            for (ModSound bgm : bgmList) soundHandler.stopSound(bgm);
        }
        SpelunkerBgm.bgmPlaying.remove(resLoc);
    }

    public static void stopCurrentBgm()
    {
        if (bgmNowPlaying != null && soundHandler.isSoundPlaying(bgmNowPlaying))
        {
            soundHandler.stopSound(bgmNowPlaying);
        }
    }

    public static boolean playing()
    {
        return bgmNowPlaying != null;
    }

    public static boolean isReady()
    {
        return soundHandler != null;
    }

    public static ISound getBgmNowPlaying()
    {
        return bgmNowPlaying;
    }

    public static boolean bgmPlayingEquals(ResourceLocation resourceLocation)
    {
        return bgmNowPlaying != null && bgmNowPlaying.getPositionedSoundLocation().equals(resourceLocation);
    }

    public static void updateBgmNowPlaying()
    {
        if (bgmNowPlaying != null)
        {
            boolean playing = isReady() && soundHandler.isSoundPlaying(bgmNowPlaying);
            if (!playing)
            {
                bgmNowPlaying = null;
            }
        }
    }

    public ModSound(ResourceLocation p_i45108_1_, float p_i45108_2_, float p_i45108_3_, boolean p_i45108_4_, int p_i45108_5_, ISound.AttenuationType p_i45108_6_, float p_i45108_7_, float p_i45108_8_, float p_i45108_9_)
    {
        super(p_i45108_1_);
        this.volume = p_i45108_2_;
        this.field_147663_c = p_i45108_3_;
        this.xPosF = p_i45108_7_;
        this.yPosF = p_i45108_8_;
        this.zPosF = p_i45108_9_;
        this.repeat = p_i45108_4_;
        this.field_147665_h = p_i45108_5_;
        this.field_147666_i = p_i45108_6_;
    }

    public ResourceLocation getSoundLocation()
    {
        return super.getPositionedSoundLocation();
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof ModSound && ((ModSound)obj).getSoundLocation().equals(this.getSoundLocation());
    }
}
