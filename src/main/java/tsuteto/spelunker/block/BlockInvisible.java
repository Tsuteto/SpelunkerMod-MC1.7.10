package tsuteto.spelunker.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import tsuteto.spelunker.util.BlockUtils;

public class BlockInvisible extends BlockInsubstantial
{
    public BlockInvisible(Material p_i45394_1_)
    {
        super(p_i45394_1_);
    }

    // For inventory
    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
    {
        BlockUtils.setInvisibleBlockBounds(this);
        super.setBlockBoundsBasedOnState(p_149719_1_, p_149719_2_, p_149719_3_, p_149719_4_);
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_) {}
}
