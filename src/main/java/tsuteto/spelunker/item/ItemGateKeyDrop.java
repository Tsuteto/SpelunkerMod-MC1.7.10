package tsuteto.spelunker.item;

import net.minecraft.block.BlockColored;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tsuteto.spelunker.init.SpelunkerItems;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

import java.util.List;

public class ItemGateKeyDrop extends SpelunkerItem
{
    public ItemGateKeyDrop()
    {
        super(false);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public void giveEffect(ItemStack itemStack, World world, SpelunkerPlayerMP spelunker)
    {
        spelunker.player().inventory.addItemStackToInventory(new ItemStack(SpelunkerItems.itemGateKey, 1, itemStack.getItemDamage()));
        spelunker.addSpelunkerScore(itemStack.getItemDamage() == 14 ? 2000 : 1000);
        spelunker.playSound("spelunker:key", 1.0F, 1.0F);
    }

    @Override
    public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_)
    {
        int dmg = p_82790_1_.getItemDamage();
        return ItemDye.field_150922_c[BlockColored.func_150032_b(dmg)];
    }

    @Override
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_)
    {
        for (int i = 0; i < 16; i++)
        {
            p_150895_3_.add(new ItemStack(this, 1, i));
        }
    }

    public String getUnlocalizedName(ItemStack p_77667_1_)
    {
        return super.getUnlocalizedName() + "." + ItemDye.field_150923_a[BlockColored.func_150032_b(p_77667_1_.getItemDamage())];
    }
}
