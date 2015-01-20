package tsuteto.spelunker.item;

import net.minecraft.world.World;
import tsuteto.spelunker.constants.SpelunkerDifficulty;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class ItemDoller extends SpelunkerItem
{
    @Override
    public void giveEffect(World world, SpelunkerPlayerMP spelunker)
    {
        spelunker.addSpelunkerScore(500);
        spelunker.playSound("spelunker:dollar", 1.0F, 1.0F);
    }

    @Override
    public boolean spawnCheck(SpelunkerPlayerMP spelunker, SpelunkerDifficulty difficulty, boolean isDarkPlace, double posY)
    {
        return isDarkPlace;
    }
}
