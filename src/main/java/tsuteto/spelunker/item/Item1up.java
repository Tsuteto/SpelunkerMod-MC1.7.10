package tsuteto.spelunker.item;

import net.minecraft.world.World;
import tsuteto.spelunker.constants.SpelunkerDifficulty;
import tsuteto.spelunker.constants.SpelunkerGameMode;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class Item1up extends SpelunkerItem
{
    @Override
    public void giveEffect(World world, SpelunkerPlayerMP spelunker)
    {
        if (spelunker.gameMode == SpelunkerGameMode.Arcade)
        {
            spelunker.increaseLife(1);
        }
        else
        {
            spelunker.player().addExperience(30);
        }
        spelunker.playSoundAtEntity("spelunker:1up", 1.0F, 1.0F);
    }

    @Override
    public boolean spawnCheck(SpelunkerPlayerMP spelunker, SpelunkerDifficulty difficulty, boolean isDarkPlace, double posY)
    {
        return spelunker.gameMode == SpelunkerGameMode.Arcade
                && posY <= 20 && posY >= 6 && isDarkPlace;
    }
}
