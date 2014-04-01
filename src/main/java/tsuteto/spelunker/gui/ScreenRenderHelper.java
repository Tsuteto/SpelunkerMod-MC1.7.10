package tsuteto.spelunker.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class ScreenRenderHelper
{
    public static int renderFamicomDigit(int val, Gui gui, ScaledResolution scaledresolution, int x, int y)
    {
        int guiW = scaledresolution.getScaledWidth();
        int guiH = scaledresolution.getScaledHeight();
        int offsetX = 0;
        char[] c = Integer.toString(val).toCharArray();

        for (int i = 0; i < c.length; i++)
        {
            int d = c[i] - '0';
            gui.drawTexturedModalRect(x + offsetX, guiH - y, d * 9, 21, 9, 9);
            offsetX += 8;
        }
        return offsetX;
    }

}
