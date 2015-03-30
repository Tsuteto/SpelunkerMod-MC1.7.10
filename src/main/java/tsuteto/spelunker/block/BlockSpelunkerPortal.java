package tsuteto.spelunker.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.block.tileentity.TileEntitySpelunkerPortal;
import tsuteto.spelunker.dimension.SpelunkerDimensionTeleportation;
import tsuteto.spelunker.gui.SpelunkerGuiHandler;
import tsuteto.spelunker.util.PlayerUtils;

import java.util.ArrayList;

public class BlockSpelunkerPortal extends BlockContainer4Directions
{
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
                if (te.levelInfo == null)
                {
                    this.openMapSelectorGui(world, x, y, z, player, te);
                }
                else
                {
                    if (player.isSneaking())
                    {
                        this.openMapSelectorGui(world, x, y, z, player, te);
                    }
                    else
                    {
                        this.teleportPlayerToLevel(world, x, y, z, player, te);
                    }
                }
                return true;
            }
            else
            {
                SpelunkerDimensionTeleportation.transferPlayerToDimension((EntityPlayerMP) player, 0);
            }
            return true;
        }
        else
        {
            return true;
        }

    }

    public  void openMapSelectorGui(World world, int x, int y, int z, EntityPlayer player, TileEntitySpelunkerPortal te)
    {
        if (te != null)
        {
            if (te.isOwner(player))
            {
                player.openGui(SpelunkerMod.instance, SpelunkerGuiHandler.GUIID_SPELUNKER_PORTAL, world, x, y, z);
            }
            else
            {
                EntityLivingBase ownerEntity = te.getOwner();
                if (ownerEntity != null)
                {
                    player.addChatComponentMessage(new ChatComponentTranslation("tile.spelunker:spelunkerPortal.denied.on", ownerEntity.getCommandSenderName()));
                }
                else
                {
                    player.addChatComponentMessage(new ChatComponentTranslation("tile.spelunker:spelunkerPortal.denied.off"));
                }
            }
        }
    }

    public void teleportPlayerToLevel(World world, int x, int y, int z, EntityPlayer player, TileEntitySpelunkerPortal te)
    {
        SpelunkerMod.levelManager().syncLevel(te.levelInfo.dimId, player);
        PlayerUtils.updatePlayerSpawnPoint(world, x, y, z, player);
        SpelunkerDimensionTeleportation.transferPlayerToDimension((EntityPlayerMP) player, te.levelInfo.dimId);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack)
    {
        super.onBlockPlacedBy(world, x, y, z, entityLivingBase, itemStack);

        if (!world.isRemote)
        {
            TileEntitySpelunkerPortal te = (TileEntitySpelunkerPortal) world.getTileEntity(x, y, z);
            if (itemStack.getItemDamage() != 0)
            {
                te.levelInfo = SpelunkerMod.levelManager().getLevelInfo(itemStack.getItemDamage());
            }
            if (entityLivingBase instanceof EntityPlayer)
            {
                te.owner = entityLivingBase.getUniqueID().toString();
            }
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
                    ItemStack stack;
                    if (te.levelInfo != null)
                    {
                        stack = new ItemStack(item, 1, te.levelInfo.dimId);
                    }
                    else
                    {
                        stack = new ItemStack(item, 1, 0);
                    }
                    ret.add(stack);
                }
            }
        }
        return ret;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        Item item = Item.getItemFromBlock(this);
        TileEntitySpelunkerPortal te = (TileEntitySpelunkerPortal)world.getTileEntity(x, y, z);
        if (te.levelInfo != null)
        {
            return new ItemStack(item, 1, te.levelInfo.dimId);
        }
        else
        {
            return new ItemStack(item, 1, 0);
        }
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
