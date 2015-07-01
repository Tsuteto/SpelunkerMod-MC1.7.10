package tsuteto.spelunker.item;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tsuteto.spelunker.constants.SpelunkerDifficulty;
import tsuteto.spelunker.constants.SpelunkerPacketType;
import tsuteto.spelunker.network.SpelunkerPacketDispatcher;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class Item2xScore extends SpelunkerItem
{
    @Override
    public void giveEffect(ItemStack itemStack, World world, SpelunkerPlayerMP spelunker)
    {
        spelunker.set2xScore(90 * 20);
        spelunker.playSound("spelunker:2xscore", 1.0f, 1.0f);

        SpelunkerPacketDispatcher packet = new SpelunkerPacketDispatcher(SpelunkerPacketType.GOT_2x);
        packet.sendPacketPlayer(spelunker.player());
    }

    @Override
    public int getRarity(SpelunkerDifficulty difficulty)
    {
        return (difficulty == SpelunkerDifficulty.HARD) ? 10 : 5;
    }

}
