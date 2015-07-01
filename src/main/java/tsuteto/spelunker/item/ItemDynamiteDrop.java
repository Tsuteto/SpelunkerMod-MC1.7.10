package tsuteto.spelunker.item;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tsuteto.spelunker.init.SpelunkerItems;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class ItemDynamiteDrop extends SpelunkerItem
{
    @Override
    public void giveEffect(ItemStack itemStack, World world, SpelunkerPlayerMP spelunker)
    {
        spelunker.player().inventory.addItemStackToInventory(new ItemStack(SpelunkerItems.itemDynamite));
        spelunker.addSpelunkerScore(500);
        spelunker.playSound("spelunker:dynamite", 1.0F, 1.0F);
    }
}
