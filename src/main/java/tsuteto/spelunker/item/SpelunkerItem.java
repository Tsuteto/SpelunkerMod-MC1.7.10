package tsuteto.spelunker.item;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.constants.SpelunkerDifficulty;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

import java.util.List;

public abstract class SpelunkerItem extends Item
{

    public static List<SpelunkerItem> dropItemRegistry = Lists.newArrayList();

    public SpelunkerItem()
    {
        this(true);
    }

    public SpelunkerItem(boolean canDropInCave)
    {
        super();
        this.setCreativeTab(SpelunkerMod.tabLevelComponents);
        if (canDropInCave)
        {
            dropItemRegistry.add(this);
        }
    }

    @Override
    public Item setUnlocalizedName(String par1Str)
    {
        this.setTextureName(par1Str);
        return super.setUnlocalizedName(par1Str);
    }

    public abstract void giveEffect(ItemStack itemStack, World world, SpelunkerPlayerMP spelunker);

    public int getRarity(SpelunkerDifficulty difficulty)
    {
        return 10;
    }

    public boolean spawnCheck(SpelunkerPlayerMP spelunker, SpelunkerDifficulty difficulty, boolean isDarkPlace, double posY)
    {
        return true;
    }

    public void animate() {}

    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_)
    {
        p_77624_3_.add(StatCollector.translateToLocal("item.spelunker:dropItem.desc"));
    }
}
