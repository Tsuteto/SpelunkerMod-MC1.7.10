package tsuteto.spelunker.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.player.ISpelunkerPlayer;
import tsuteto.spelunker.world.WorldGenSpelunkerLevel;

import java.util.List;

public class ItemLevelBuilder extends Item
{
    @Override
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        new WorldGenSpelunkerLevel().generate(p_77648_3_, itemRand, p_77648_4_, p_77648_5_, p_77648_6_);
        return true;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("item.spelunker:levelBuilder.desc.1"));
        par3List.add(StatCollector.translateToLocal("item.spelunker:levelBuilder.desc.2"));
        par3List.add(StatCollector.translateToLocal("item.spelunker:levelBuilder.desc.3"));
    }
}
