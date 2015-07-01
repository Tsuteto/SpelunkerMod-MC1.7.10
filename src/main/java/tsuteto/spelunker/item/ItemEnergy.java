package tsuteto.spelunker.item;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tsuteto.spelunker.constants.SpelunkerDifficulty;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class ItemEnergy extends ItemAnimation
{
    @Override
    public void giveEffect(ItemStack itemStack, World world, SpelunkerPlayerMP spelunker)
    {
        spelunker.addEnergy(3600);
        spelunker.setEnergyAlertTime(0);
        spelunker.addSpelunkerScore(100);
        spelunker.playSound("spelunker:energy", 1.0F, 1.0F);
    }

    @Override
    public int getRarity(SpelunkerDifficulty difficulty)
    {
        return (difficulty == SpelunkerDifficulty.HARD) ? 2 : 5;
    }

    @Override
    public int getAnimationFlames()
    {
        return 4;
    }
}
