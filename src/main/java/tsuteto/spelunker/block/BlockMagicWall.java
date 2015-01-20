package tsuteto.spelunker.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.util.Utils;

public class BlockMagicWall extends BlockWall
{
    private IIcon transparent;

    public BlockMagicWall(Material p_i45394_1_)
    {
        super(p_i45394_1_);
        this.setLightOpacity(255);
    }

    @Override
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        if ((blockAccess.getBlockMetadata(x, y, z) & 7) == side)
        {
            return super.getIcon(blockAccess, x, y, z, side);
        }
        else
        {
            return transparent;
        }
    }

    public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
    {
        int l = Utils.determineBlockOrientation6(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_);
        p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l, 2);
    }

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        super.registerBlockIcons(p_149651_1_);
        this.transparent = p_149651_1_.registerIcon(SpelunkerMod.resourceDomain + "transparent");
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
