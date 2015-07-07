package tsuteto.spelunker.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;
import tsuteto.spelunker.constants.SpelunkerPacketType;
import tsuteto.spelunker.network.SpelunkerPacketDispatcher;

import java.util.Iterator;

@SideOnly(Side.CLIENT)
public class GuiSpeGameOver extends GuiScreen
{
    private int field_146347_a;
    private boolean field_146346_f = false;

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.buttonList.clear();

        if (this.mc.isIntegratedServerRunning())
        {
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.deleteWorld")));
        }
        else
        {
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.leaveServer")));
        }

        GuiButton guibutton;

        for (Iterator iterator = this.buttonList.iterator(); iterator.hasNext(); guibutton.enabled = false)
        {
            guibutton = (GuiButton)iterator.next();
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char p_73869_1_, int p_73869_2_) {}

    protected void actionPerformed(GuiButton p_146284_1_)
    {
        // send packet instead of this.mc.thePlayer.respawnPlayer();
        SpelunkerPacketDispatcher.of(SpelunkerPacketType.GAMEOVER).sendPacketToServer();
        this.mc.displayGuiScreen(null);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);

        GL11.glPushMatrix();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        String s = I18n.format("deathScreen.title.hardcore");
        this.drawCenteredString(this.fontRendererObj, s, this.width / 2 / 2, 30, 16777215);
        GL11.glPopMatrix();

        this.drawCenteredString(this.fontRendererObj, I18n.format("Spelunker.deathScreen.info"), this.width / 2, 144, 16777215);
        this.drawCenteredString(this.fontRendererObj, I18n.format("deathScreen.score") + ": " + EnumChatFormatting.YELLOW + this.mc.thePlayer.getScore(), this.width / 2, 100, 16777215);
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        ++this.field_146347_a;
        GuiButton guibutton;

        if (this.field_146347_a == 20)
        {
            for (Iterator iterator = this.buttonList.iterator(); iterator.hasNext(); guibutton.enabled = true)
            {
                guibutton = (GuiButton)iterator.next();
            }
        }
    }
}