package tsuteto.spelunker.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import tsuteto.spelunker.SpelunkerMod;

public class BlockInsubstantial extends Block
{
    protected BlockInsubstantial(Material p_i45394_1_)
    {
        super(p_i45394_1_);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
            return null;
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

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon(SpelunkerMod.resourceDomain + "transparent");
    }
}
