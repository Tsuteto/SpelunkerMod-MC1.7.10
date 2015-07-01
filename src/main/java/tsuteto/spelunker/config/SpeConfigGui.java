package tsuteto.spelunker.config;

import com.google.common.collect.Lists;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import tsuteto.spelunker.Settings;
import tsuteto.spelunker.SpelunkerMod;

import java.util.List;

public class SpeConfigGui extends GuiConfig
{
    public SpeConfigGui(GuiScreen parent) {
        super(parent, getConfigElements(),
                SpelunkerMod.modId, false, false, StatCollector.translateToLocal("Spelunker.options.title"));
    }

    private static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> elements = Lists.newArrayList();

        for (String cat : Settings.CATEGORIES)
        {
            elements.add(new ConfigElement(getConf().getCategory(cat)));
        }

        return elements;
    }

    private static Configuration getConf()
    {
        return SpelunkerMod.settings().getCfg();
    }
}
