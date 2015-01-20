package tsuteto.spelunker.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class BlockPlatform extends Block
{

    protected BlockPlatform(Material p_i45394_1_)
    {
        super(p_i45394_1_);
        float h = 5.0F / 16.0F;
        this.setBlockBounds(0.0F, 1.0F - h, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB collidingBB, List boxes, Entity entity)
    {
        AxisAlignedBB blockBB = this.getCollisionBoundingBoxFromPool(world, x, y, z);

        if (entity == null || entity.boundingBox.minY > blockBB.minY && entity.motionY <= 0.0D)
        {
            if (blockBB != null && collidingBB.intersectsWith(blockBB))
            {
                boxes.add(blockBB);
            }
        }
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

}