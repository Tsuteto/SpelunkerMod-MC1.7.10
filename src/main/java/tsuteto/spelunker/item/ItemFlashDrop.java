package tsuteto.spelunker.item;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tsuteto.spelunker.constants.SpelunkerDifficulty;
import tsuteto.spelunker.init.SpelunkerItems;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class ItemFlashDrop extends SpelunkerItem
{
    @Override
    public void giveEffect(ItemStack itemStack, World world, SpelunkerPlayerMP spelunker)
    {
        spelunker.player().inventory.addItemStackToInventory(new ItemStack(SpelunkerItems.itemFlash));
        spelunker.addSpelunkerScore(300);
        spelunker.playSound("spelunker:flare", 1.0F, 1.0F);
    }

    @Override
    public int getRarity(SpelunkerDifficulty difficulty)
    {
        return 8;
    }
}
