package tsuteto.spelunker.eventhandler;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import tsuteto.spelunker.entity.IEntityPlatform;

import java.util.List;

public class BlockCollisionCheckHook
{
    public static boolean onBlockCollisionCheck(World world, AxisAlignedBB boundingBox)
    {
        List list = world.getEntitiesWithinAABB(IEntityPlatform.class, boundingBox.addCoord(0.0D, -1.0D, 0.0D));
        return !list.isEmpty();
    }
}
