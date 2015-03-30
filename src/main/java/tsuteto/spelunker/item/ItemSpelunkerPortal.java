package tsuteto.spelunker.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemSpelunkerPortal extends ItemBlock
{
    public ItemSpelunkerPortal(Block block)
    {
        super(block);
        this.setHasSubtypes(true);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean p_77624_4_)
    {
        if (itemStack.getItemDamage() == 0)
        {
            list.add(StatCollector.translateToLocal("item.spelunker:spelunkerPortal.unused"));
        }
        else
        {
            //list.add(StatCollector.translateToLocalFormatted("item.spelunker:spelunkerPortal.level", info.levelName));
            list.add(StatCollector.translateToLocalFormatted("item.spelunker:spelunkerPortal.dimId", itemStack.getItemDamage()));
        }
    }
}
