package tsuteto.spelunker.eventhandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.util.ModLog;

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
            ModLog.info("Saving Spelunker world info");
            SpelunkerMod.saveWorldInfo();
        }
    }
}
