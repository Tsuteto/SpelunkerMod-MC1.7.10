package tsuteto.spelunker.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemBlockInvisible extends ItemBlock
{

    public ItemBlockInvisible(Block p_i45328_1_)
    {
        super(p_i45328_1_);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal(this.getUnlocalizedName() + ".desc"));
    }

}
