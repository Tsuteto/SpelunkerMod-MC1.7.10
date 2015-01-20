package tsuteto.spelunker.item;

import net.minecraft.world.World;
import tsuteto.spelunker.constants.SpelunkerDifficulty;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class ItemCoin extends SpelunkerItem
{
    @Override
    public void giveEffect(World world, SpelunkerPlayerMP spelunker)
    {
        spelunker.addSpelunkerScore(1000);
        spelunker.playSound("spelunker:coin", 1.0F, 1.0F);
    }

    @Override
    public boolean spawnCheck(SpelunkerPlayerMP spelunker, SpelunkerDifficulty difficulty, boolean isDarkPlace, double posY)
    {
        return isDarkPlace;
    }

    @Override
    public int getRarity(SpelunkerDifficulty difficulty)
    {
        return (difficulty == SpelunkerDifficulty.HARD) ? 12 : 7;
    }
}
