package tsuteto.spelunker.eventhandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import tsuteto.spelunker.entity.BatDroppingsHandler;

/**
 * Handles game tick on server side
 *
 * @author Tsuteto
 *
 */
public class ServerTickHandler
{

    @SubscribeEvent
    public void onTick(TickEvent event)
    {
        if (event.type == TickEvent.Type.SERVER && event.phase == TickEvent.Phase.END)
        {
            World world = MinecraftServer.getServer().worldServerForDimension(0);
            BatDroppingsHandler.forStillBats().onServerTick(world);
            BatDroppingsHandler.forNormalBats().onServerTick(world);
        }
    }
}
