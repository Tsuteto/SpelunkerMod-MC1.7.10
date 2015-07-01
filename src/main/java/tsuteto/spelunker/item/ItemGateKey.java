package tsuteto.spelunker.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tsuteto.spelunker.block.BlockLockedGate;
import tsuteto.spelunker.block.tileentity.TileEntityLockedGate;
import tsuteto.spelunker.init.SpelunkerBlocks;

public class ItemGateKey extends Item implements SpelunkerWorldItem
{
    public ItemGateKey()
    {
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     */
    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        Block block = world.getBlock(x, y, z);

        if (block == SpelunkerBlocks.blockLockedGate && BlockLockedGate.isLocked(world, x, y, z))
        {
            TileEntityLockedGate te = (TileEntityLockedGate)world.getTileEntity(x, y, z);
            if (te != null && te.colorId == itemstack.getItemDamage())
            {
                ((BlockLockedGate) block).unlockGate(world, x, y, z);
                itemstack.stackSize--;
                return true;
            }
        }
        return false;
    }

    @Override
    public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_)
    {
        int dmg = p_82790_1_.getItemDamage();
        return ItemDye.field_150922_c[BlockColored.func_150032_b(dmg)];
    }

    public String getUnlocalizedName(ItemStack p_77667_1_)
    {
        return super.getUnlocalizedName() + "." + ItemDye.field_150923_a[BlockColored.func_150032_b(p_77667_1_.getItemDamage())];
    }
}
