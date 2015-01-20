package tsuteto.spelunker.dimension;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.DimensionManager;
import tsuteto.spelunker.SpelunkerMod;

import java.util.List;

@SideOnly(Side.CLIENT)
public class SpelunkerLevelManagerClient
{
    private static List<Integer> registeredDims = Lists.newArrayList();

    public static void register(int dimId)
    {
        if (!registeredDims.contains(dimId))
        {
            if (!DimensionManager.isDimensionRegistered(dimId))
            {
                DimensionManager.registerDimension(dimId, SpelunkerMod.settings().dimTypeId);
            }
            registeredDims.add(dimId);
        }
    }

    public static void unregisterAll()
    {
        for (int dimId : registeredDims)
        {
            if (DimensionManager.isDimensionRegistered(dimId))
            {
                DimensionManager.unregisterDimension(dimId);
            }
        }
        registeredDims.clear();
    }
}
