package tsuteto.spelunker.block;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import tsuteto.spelunker.block.tileentity.TileEntityItemBoxHidden;
import tsuteto.spelunker.block.tileentity.TileEntityItemSpawnPoint;

public class BlockItemSpawnPoint extends BlockContainer
{
    public BlockItemSpawnPoint(Material p_i45394_1_)
    {
        super(p_i45394_1_);
    }

    @Override
    public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_, Entity p_149670_5_)
    {
        super.onEntityCollidedWithBlock(p_149670_1_, p_149670_2_, p_149670_3_, p_149670_4_, p_149670_5_);

        if (!p_149670_1_.isRemote && p_149670_5_ instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)p_149670_5_;
            if (!player.capabilities.isCreativeMode)
            {
                TileEntityItemSpawnPoint te = (TileEntityItemSpawnPoint) p_149670_1_.getTileEntity(p_149670_2_, p_149670_3_, p_149670_4_);
                TileEntity itemBox = p_149670_1_.getTileEntity(te.targetX, te.targetY, te.targetZ);
                if (itemBox instanceof TileEntityItemBoxHidden)
                {
                    if (((TileEntityItemBoxHidden) itemBox).revealItem())
                    {
                        p_149670_1_.playSoundAtEntity(player, "spelunker:itemappeared", 1.0f, 1.0f);
                    }
                }
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityItemSpawnPoint();
    }

    @Override
    public int getRenderType()
    {
        return -1;
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
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World p_149633_1_, int p_149633_2_, int p_149633_3_, int p_149633_4_)
    {
        EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
        if (!player.capabilities.isCreativeMode)
        {
            return AxisAlignedBB.getBoundingBox((double)p_149633_2_, (double)p_149633_3_, (double)p_149633_4_,
                    (double)p_149633_2_, (double)p_149633_3_, (double)p_149633_4_);
        }
        else
        {
            return super.getSelectedBoundingBoxFromPool(p_149633_1_, p_149633_2_, p_149633_3_, p_149633_4_);
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_) {}
}
