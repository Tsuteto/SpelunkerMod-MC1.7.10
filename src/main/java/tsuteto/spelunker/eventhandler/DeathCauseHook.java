package tsuteto.spelunker.eventhandler;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import org.apache.commons.lang3.StringUtils;
import tsuteto.spelunker.Settings;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.util.FieldAccessor;

public class DeathCauseHook
{
    public static String onDeathCauseReplace(String orgKey)
    {
        if (StringUtils.isNotEmpty(orgKey) && orgKey.startsWith("death."))
        {
            String speKey = (SpelunkerMod.settings().moodyDeathCause ? "spe.moo." : "spe.nor.") + orgKey;

            // Replace if the key exists
            if (StatCollector.canTranslate(speKey))
            {
                return speKey;
            }
        }
        return orgKey;
    }
}
