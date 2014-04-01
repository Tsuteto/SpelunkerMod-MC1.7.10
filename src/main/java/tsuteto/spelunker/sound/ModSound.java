package tsuteto.spelunker.sound;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class ModSound extends PositionedSound
{
    private static net.minecraft.client.audio.SoundHandler soundHandler;
    private static ISound bgmNowPlaying = null;
    private static int ticksInterval = -1;

    public static void init()
    {
        soundHandler = FMLClientHandler.instance().getClient().getSoundHandler();
    }

    public static ModSound bgm(ResourceLocation resourceLocation)
    {
        return new ModSound(resourceLocation, 1.0F, 1.0F, true, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
    }

    public static void playBgm(ISound sound)
    {
        if (bgmNowPlaying != null || ticksInterval != -1) return;

        soundHandler.playSound(sound);
        bgmNowPlaying = sound;
        ticksInterval = 0;
    }

    public static void playSound(ResourceLocation resourceLocation, double x, double y, double z, float volume, float pitch)
    {
        soundHandler.playSound(new PositionedSoundRecord(resourceLocation, (float) x, (float) y, (float) z, volume, pitch));
    }

    public static void stopBgm(ISound sound)
    {
        soundHandler.stopSound(sound);
    }

    public static void stopCurrentBgm()
    {
        if (bgmNowPlaying != null)
        {
            soundHandler.stopSound(bgmNowPlaying);
        }
    }

    public static boolean playing()
    {
        return bgmNowPlaying != null && ticksInterval < 20;
    }

    public static boolean isReady()
    {
        return soundHandler != null;
    }

    public static ISound getBgmNowPlaying()
    {
        return bgmNowPlaying;
    }

    public static void updateBgmNowPlaying()
    {
        if (bgmNowPlaying != null)
        {
            boolean playing = isReady() && soundHandler.isSoundPlaying(bgmNowPlaying);
            if (!playing)
            {
                bgmNowPlaying = null;
                ticksInterval = 0;
            }
        }
        if (ticksInterval != -1)
        {
            if (++ticksInterval == 20) ticksInterval = -1;
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
}
