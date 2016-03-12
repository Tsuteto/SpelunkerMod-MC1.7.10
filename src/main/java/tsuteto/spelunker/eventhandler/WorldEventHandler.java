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
    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event)
    {
        if (event.world.provider.dimensionId == 0)
        {
            SpelunkerMod.saveWorldInfo();
        }
    }
}
