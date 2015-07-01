package tsuteto.spelunker.block;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tsuteto.spelunker.block.tileentity.TileEntityLockedGate;
import tsuteto.spelunker.init.SpelunkerBlocks;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class BlockLockedGate extends BlockContainer
{
    public static int renderId = RenderingRegistry.getNextAvailableRenderId();
    public static final int overlayColor = new Color(160, 153, 16).getRGB();

    private IIcon iconOverlay;
    private IIcon iconSide;

    public BlockLockedGate()
    {
        super(Material.rock);
        this.setBlockUnbreakable();
        this.setResistance(6000000.0F);
    }

    @Override
    public IIcon getIcon(int side, int meta)
    {
        if (meta == 0 || meta == 1)
        {
            return side < 2 ? this.blockIcon : iconSide;
        }
        else
        {
            return side > 1 ? this.blockIcon : iconSide;
        }
    }

    @Override
    public int getRenderType()
    {
        return renderId;
    }

    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        super.registerBlockIcons(p_149651_1_);
        this.iconOverlay = p_149651_1_.registerIcon(this.getTextureName() + "_overlay");
        this.iconSide = p_149651_1_.registerIcon(this.getTextureName() + "_side");
    }

    public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
    {
        int l = BlockPistonBase.determineOrientation(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_);
        p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l, 2);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        if (isLocked(par1World, par2, par3, par4))
        {
            return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
        }
        else
        {
            return null;
        }
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
        if (isLocked(meta))
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        }
    }

        @Override
    public boolean isBlockSolid(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        return isLocked(blockAccess, x, y, z);
    }

    @Override
    public boolean isSideSolid(IBlockAccess blockAccess, int x, int y, int z, ForgeDirection dir)
    {
        return isLocked(blockAccess, x, y, z);
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
    public int getLightOpacity(IBlockAccess world, int x, int y, int z)
    {
        return isLocked(world, x, y, z) ? 255 : 0;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        TileEntityLockedGate te = new TileEntityLockedGate();
        te.colorId = metadata;
        return te;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        this.updateStatus(world, x, y, z, false);
    }

    public static boolean isLocked(IBlockAccess world, int x, int y, int z)
    {
        return isLocked(world.getBlockMetadata(x, y, z));
    }

    public static boolean isLocked(int meta)
    {
        return (meta & 8) == 0;
    }

    public void unlockGate(World world, int x, int y, int z)
    {
        this.updateStatus(world, x, y, z, true);
        // Locked in 10 minutes
        world.scheduleBlockUpdate(x, y, z, this, 12000);
    }

    public void updateStatus(World world, int x, int y, int z, boolean unlocking)
    {
        int meta = world.getBlockMetadata(x, y, z);
        ForgeDirection side = ForgeDirection.getOrientation(meta & 7);
        world.setBlockMetadataWithNotify(x, y, z, unlocking ? meta | 8 : meta & 7, 3);

        TileEntityLockedGate te = (TileEntityLockedGate)world.getTileEntity(x, y, z);

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            //  Select sideways gate blocks of the same direction as this block
            if (side.offsetX != 0 && dir.offsetX == 0
                    || side.offsetY != 0 && dir.offsetY == 0
                    || side.offsetZ != 0 && dir.offsetZ == 0)
            {
                int nx = x + dir.offsetX;
                int ny = y + dir.offsetY;
                int nz = z + dir.offsetZ;
                int nmeta = world.getBlockMetadata(nx, ny, nz);
                if (SpelunkerBlocks.blockLockedGate == world.getBlock(nx, ny, nz)
                        && (unlocking ? isLocked(nmeta) : !isLocked(nmeta))
                        && meta == nmeta)
                {
                    TileEntityLockedGate nte = (TileEntityLockedGate)world.getTileEntity(nx, ny, nz);
                    if (nte != null && te.colorId == nte.colorId)
                    {
                        updateStatus(world, nx, ny, nz, unlocking);
                    }
                }
            }
        }
    }

    // For inventory
    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public int colorMultiplier(IBlockAccess world, int x, int y, int z)
    {
        TileEntityLockedGate te = (TileEntityLockedGate)world.getTileEntity(x, y, z);
        if (te != null)
        {
            return ItemDye.field_150922_c[BlockColored.func_150032_b(te.colorId)];
        }
        else
        {
            return ItemDye.field_150922_c[0];
        }
    }

    public int colorMultiplierOverlay(IBlockAccess world, int x, int y, int z)
    {
        return overlayColor;
    }

    // For inventory
    @Override
    public int getRenderColor(int p_149741_1_)
    {
        return ItemDye.field_150922_c[BlockColored.func_150032_b(p_149741_1_)];
    }

    // For inventory
    public int getRenderColorOverlay(int metadata)
    {
        return overlayColor;
    }

    public static IIcon getFrontOverlayIcon()
    {
        return ((BlockLockedGate)SpelunkerBlocks.blockLockedGate).iconOverlay;
    }

    @Override
    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_)
    {
        for (int i = 0; i < 16; i++)
        {
            p_149666_3_.add(new ItemStack(this, 1, i));
        }
    }

    public String getUnlocalizedName(ItemStack p_77667_1_)
    {
        return super.getUnlocalizedName() + "." + ItemDye.field_150923_a[BlockColored.func_150032_b(p_77667_1_.getItemDamage())];
    }
}
