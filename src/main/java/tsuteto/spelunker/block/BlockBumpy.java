package tsuteto.spelunker.block;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.block.tileentity.TileEntityBlockBumpy;
import tsuteto.spelunker.util.Utils;

import java.util.List;

public class BlockBumpy extends Block implements ITileEntityProvider
{
    public static int renderId = RenderingRegistry.getNextAvailableRenderId();

    public static final int META_DIR_X = 0;
    public static final int META_DIR_Y = 1;

    private IIcon[] icons;

    public BlockBumpy(Material p_i45394_1_)
    {
        super(p_i45394_1_);
    }

    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        long rand = Utils.generateRandomFromCoord(x, y, z);
        return this.icons[(int)(rand & 1L)];
    }

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.icons = new IIcon[2];
        this.icons[0] = p_149651_1_.registerIcon(SpelunkerMod.resourceDomain + "wall1");
        this.icons[1] = p_149651_1_.registerIcon(SpelunkerMod.resourceDomain + "wall2");

        this.blockIcon = this.icons[0];
    }


    // Has reference to TileEntity. This should not be called from TileEntity
    public void setStepCollisionBox(IBlockAccess blockAccess, int x, int y, int z, int step)
    {
        this.setStepCollisionBox(blockAccess, x, y, z, getProgress(blockAccess, x, y, z), step);
    }

    public void setStepCollisionBox(IBlockAccess blockAccess, int x, int y, int z, int progress, int step)
    {
        int l = blockAccess.getBlockMetadata(x, y, z);
        float stepCoord1 = step * 0.5F;
        float stepCoord2 = stepCoord1 + 0.5F;
        float stepHeight = getStepHeight(progress, step);

        if (l == META_DIR_X)
        {
            this.setBlockBounds(stepCoord1, 0.0F, 0.0F, stepCoord2, stepHeight, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, stepCoord1, 1.0F, stepHeight, stepCoord2);
        }
    }

    public void addCollisionBoxesToList(World p_149743_1_, int p_149743_2_, int p_149743_3_, int p_149743_4_, AxisAlignedBB p_149743_5_, List p_149743_6_, Entity p_149743_7_)
    {
        this.setStepCollisionBox(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, 0);
        super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_, p_149743_7_);

        this.setStepCollisionBox(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, 1);
        super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_, p_149743_7_);

        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public static float getStepHeight(int p, int step)
    {
        int base = ((p <= 15 ? p : 15 - p) & 15);
        return (step == 0 ? 1 + base : 16 - base) / 16.0F;
    }

    public static float getStepHeight(IBlockAccess world, int x, int y, int z, int step)
    {
        return getStepHeight(getProgress(world, x, y, z), step);
    }

    public static int getProgress(IBlockAccess world, int x, int y, int z)
    {
        return ((TileEntityBlockBumpy)world.getTileEntity(x, y, z)).progress;
    }

    public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
    {
        int l = MathHelper.floor_double((double) (p_149689_5_.rotationYaw / 90.0F) + 0.5D) & 3;

        if (l == 1 || l == 3)
        {
            p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, META_DIR_X, 2);
        }
        else
        {
            p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, META_DIR_Y, 2);
        }

        this.initTileEntity(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_);
    }

    public void initTileEntity(World world, int x, int y, int z)
    {
        // Initialize TileEntity
        TileEntityBlockBumpy te = (TileEntityBlockBumpy)world.getTileEntity(x, y, z);
        if (te != null)
        {
            long rand = Utils.generateRandomFromCoord(te.xCoord, te.yCoord, te.zCoord);
            te.progress = (int) (rand & 31L);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityBlockBumpy();
    }

    @Override
    public int getRenderType()
    {
        return renderId;
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

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World p_149633_1_, int p_149633_2_, int p_149633_3_, int p_149633_4_)
    {
        this.setSelectedBoundingBox(p_149633_1_.getBlockMetadata(p_149633_2_, p_149633_3_, p_149633_4_));
        return super.getSelectedBoundingBoxFromPool(p_149633_1_, p_149633_2_, p_149633_3_, p_149633_4_);
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
    {
        this.setSelectedBoundingBox(p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_));
    }

    public void setSelectedBoundingBox(int meta)
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void setBlockBoundsForItemRender(int step)
    {
        if (step == 0)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.8F, 0.5F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.2F, 1.0F);
        }
    }
}
