package tsuteto.spelunker.gui;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import tsuteto.spelunker.constants.SpelunkerGameMode;
import tsuteto.spelunker.player.SpelunkerPlayerSP;

/**
 * Renders Spelunker status on the screen
 *
 * @author Tsuteto
 *
 */
public class ScreenRendererGameover
{
    public void renderGameOverlay(Minecraft mc, EntityPlayerSP player, SpelunkerPlayerSP spelunker, long worldtime)
    {
        if (!spelunker.isReady) return;

        // GAME OVER
        if (mc.currentScreen instanceof GuiGameOver)
        {
            ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            int guiW = scaledresolution.getScaledWidth();
            int guiH = scaledresolution.getScaledHeight();
            FontRenderer fontrenderer = mc.fontRenderer;
            Gui gui = new Gui();

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            String color;
            if (spelunker.gameMode == SpelunkerGameMode.Adventure
                    && spelunker.spelunkerScore.scoreActual == spelunker.spelunkerScore.hiscore
                    && spelunker.spelunkerScore.scoreActual != 0 || spelunker.gameMode == SpelunkerGameMode.Arcade
                    && spelunker.livesLeft < 0)
            {
                color = "§" + (char) ('a' + mc.theWorld.getWorldTime() % 6);
            }
            else
            {
                color = "§f";
            }

            String title = "< SPELUNKER RESULTS >";
            int width = fontrenderer.getStringWidth(title);
            fontrenderer.drawStringWithShadow(title, guiW / 2 - width / 2, 5, Color.white.getRGB());

            int widthFull = fontrenderer.getStringWidth("YOUR SCORE  88888888");

            // SCORE
            fontrenderer.drawStringWithShadow("YOUR SCORE", guiW / 2 - widthFull / 2, 20, Color.white.getRGB());
            int widthDigit = fontrenderer.getStringWidth(String.valueOf(spelunker.spelunkerScore.scoreActual));
            fontrenderer.drawStringWithShadow(color + String.valueOf(spelunker.spelunkerScore.scoreActual), guiW / 2
                    + widthFull / 2 - widthDigit, 20, Color.white.getRGB());

            // HIGH SCORE
            if (spelunker.gameMode == SpelunkerGameMode.Adventure)
            {
                fontrenderer.drawStringWithShadow("HIGH SCORE", guiW / 2 - widthFull / 2, 35, Color.white.getRGB());
                widthDigit = fontrenderer.getStringWidth(String.valueOf(spelunker.spelunkerScore.hiscore));
                fontrenderer.drawStringWithShadow(color + String.valueOf(spelunker.spelunkerScore.hiscore), guiW / 2
                        + widthFull / 2 - widthDigit, 35, Color.white.getRGB());
            }
        }
    }
}
