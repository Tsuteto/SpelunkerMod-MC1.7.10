package tsuteto.spelunker.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tsuteto.spelunker.constants.SpelunkerDifficulty;
import tsuteto.spelunker.init.SpelunkerItems;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class ItemMiracle extends ItemAnimation
{
    @Override
    public void giveEffect(ItemStack itemStack, World world, SpelunkerPlayerMP spelunker)
    {
        int tick = (int) (world.getTotalWorldTime() & 7);
        // System.out.println("Got Miracle: " + tick);
        Item item = null;

        switch (tick)
        {
        case 0:
        case 7:
            item = SpelunkerItems.item1up;
            break;
        case 1:
            item = SpelunkerItems.itemFlashDrop;
            break;
        case 2:
        case 3:
            item = SpelunkerItems.itemEnergy;
            break;
        case 4:
            item = SpelunkerItems.itemDynamiteDrop;
            break;
        case 5:
            item = SpelunkerItems.itemCoin;
            break;
        case 6:
            item = SpelunkerItems.itemDollar;
            break;
        }
        if (item != null)
        {
            ((SpelunkerItem) item).giveEffect(new ItemStack(item), world, spelunker);
        }
    }

    @Override
    public boolean spawnCheck(SpelunkerPlayerMP spelunker, SpelunkerDifficulty difficulty, boolean isDarkPlace, double posY)
    {
        return posY <= 30 && isDarkPlace;
    }

    @Override
    public int getRarity(SpelunkerDifficulty difficulty)
    {
        return 15;
    }

    @Override
    public void animate()
    {
        this.itemIcon = this.icons[animationTick / 16];
        animationTick = ++animationTick % 32;
    }

    @Override
    public int getAnimationFlames()
    {
        return 2;
    }
}
