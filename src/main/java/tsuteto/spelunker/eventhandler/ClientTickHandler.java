package tsuteto.spelunker.eventhandler;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.gui.TitleController;
import tsuteto.spelunker.player.SpelunkerPlayerSP;
import tsuteto.spelunker.sound.ModSound;

/**
 * Handles game ticks on client side
 *
 * @author Tsuteto
 *
 */
public class ClientTickHandler
{
    private SpelunkerMod mod;

    public ClientTickHandler(SpelunkerMod mod)
    {
        this.mod = mod;
    }

    @SubscribeEvent
    public void tickEnd(TickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            Minecraft mc = FMLClientHandler.instance().getClient();
            if (mc.thePlayer == null) return;

            SpelunkerPlayerSP spelunker = SpelunkerMod.getSpelunkerPlayer(mc.thePlayer);
            if (spelunker == null) return;

            long worldtime = mc.theWorld.getTotalWorldTime();

            if (event.type == TickEvent.Type.RENDER)
            {
                mod.gameScreenRenderer().renderGameOverlay(mc, mc.thePlayer, spelunker, worldtime);
            }
            else if (event.type == TickEvent.Type.CLIENT)
            {
                if (!ModSound.isReady())
                {
                    ModSound.init();
                }
                else
                {
                    ModSound.updateBgmNowPlaying();
                }
                ItemSpawnHandler.onClientTick(mc, spelunker, worldtime);
                TitleController.instance().onGameTick();
            }
        }
    }
}
