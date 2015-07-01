package tsuteto.spelunker.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.entity.EntityFallingFloor;
import tsuteto.spelunker.init.SpelunkerBlocks;

import java.util.EnumSet;
import java.util.Random;

public class BlockFallingFloor extends BlockWall
{
    public static final int META_HELD = 0;
    public static final int META_FALLEN = 1;

    private IIcon iconTransparent;
//    private IIcon iconHighlighted;

    public BlockFallingFloor()
    {
        super(Material.rock);
    }

    @Override
    public IIcon getIcon(int side, int meta)
    {
//        ItemStack itemHeld = FMLClientHandler.instance().getClient().thePlayer.getHeldItem();
//        if (itemHeld != null && itemHeld.getItem() == Item.getItemFromBlock(this))
//        {
//            return iconHighlighted;
//        }
//        else
        if (meta == META_HELD)
        {
            return super.getIcon(side, meta);
        }
        else
        {
            return iconTransparent;
        }
    }

    @Override
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        return this.getIcon(side, blockAccess.getBlockMetadata(x, y, z));
    }

        @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        int meta = par1World.getBlockMetadata(par2, par3, par4);
        if (meta == META_HELD)
        {
            double d = 0.125D;
            return AxisAlignedBB.getBoundingBox((double) par2, (double) par3, (double) par4, (double) (par2 + 1), (par3 + 1) - d, (double) (par4 + 1));
        }
        else
        {
            return null;
        }
    }

    @Override
    public boolean isBlockSolid(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        int meta = blockAccess.getBlockMetadata(x, y, z);
        return meta == META_HELD;
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
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        int meta = world.getBlockMetadata(x, y, z);

        if (meta == META_FALLEN)
        {
            this.updateStatus(world, x, y, z, META_HELD);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        int meta = world.getBlockMetadata(x, y, z);

        if (meta == META_HELD)
        {
            this.updateStatus(world, x, y, z, META_FALLEN);
        }
        super.onEntityCollidedWithBlock(world, x, y, z, entity);
    }

    private void updateStatus(World world, int x, int y, int z, int meta)
    {
        world.setBlockMetadataWithNotify(x, y, z, meta, 3);
        world.notifyBlockOfNeighborChange(x, y, z, this);

        if (meta == META_FALLEN)
        {
            world.spawnEntityInWorld(new EntityFallingFloor(world, x + 0.5D, y + 0.5D, z + 0.5D, this, META_HELD));
            // Restored in 200 ticks
            world.scheduleBlockUpdate(x, y, z, this, 600);
        }

        for (ForgeDirection dir : EnumSet.of(ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST))
        {
            int i = dir.offsetX;
            int j = dir.offsetZ;
            if (world.getBlock(x + i, y, z + j) == SpelunkerBlocks.blockFallingFloor
                    && world.getBlockMetadata(x + i, y, z + j) != meta)
            {
                this.updateStatus(world, x + i, y, z + j, meta);
            }
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        super.registerBlockIcons(par1IconRegister);
//        this.iconHighlighted = par1IconRegister.registerIcon(SpelunkerMod.resourceDomain + "fallingFloor_hl");
        this.iconTransparent = par1IconRegister.registerIcon(SpelunkerMod.resourceDomain + "transparent");
    }
}
