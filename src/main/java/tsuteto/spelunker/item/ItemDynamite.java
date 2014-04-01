package tsuteto.spelunker.item;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class ItemDynamite extends SpelunkerItem
{
    @Override
    public void giveEffect(World world, SpelunkerPlayerMP spelunker)
    {
        spelunker.player().inventory.addItemStackToInventory(new ItemStack(Blocks.tnt));
        spelunker.addSpelunkerScore(500);
        spelunker.playSoundAtEntity("spelunker:dynamite", 1.0F, 1.0F);
    }
}
