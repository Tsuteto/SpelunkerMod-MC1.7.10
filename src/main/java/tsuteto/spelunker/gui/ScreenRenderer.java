package tsuteto.spelunker.gui;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import org.lwjgl.opengl.GL11;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.constants.SpelunkerGameMode;
import tsuteto.spelunker.entity.EntityGhost;
import tsuteto.spelunker.player.SpelunkerPlayerSP;
import tsuteto.spelunker.util.Utils;

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
    public void renderEntitySpecials(RenderLivingEvent.Post event)
    {

        if (event.entity instanceof EntityGhost)
        {
            Minecraft mc = FMLClientHandler.instance().getClient();
            EntityPlayerSP player = mc.thePlayer;

            if (!player.canEntityBeSeen(event.entity))
            {
                Gui gui = new Gui();
                RenderManager renderManager = RenderManager.instance;

                double x = event.x;
                double y = event.y;
                double z = event.z;
                float f = 6.4F;
                float f1 = 0.016666668F * f;

                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0, 0xf0);

                GL11.glPushMatrix();
                GL11.glTranslatef((float) x, (float) y + event.entity.height / 2 + 0.5F, (float) z);
                GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                GL11.glScalef(-f1, -f1, f1);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glTranslatef(0.0F, 0.25F / f1, 0.0F);
                GL11.glDepthMask(false);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                mc.getTextureManager().bindTexture(texture);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
                Tessellator.instance.setBrightness(player.worldObj.getLightBrightnessForSkyBlocks((int) player.posX, (int) player.posY, (int) player.posZ, 15));
                gui.drawTexturedModalRect(-8, -9, 90, 0, 17, 18);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
            }
        }
    }

    @SubscribeEvent
    public void renderGameOverlay(RenderGameOverlayEvent.Text event)
    {
        Minecraft mc = FMLClientHandler.instance().getClient();
        EntityPlayerSP player = mc.thePlayer;
        SpelunkerPlayerSP spelunker = SpelunkerMod.getSpelunkerPlayer(player);
        long worldtime = mc.theWorld.getTotalWorldTime();

        if (!spelunker.isReady) return;

        // TITLE
        TitleController.instance().render(mc);

        ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int guiW = scaledresolution.getScaledWidth();
        int guiH = scaledresolution.getScaledHeight();
        FontRenderer fontrenderer = mc.fontRenderer;
        Gui gui = new Gui();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        // TIME
        if (spelunker.isInSpelunkerWorld() && spelunker.speLevelStartTime != -1)
        {
            long timeTo = spelunker.speLevelFinishTime != -1 ? spelunker.speLevelFinishTime : worldtime;
            String dispTime, dispBest;
            String color;
            if (spelunker.isSpeLevelCleared && spelunker.isBestTime)
            {
                color = "§" + (char) ('a' + mc.theWorld.getTotalWorldTime() % 6);
            }
            else
            {
                color = "";
            }
            if (!spelunker.isSpeLevelCheated)
            {
                dispTime = color + Utils.formatTickToTime((int) (timeTo - spelunker.speLevelStartTime), spelunker.isSpeLevelCleared);
            }
            else
            {
                dispTime = "§8" + I18n.format("Spelunker.cheated");
            }
            dispBest = spelunker.speLevelBestTime != -1 ? color + Utils.formatTickToTime(spelunker.speLevelBestTime, true) : "--:--";

            boolean originalUnicodeFlag = fontrenderer.getUnicodeFlag();
            fontrenderer.setUnicodeFlag(false);
            int widthFull = fontrenderer.getStringWidth("TIME " + I18n.format("Spelunker.cheated"));
            if (SpelunkerMod.isSinglePlayer())
            {
                int widthLegend = fontrenderer.getStringWidth("BEST");
                int widthLegendTime = fontrenderer.getStringWidth("TIME");
                fontrenderer.drawStringWithShadow("TIME", guiW / 2 - widthFull / 2 + widthLegend - widthLegendTime, 20, 0xffffff);
                int widthTime = fontrenderer.getStringWidth(dispTime);
                fontrenderer.drawStringWithShadow(dispTime, guiW / 2 + widthFull / 2 - widthTime, 20, 0xffffff);

                int widthBest = fontrenderer.getStringWidth(dispBest);
                fontrenderer.drawStringWithShadow("BEST", guiW / 2 - widthFull / 2, 8, 0xaaaacc);
                fontrenderer.drawStringWithShadow(dispBest, guiW / 2 + widthFull / 2 - widthBest, 8, 0xaaaacc);
            }
            else
            {
                fontrenderer.drawStringWithShadow("TIME", guiW / 2 - widthFull / 2, 20, 0xffffff);
                int widthTime = fontrenderer.getStringWidth(dispTime);
                fontrenderer.drawStringWithShadow(dispTime, guiW / 2 + widthFull / 2 - widthTime, 20, 0xffffff);
            }
            fontrenderer.setUnicodeFlag(originalUnicodeFlag);

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        // STATUS
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
