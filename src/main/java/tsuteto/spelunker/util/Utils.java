package tsuteto.spelunker.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import tsuteto.spelunker.SpelunkerMod;

import java.net.URL;

public class Utils
{
    public static final int[] vanillaColors = new int[]{
            0,
            170,
            43520,
            43690,
            11141120,
            11141290,
            16755200,
            11184810,
            5592405,
            5592575,
            5635925,
            5636095,
            16733525,
            16733695,
            16777045,
            16777215
    };

    public static Vec3 getHeadLook(EntityLivingBase entity)
    {
        float f = MathHelper.cos(-entity.rotationYawHead * 0.01745329F - (float) Math.PI);
        float f2 = MathHelper.sin(-entity.rotationYawHead * 0.01745329F - (float) Math.PI);
        float f4 = -MathHelper.cos(-entity.rotationPitch * 0.01745329F);
        float f6 = MathHelper.sin(-entity.rotationPitch * 0.01745329F);
        return Vec3.createVectorHelper(f2 * f4, f6, f * f4);
    }

    public static long generateRandomFromCoord(int x, int y, int z)
    {
        long i1 = (long) (x * 3129871) ^ (long) y * 116129781L ^ (long) z;
        i1 = i1 * i1 * 42317861L + i1 * 11L + i1;
        return i1 >> 16;
    }

    public static boolean soundFileExists(String filename)
    {
        for (String ext : new String[]{"ogg"})
        {
            String rsrcpath = String.format("/assets/spelunker/sounds/%s.%s", filename, ext);
            URL url = SpelunkerMod.class.getResource(rsrcpath);
            if (url != null)
            {
                return true;
            }
        }
        return false;
    }

    public static String removeFileExtension(String filename)
    {
        int dotpos = filename.lastIndexOf(".");
        return filename.substring(0, dotpos);
    }

    public static String formatTickToTime(int tick, boolean addFrac)
    {
        int millisec = tick * 50;
        int min = millisec / 60 / 1000;
        int sec = millisec / 1000 % 60;
        if (addFrac)
        {
            return String.format("%d:%02d.%d", min, sec, millisec / 100 % 10);
        }
        else
        {
            return String.format("%d:%02d", min, sec);
        }
    }
}
