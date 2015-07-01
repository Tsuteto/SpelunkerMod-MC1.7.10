package tsuteto.spelunker.eventhandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.BlockEvent;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.block.BlockSpelunkerPortal;
import tsuteto.spelunker.block.tileentity.TileEntitySpelunkerPortalStage;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class BlockEventHandler
{
    @SubscribeEvent
    public void onBlockDestroyed(BlockEvent.BreakEvent event)
    {
        EntityPlayer player = event.getPlayer();

        // Portal ownership
        if (event.block instanceof BlockSpelunkerPortal)
        {
            BlockSpelunkerPortal portal = (BlockSpelunkerPortal)event.block;
            TileEntitySpelunkerPortalStage te = portal.getTileEntity(event.world, event.x, event.y, event.z);
            if (!te.isOwner(event.getPlayer()))
            {
                event.setCanceled(true);
                return;
            }
        }

        // Kick Spelunker event
        SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(player);
        if (spelunker != null)
        {
            spelunker.onBlockDestroyed(event.x, event.y, event.z, event.block);
        }
    }
}
