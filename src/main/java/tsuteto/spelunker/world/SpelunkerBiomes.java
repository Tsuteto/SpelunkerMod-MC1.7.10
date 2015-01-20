package tsuteto.spelunker.world;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import tsuteto.spelunker.util.ModLog;

public class SpelunkerBiomes
{
    public static BiomeGenBase spelunkerCave;

    private static final String CONF_CATEGORY = "biome";

    public static void register(Configuration conf)
    {
        conf.addCustomCategoryComment(CONF_CATEGORY, "Biome IDs. They must be 127 or less");

        try
        {
            spelunkerCave = new BiomeGenSpelunkerCave(assignId("spelunkerCave", conf))
                    .setColor(8421631)
                    .setBiomeName("SpelunkerCave")
                    .setDisableRain();
        }
        catch (Exception e)
        {
            ModLog.log(Level.WARN, e, e.getLocalizedMessage());
        }

    }

    public static int assignId(String confKey, Configuration conf) throws Exception
    {
        if (conf.hasKey(CONF_CATEGORY, confKey))
        {
            int id = conf.get(CONF_CATEGORY, confKey, -1).getInt();
            if (id != -1)
            {
                return id;
            }
        }
        // Find an undefined entry
        for (int i = 127; i >= 0; i--)
        {
            BiomeGenBase biome = BiomeGenBase.getBiomeGenArray()[i];
            if (biome == null)
            {
                conf.get(CONF_CATEGORY, confKey, i).set(i);
                return i;
            }
        }

        // Unable to find entry
        throw new RuntimeException("Failed to register BIOME. Seems to be running out of biome ID.");
    }

}
