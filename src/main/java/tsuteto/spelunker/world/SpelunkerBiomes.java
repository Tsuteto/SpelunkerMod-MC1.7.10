package tsuteto.spelunker.world;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.config.Property;
import tsuteto.spelunker.SpelunkerMod;

public class SpelunkerBiomes
{
    public static BiomeGenBase spelunkerCave;

    public static void register()
    {
        SpelunkerMod.settings().biome();

        spelunkerCave = new BiomeGenSpelunkerCave(SpelunkerMod.settings().biomeSpelunkerCaveId)
                .setColor(8421631)
                .setBiomeName("SpelunkerCave")
                .setDisableRain();
    }

    public static int assignId(Property prop) throws Exception
    {
        int id = prop.getInt();
        if (id != -1)
        {
            return id;
        }
        else
        {
            // Find an undefined entry
            for (int i = 127; i >= 0; i--)
            {
                BiomeGenBase biome = BiomeGenBase.getBiomeGenArray()[i];
                if (biome == null)
                {
                    prop.set(i);
                    return i;
                }
            }
        }

        // Unable to find entry
        throw new RuntimeException("Failed to register BIOME. Seems to be running out of biome ID.");
    }

}
