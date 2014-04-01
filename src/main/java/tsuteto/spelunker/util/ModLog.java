package tsuteto.spelunker.util;

import cpw.mods.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

/**
 * Logger
 *
 * @author Tsuteto
 *
 */
public class ModLog
{
    public static String mod;
    public static boolean isDebug;

    public static void log(Level level, Throwable t, String format, Object... data)
    {
        FMLLog.log(mod, level, t, format, data);
    }

    public static void log(Level level, String format, Object... data)
    {
        FMLLog.log(mod, level, format, data);
    }

    public static void info(String format, Object... data)
    {
        FMLLog.log(mod, Level.INFO, format, data);
    }

    public static void warn(Throwable t, String format, Object... data)
    {
        FMLLog.log(mod, Level.WARN, t, format, data);
    }

    public static void debug(String format, Object... data)
    {
        if (isDebug)
        {
            System.out.println(String.format("[" + mod + "]" + format, data));
        }
    }
}
