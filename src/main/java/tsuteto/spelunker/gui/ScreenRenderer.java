package tsuteto.spelunker.gui;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.constants.SpelunkerGameMode;
import tsuteto.spelunker.player.SpelunkerPlayerSP;

/**
 * Renders Spelunker status on the screen
 *
 * @author Tsuteto
 *
 */
public class ScreenRenderer
{
    private static final ResourceLocation texture = new ResourceLocation("spelunker", "textures/icons.png");

    private int airPointerPos = -1;

    @SubscribeEvent
    public void renderGameOverlay(RenderGameOverlayEvent.Text event)
    {
        Minecraft mc = FMLClientHandler.instance().getClient();
        EntityPlayerSP player = mc.thePlayer;
        SpelunkerPlayerSP spelunker = SpelunkerMod.getSpelunkerPlayer(player);
        long worldtime = mc.theWorld.getWorldTime();

        if (!spelunker.isReady) return;

        ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int guiW = scaledresolution.getScaledWidth();
        int guiH = scaledresolution.getScaledHeight();
        FontRenderer fontrenderer = mc.fontRenderer;
        Gui gui = new Gui();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int ox = 0;
        int oy = 0;
        if (!player.capabilities.isCreativeMode)
        {
            GL11.glEnable(GL11.GL_BLEND);

            mc.getTextureManager().bindTexture(texture);

            if (SpelunkerMod.settings().displayScore || spelunker.isHardcore())
            {
                oy += 11;
            }

            // Hardcore mode sign
            if (spelunker.isHardcore())
            {
                gui.drawTexturedModalRect(2, guiH - oy, 79, 8, 11, 9);
                ox += 12;
            }

            if (SpelunkerMod.settings().displayScore)
            {
                // Score
                ScreenRenderHelper.renderFamicomDigit(spelunker.spelunkerScore.getDisplayScore(), gui, scaledresolution, ox + 2, oy);
            }

            if (SpelunkerMod.settings().displayDeaths && !mc.theWorld.getWorldInfo().isHardcoreModeEnabled())
            {
                ox = 0;

                if (spelunker.gameMode == SpelunkerGameMode.Arcade)
                {
                    // Life
                    oy += 11;
                    gui.drawTexturedModalRect(2, guiH - oy, 38, 8, 20, 9);
                    if (spelunker.livesLeft >= 0)
                    {
                        int l = spelunker.livesLeft;
                        int width = ScreenRenderHelper.renderFamicomDigit(l, gui, scaledresolution, 24, oy);
                        ox += 28 + width;
                    }
                    else
                    {
                        ox += 28 + 8;
                    }
                }
                else
                {
                    // DEATHS
                    oy += 13;
                    gui.drawTexturedModalRect(2, guiH - oy, 26, 8, 12, 11);
                    int width = ScreenRenderHelper.renderFamicomDigit(spelunker.deaths, gui, scaledresolution, 15, oy - 2);
                    ox += 20 + width;
                }
            }
            else
            {
                oy += 11;
            }

            mc.getTextureManager().bindTexture(texture);
            // 2x
            if (spelunker.is2xScore())
            {
                gui.drawTexturedModalRect(ox, guiH - oy + 1, 58, 8, 10, 10);
                ox += 12;
            }
            // Invincible
            if (spelunker.isInvincible())
            {
                gui.drawTexturedModalRect(ox, guiH - oy + 1, 68, 8, 11, 10);
                ox += 12;
            }
            // Ghost attacking
            if (spelunker.isGhostComing())
            {
                gui.drawTexturedModalRect(ox, guiH - oy + 1 - 8, 90, 0, 17, 18);
                ox += 12;
            }

        }

        // Energy indicator
        if ((spelunker.isHardcore() || spelunker.isUsingEnergy()) && !player.capabilities.disableDamage && spelunker.getMaxEnergy() > 0)
        {
            GL11.glEnable(GL11.GL_BLEND);
            mc.getTextureManager().bindTexture(texture);
            int j7 = guiW / 2 + 91;
            int k8 = guiH - 39;
            int j9 = k8 - 10;

            int airPointer = spelunker.energy / (spelunker.getMaxEnergy() / 44);
            if (airPointerPos != -1 && airPointerPos < airPointer)
            {
                if (airPointerPos < airPointer - 1)
                {
                    airPointerPos += 2;
                }
                else
                {
                    airPointerPos += 1;
                }
            }
            else
            {
                airPointerPos = airPointer;
            }

            // MIN
            gui.drawTexturedModalRect(j7 - 81, j9, 0, 0, 14, 8);
            // MAX
            gui.drawTexturedModalRect(j7 - 17, j9, 24, 0, 17, 8);
            // Pointer
            if (spelunker.isUsingEnergy())
            {
                if (spelunker.energy < 600 && worldtime % 20 >= 10)
                {
                    GL11.glColor3f(1.0F, 0.0F, 0.04705F);
                }
                else
                {
                    GL11.glColor3f(0.63137F, 0.38039F, 1.0F);
                }
            }
            else
            {
                GL11.glColor3f(0.6F, 0.6F, 0.6F);
            }
            gui.drawTexturedModalRect(j7 + airPointerPos - 68, j9, 15, 0, 8, 8);
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            // ENERGY UP!
            if (spelunker.timeLvlupIndicator > -1)
            {
                int x = j9 - 16 - (spelunker.timeLvlupIndicator / 2) % 5;
                gui.drawTexturedModalRect(j7 - 21, x, 0, 8, 26, 13);
            }
            // fontrenderer.drawStringWithShadow(String.valueOf(spelunker.getEnergy()),
            // j7 - 81, j9 - 10, Color.white.getRGB());
        }
        GL11.glDisable(GL11.GL_BLEND);
    }
}
