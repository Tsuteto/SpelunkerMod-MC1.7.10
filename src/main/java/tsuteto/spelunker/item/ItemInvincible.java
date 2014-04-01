package tsuteto.spelunker.item;

import net.minecraft.world.World;
import tsuteto.spelunker.constants.SpelunkerDifficulty;
import tsuteto.spelunker.constants.SpelunkerPacketType;
import tsuteto.spelunker.packet.SpelunkerPacketDispatcher;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class ItemInvincible extends ItemAnimation
{
    @Override
    public void giveEffect(World world, SpelunkerPlayerMP spelunker)
    {
        spelunker.setInvincible(30 * 20);
        spelunker.playSoundAtEntity("spelunker:item", 1.0f, 1.0f);

        SpelunkerPacketDispatcher packet = new SpelunkerPacketDispatcher(SpelunkerPacketType.GOT_INVINCIBLE);
        packet.sendPacketPlayer(spelunker.player());
    }

    @Override
    public int getRarity(SpelunkerDifficulty difficulty)
    {
        return 3;
    }

    @Override
    public int getAnimationFlames()
    {
        return 2;
    }
}
