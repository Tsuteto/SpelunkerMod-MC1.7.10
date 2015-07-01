package tsuteto.spelunker.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockRock extends BlockBreakable
{

    public BlockRock(Material p_i45394_1_)
    {
        super(p_i45394_1_);
    }

    @Override
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        if (this.blockExists(blockAccess, x, y, z))
        {
            return this.blockIcon;
        }
        else
        {
            return this.transparent;
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        super.registerBlockIcons(p_149651_1_);
        this.blockIcon = p_149651_1_.registerIcon(this.getTextureName());
    }

}
