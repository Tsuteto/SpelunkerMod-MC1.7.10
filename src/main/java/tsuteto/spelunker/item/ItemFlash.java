package tsuteto.spelunker.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tsuteto.spelunker.entity.EntityFlash;

/**
 * Defines Flash, originally "IlluminatingFlare" by ayashige
 *
 * @author ayashige, Tsuteto
 *
 */
public class ItemFlash extends Item
{
    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3)
    {
        var3.setItemInUse(var1, this.getMaxItemUseDuration(var1));
        return var1;
    }

    /**
     * called when the player releases the use item button. Args: itemstack, world, entityplayer, itemInUseCount
     */
    @Override
    public void onPlayerStoppedUsing(ItemStack var1, World var2, EntityPlayer var3, int var4)
    {
        int var5 = this.getMaxItemUseDuration(var1) - var4;
        float var6 = (float)var5 / 20.0F + 0.2F;

        if (var6 > 10.0F)
        {
            var6 = 10.0F;
        }

        if (!var3.capabilities.isCreativeMode)
        {
            --var1.stackSize;

            if (var1.stackSize <= 0)
            {
                var3.inventory.mainInventory[var3.inventory.currentItem] = null;
            }
        }

        var2.playSoundAtEntity(var3, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!var2.isRemote)
        {
            var2.spawnEntityInWorld(new EntityFlash(var2, var3, var6));
        }
    }

    @Override
    public ItemStack onEaten(ItemStack var1, World var2, EntityPlayer var3)
    {
        return var1;
    }

    /**
     * How long it takes to use or consume an item
     */
    @Override
    public int getMaxItemUseDuration(ItemStack var1)
    {
        return 72000;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    @Override
    public EnumAction getItemUseAction(ItemStack var1)
    {
        return EnumAction.bow;
    }
}
