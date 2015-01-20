package tsuteto.spelunker.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import tsuteto.spelunker.world.gen.WorldGenSpelunkerLevel;
import tsuteto.spelunker.world.levelmapper.SpelunkerLevelMapper;

import java.util.List;

public class ItemLevelBuilder extends Item
{
    @Override
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        if (!p_77648_3_.isRemote)
        {
            SpelunkerLevelMapper mapper = SpelunkerLevelMapper.getInstance();
            int w = mapper.getWidth();
            int h = mapper.getHeight();
            // Remove entities
            List list = p_77648_3_.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBox(p_77648_4_, p_77648_5_, p_77648_6_, p_77648_4_ + w + 1, p_77648_5_ + h + 1, p_77648_6_ + 4));
            for (Object entity : list)
            {
                ((Entity) entity).setDead();
            }

            new WorldGenSpelunkerLevel(true).generate(p_77648_3_, itemRand, p_77648_4_, p_77648_5_, p_77648_6_);
        }
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
