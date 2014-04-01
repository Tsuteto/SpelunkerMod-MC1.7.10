package tsuteto.spelunker.eventhandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.BlockEvent;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class BlockEventHandler
{
    @SubscribeEvent
    public void onBlockDestroyed(BlockEvent.BreakEvent event)
    {
        EntityPlayer player = event.getPlayer();
        SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(player);
        if (spelunker != null)
        {
            spelunker.onBlockDestroyed(event.x, event.y, event.z, event.block);
        }
    }
}
