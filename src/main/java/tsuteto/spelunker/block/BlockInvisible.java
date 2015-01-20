package tsuteto.spelunker.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockInvisible extends BlockInsubstantial
{
//    private String itemIconName;

    protected BlockInvisible(Material p_i45394_1_)
    {
        super(p_i45394_1_);
    }

    // For inventory
    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
    {
        this.setSelectedBoundingBox(p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_));
    }

    public void setSelectedBoundingBox(int meta)
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World p_149633_1_, int p_149633_2_, int p_149633_3_, int p_149633_4_)
    {
        this.setSelectedBoundingBox(p_149633_1_.getBlockMetadata(p_149633_2_, p_149633_3_, p_149633_4_));
        return super.getSelectedBoundingBoxFromPool(p_149633_1_, p_149633_2_, p_149633_3_, p_149633_4_);
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

//    @Override
//    public String getItemIconName()
//    {
//        return itemIconName;
//    }
//
//    public void setItemIconName(String itemIconName)
//    {
//        this.itemIconName = itemIconName;
//    }


}
