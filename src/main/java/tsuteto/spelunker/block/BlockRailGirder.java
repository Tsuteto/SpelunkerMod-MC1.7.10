package tsuteto.spelunker.block;

import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockRailGirder extends BlockInsubstantial
{
    protected BlockRailGirder(Material p_i45394_1_)
    {
        super(p_i45394_1_);
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
    {
        return side == ForgeDirection.UP;
    }
}
