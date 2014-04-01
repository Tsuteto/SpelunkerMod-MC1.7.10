package tsuteto.spelunker.eventhandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;
import tsuteto.spelunker.SpelunkerMod;

/**
 * Handles events on the world
 *
 * @author Tsuteto
 *
 */
public class WorldEventHandler
{
    private SpelunkerMod mod;

    public WorldEventHandler(SpelunkerMod mod)
    {
        this.mod = mod;
    }

    @SubscribeEvent
    public void onWorldEvent(WorldEvent.Load event)
    {
    }
}
