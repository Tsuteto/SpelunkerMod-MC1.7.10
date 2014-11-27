package tsuteto.spelunker.eventhandler;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import tsuteto.spelunker.entity.EntityElevator;
import tsuteto.spelunker.entity.EntityLift;

import java.util.List;

public class BlockCollisionCheckHook
{
    public static boolean onBlockCollisionCheck(World world, AxisAlignedBB boundingBox)
    {
        List list;
        list = world.getEntitiesWithinAABB(EntityElevator.class, boundingBox.addCoord(0.0D, -1.0D, 0.0D));
        if (!list.isEmpty()) return true;

        list = world.getEntitiesWithinAABB(EntityLift.class, boundingBox.addCoord(0.0D, -1.0D, 0.0D));
        if (!list.isEmpty()) return true;

        return false;
    }
}
