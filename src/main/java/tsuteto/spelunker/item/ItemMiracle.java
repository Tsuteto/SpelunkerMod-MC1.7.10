package tsuteto.spelunker.item;

import net.minecraft.world.World;
import tsuteto.spelunker.constants.SpelunkerDifficulty;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class ItemMiracle extends ItemAnimation
{
    @Override
    public void giveEffect(World world, SpelunkerPlayerMP spelunker)
    {
        int tick = (int) (world.getTotalWorldTime() & 7);
        // System.out.println("Got Miracle: " + tick);

        switch (tick)
        {
        case 0:
        case 7:
            ((SpelunkerItem)SpelunkerItem.item1up).giveEffect(world, spelunker);
            break;
        case 1:
            ((SpelunkerItem)SpelunkerItem.itemFlashDrop).giveEffect(world, spelunker);
            break;
        case 2:
        case 3:
            ((SpelunkerItem)SpelunkerItem.itemEnergy).giveEffect(world, spelunker);
            break;
        case 4:
            ((SpelunkerItem)SpelunkerItem.itemDynamiteDrop).giveEffect(world, spelunker);
            break;
        case 5:
            ((SpelunkerItem)SpelunkerItem.itemCoin).giveEffect(world, spelunker);
            break;
        case 6:
            ((SpelunkerItem)SpelunkerItem.itemDollar).giveEffect(world, spelunker);
            break;
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
