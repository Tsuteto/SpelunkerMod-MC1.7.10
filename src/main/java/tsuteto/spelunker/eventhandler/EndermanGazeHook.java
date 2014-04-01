package tsuteto.spelunker.eventhandler;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.damage.SpelunkerDamageSource;
import tsuteto.spelunker.player.ISpelunkerPlayer;

public class EndermanGazeHook
{
    public static void onEndermanGazing(EntityEnderman enderman, EntityPlayer target)
    {
        ISpelunkerPlayer spelunker = SpelunkerMod.getSpelunkerPlayer(target);
        if (spelunker != null && spelunker.isHardcore())
        {
            target.attackEntityFrom(SpelunkerDamageSource.enderGaze, 4.0F);
        }
    }
}
