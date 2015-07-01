package tsuteto.spelunker.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TitleController
{
    private static final TitleController instance = new TitleController();

    public int titleTick = 0;
    public String title = null;
    public String[] subtitles = new String[0];

    private TitleController() {}

    public static TitleController instance()
    {
        return instance;
    }

    public void setTitle(String title, String... subtitles)
    {
        this.title = title;
        this.subtitles = subtitles;
        titleTick = 200;
    }

    public void onGameTick()
    {
        if (titleTick > 0)
        {
            titleTick--;
        }
    }

    public void renderTitle(Minecraft mc)
    {
        if (titleTick > 0)
        {
            ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
            int guiW = scaledresolution.getScaledWidth();
            int guiH = scaledresolution.getScaledHeight();
            FontRenderer fontrenderer = mc.fontRenderer;
            Gui gui = new Gui();

            GL11.glPushMatrix();
            GL11.glScalef(3.0F, 3.0F, 1.0F);

            int alpha = (titleTick < 16 ? 0x10 * titleTick : 0xff) << 24;
            int width = fontrenderer.getStringWidth(title);
            fontrenderer.drawStringWithShadow(title, guiW / 6 - width / 2, 25, 0xffffff | alpha);

            GL11.glPopMatrix();

            int subLines = subtitles.length;
            int subX = 150 - subLines * 10 / 2;
            for (int l = 0; l < subLines; l++)
            {
                width = fontrenderer.getStringWidth(subtitles[l]);
                fontrenderer.drawStringWithShadow(subtitles[l],
                        guiW / 2 - width / 2, subX + 15 * l, 0xffffff | alpha);
            }

        }
    }

    public void clear()
    {
        this.titleTick = 0;
        this.title = null;
        this.subtitles = new String[0];
    }
}
