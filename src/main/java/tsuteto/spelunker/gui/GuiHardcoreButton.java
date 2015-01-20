package tsuteto.spelunker.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiHardcoreButton extends GuiButton
{
    private static final ResourceLocation buttonTexture = new ResourceLocation("spelunker", "textures/icons.png");
    private final int offIconX;
    private final int offIconY;
    private final int onIconX;
    private final int onIconY;

    private boolean isTurnedOn = false;

    public GuiHardcoreButton(int par1, int par2, int par3, int par5, int par6)
    {
        super(par1, par2, par3, 30, 20, "");
        this.offIconX = par5;
        this.offIconY = par6;
        this.onIconX = par5 + 17;
        this.onIconY = par6;
    }

    /**
     * Draws this button to the screen.
     */
    @Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.visible)
        {
            super.drawButton(par1Minecraft, par2, par3);

            par1Minecraft.getTextureManager().bindTexture(buttonTexture);

            if (isTurnedOn)
            {
                this.drawTexturedModalRect(this.xPosition + this.width / 2 - 8, this.yPosition + 2, this.onIconX, this.onIconY, 16, 16);
            }
            else
            {
                this.drawTexturedModalRect(this.xPosition + this.width / 2 - 8, this.yPosition + 2, this.offIconX, this.offIconY, 16, 16);
            }
        }
    }

    public void turnOn()
    {
        this.isTurnedOn = true;
    }

    public void turnOff()
    {
        this.isTurnedOn = false;
    }

    public void toggle()
    {
        this.isTurnedOn ^= true;
    }

    public boolean isTurnedOn()
    {
        return this.isTurnedOn;
    }
}
