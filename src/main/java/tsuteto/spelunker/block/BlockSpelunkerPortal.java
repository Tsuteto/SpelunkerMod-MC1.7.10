package tsuteto.spelunker.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.block.tileentity.TileEntitySpelunkerPortal;
import tsuteto.spelunker.dimension.SpelunkerDimensionTeleportation;
import tsuteto.spelunker.dimension.SpelunkerLevelInfo;
import tsuteto.spelunker.dimension.SpelunkerLevelManager;

import java.util.ArrayList;

public class BlockSpelunkerPortal extends BlockContainer4Directions
{
    SpelunkerDimensionTeleportation dimensionTeleportation = new SpelunkerDimensionTeleportation();

    public BlockSpelunkerPortal(Material p_i45386_1_)
    {
        super(p_i45386_1_);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        if (player instanceof EntityPlayerMP)
        {
            if (player.dimension == 0)
            {
                TileEntitySpelunkerPortal te = (TileEntitySpelunkerPortal)world.getTileEntity(x, y, z);
                if (te.travelTo == 0)
                {
                    te.travelTo = DimensionManager.getNextFreeDimId();
                }

                SpelunkerLevelManager levelManager = SpelunkerMod.getLevelManager();

                if (!DimensionManager.isDimensionRegistered(te.travelTo))
                {
                    // Register new dimension (with static parameters for now)
                    SpelunkerLevelInfo info = new SpelunkerLevelInfo();
                    info.dimId = te.travelTo;
                    info.levelName = "Test Level";
                    info.mapFileName = "testLevel.png";
                    levelManager.register(info);
                }
                levelManager.syncLevel(te.travelTo, player);
                dimensionTeleportation.transferPlayerToDimension((EntityPlayerMP) player, te.travelTo);
            }
            else
            {
                dimensionTeleportation.transferPlayerToDimension((EntityPlayerMP) player, 0);
            }
            return true;
        }
        else
        {
            return false;
        }

    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack)
    {
        super.onBlockPlacedBy(world, x, y, z, entityLivingBase, itemStack);

        if (!world.isRemote)
        {
            TileEntitySpelunkerPortal te = (TileEntitySpelunkerPortal) world.getTileEntity(x, y, z);
            te.travelTo = itemStack.getItemDamage();
        }
    }

    @Override
    public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer)
    {
        if (par6EntityPlayer.capabilities.isCreativeMode)
        {
            // Set 8 for no drops
            par5 |= 8;
            par1World.setBlockMetadataWithNotify(par2, par3, par4, par5, 4);
        }

        // Force to drop items here before removing tileentity
        dropBlockAsItem(par1World, par2, par3, par4, par5, 0);
        super.onBlockHarvested(par1World, par2, par3, par4, par5, par6EntityPlayer);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        if ((metadata & 8) == 0) // no drops when the metadata is 8
        {
            // Called twice, but drops items once when the tileentity is alive. Based on BlockSkull
            TileEntitySpelunkerPortal te = (TileEntitySpelunkerPortal)world.getTileEntity(x, y, z);
            if (te != null)
            {
                Item item = getItemDropped(metadata, world.rand, fortune);
                if (item != null)
                {
                    ret.add(new ItemStack(item, 1, te.travelTo));
                }
            }
        }
        return ret;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntitySpelunkerPortal();
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
    public int getRenderType()
    {
        return -1;
    }
}
