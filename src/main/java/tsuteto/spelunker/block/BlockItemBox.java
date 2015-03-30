package tsuteto.spelunker.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.block.tileentity.TileEntityItemBox;
import tsuteto.spelunker.item.SpelunkerItem;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class BlockItemBox extends BlockRespawnPoint
{
    public BlockItemBox(Material p_i45394_1_)
    {
        super(p_i45394_1_);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        if (player.capabilities.isCreativeMode)
        {
            ItemStack heldItem = player.getHeldItem();
            if (heldItem != null)
            {
                TileEntityItemBox te = (TileEntityItemBox)world.getTileEntity(x, y, z);
                te.setItemStack(new ItemStack(heldItem.getItem(), 1, heldItem.getItemDamage()));
                return true;
            }
        }
        return false;
    }

    public boolean shouldSetRespawnPoint(World world, int x, int y, int z, Entity entity)
    {
        TileEntityItemBox te = (TileEntityItemBox)world.getTileEntity(x, y, z);
        if (te.itemContained != null)
        {
            Item item = te.itemContained.getItem();
            return entity instanceof EntityPlayer && item instanceof SpelunkerItem && item != SpelunkerItem.itemGateKey;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_, Entity p_149670_5_)
    {
        super.onEntityCollidedWithBlock(p_149670_1_, p_149670_2_, p_149670_3_, p_149670_4_, p_149670_5_);

        if (!p_149670_1_.isRemote && p_149670_5_ instanceof EntityPlayer)
        {
            onItemTaken(p_149670_1_, p_149670_2_, p_149670_3_, p_149670_4_, (EntityPlayer)p_149670_5_);
        }
    }

    public void onItemTaken(World world, int x, int y, int z, EntityPlayer player)
    {
        if (player.capabilities.isCreativeMode) return;

        SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(player);
        if (spelunker != null)
        {
            TileEntityItemBox te = (TileEntityItemBox)world.getTileEntity(x, y, z);
            if (te != null && te.isItemAvailable())
            {
                boolean taken;
                if (te.itemContained.getItem() instanceof SpelunkerItem)
                {
                    ((SpelunkerItem) te.itemContained.getItem()).giveEffect(player.worldObj, spelunker);
                    taken = true;
                }
                else
                {
                    taken = player.inventory.addItemStackToInventory(te.itemContained.copy());
                }

                if (taken)
                {
                    te.onItemTaken();
                    world.playSoundAtEntity(player, "random.pop", 0.2F, ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                }
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityItemBox();
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }
}
