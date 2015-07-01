package tsuteto.spelunker.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tsuteto.spelunker.SpelunkerMod;

import java.util.Random;

public class BlockBreakable extends BlockWall
{
    public static final int META_EXISTING = 0;
    public static final int META_DESTROYED = 8;

    protected IIcon transparent;

    public BlockBreakable(Material p_i45394_1_)
    {
        super(p_i45394_1_);
    }

    @Override
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        if (this.blockExists(blockAccess, x, y, z))
        {
            return super.getIcon(blockAccess, x, y, z, side);
        }
        else
        {
            return transparent;
        }
    }

    public boolean blockExists(IBlockAccess blockAccess, int x, int y, int z)
    {
        return this.blockExists(blockAccess.getBlockMetadata(x, y, z));
    }

    public boolean blockExists(int meta)
    {
        return (meta & 8) == META_EXISTING;
    }

    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        // Restore block from transparent
        int meta = world.getBlockMetadata(x, y, z);
        world.setBlockMetadataWithNotify(x, y, z, meta & 7, 3);
    }


    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion)
    {
        // Place a transparent block after explosion
        int meta = world.getBlockMetadata(x, y, z);
        world.setBlock(x, y, z, this, META_DESTROYED | meta, 3);
        world.scheduleBlockUpdate(x, y, z, this, SpelunkerMod.restorationTime);
    }

    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        super.registerBlockIcons(p_149651_1_);
        this.transparent = p_149651_1_.registerIcon(SpelunkerMod.resourceDomain + "transparent");
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        if (this.blockExists(par1World, par2, par3, par4))
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
        if (this.blockExists(meta))
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
        return this.blockExists(blockAccess, x, y, z);
    }

    @Override
    public boolean isSideSolid(IBlockAccess blockAccess, int x, int y, int z, ForgeDirection dir)
    {
        return this.blockExists(blockAccess, x, y, z);
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
        return this.blockExists(world, x, y, z) ? 255 : 0;
    }
}
