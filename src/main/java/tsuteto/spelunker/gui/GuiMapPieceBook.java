package tsuteto.spelunker.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import tsuteto.spelunker.world.levelmapper.MapPieces;

public class GuiMapPieceBook extends GuiScreen
{
    private static final ResourceLocation bookGuiTextures = new ResourceLocation("spelunker", "textures/gui/guiMapPieceGuide.png");
    private static final int COLS = 8;
    private static final int COL_HEIGHT = 15;

    private boolean field_146481_r;
    private boolean field_146480_s;
    private int bookImageWidth = 256;
    private int bookImageHeight = 192;
    private int bookTotalPages = 1;
    private int currPage = 0;
    private NextPageButton buttonNextPage;
    private NextPageButton buttonPreviousPage;
    private GuiButton buttonDone;

    public GuiMapPieceBook(EntityPlayer p_i1080_1_, ItemStack p_i1080_2_)
    {
        this.bookTotalPages = MapPieces.mapPieceList.size() / COLS + 1;
    }

    public void initGui()
    {
        this.buttonList.clear();

        this.buttonList.add(this.buttonDone = new GuiButton(0, this.width / 2 - 100, 4 + this.bookImageHeight, 200, 20, I18n.format("gui.done")));

        int i = (this.width - this.bookImageWidth) / 2;
        byte b0 = 2;
        this.buttonList.add(this.buttonNextPage = new NextPageButton(1, i + 160, b0 + 154, true));
        this.buttonList.add(this.buttonPreviousPage = new NextPageButton(2, i + 75, b0 + 154, false));
        this.updateButtons();
    }

    private void updateButtons()
    {
        this.buttonNextPage.visible = !this.field_146480_s && this.currPage < this.bookTotalPages - 1;
        this.buttonPreviousPage.visible = !this.field_146480_s && this.currPage > 0;
        this.buttonDone.visible = !this.field_146480_s;
    }

    protected void actionPerformed(GuiButton p_146284_1_)
    {
        if (p_146284_1_.enabled)
        {
            if (p_146284_1_.id == 0)
            {
                this.mc.displayGuiScreen(null);
            }
            else if (p_146284_1_.id == 1)
            {
                if (this.currPage < this.bookTotalPages - 1)
                {
                    ++this.currPage;
                }
            }
            else if (p_146284_1_.id == 2)
            {
                if (this.currPage > 0)
                {
                    --this.currPage;
                }
            }
            else if (p_146284_1_.id == 5 && this.field_146480_s)
            {
                this.mc.displayGuiScreen(null);
            }
            else if (p_146284_1_.id == 4 && this.field_146480_s)
            {
                this.field_146480_s = false;
            }

            this.updateButtons();
        }
    }

    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(bookGuiTextures);
        int k = (this.width - this.bookImageWidth) / 2;
        byte b0 = 2;
        this.drawTexturedModalRect(k, b0, 0, 0, this.bookImageWidth, this.bookImageHeight);
        String s;
        int l;

        // Subject
        s = I18n.format("Spelunker.mapPiece.subject");
        l = this.fontRendererObj.getStringWidth(s);
        this.fontRendererObj.drawString(s, k - l / 2 + this.bookImageWidth / 2, b0 + 16, 0x000000);

        // Page indicator
        s = I18n.format("book.pageIndicator", this.currPage + 1, this.bookTotalPages);
        l = this.fontRendererObj.getStringWidth(s);
        this.fontRendererObj.drawString(s, k - l / 2 + this.bookImageWidth / 2, b0 + 154, 0x000000);

        // Page contents
        boolean originalUnicodeFlag = this.fontRendererObj.getUnicodeFlag();
        for (int i = this.currPage * COLS; i < (this.currPage + 1) * COLS && i < MapPieces.mapPieceList.size(); i++)
        {
            MapPieces.MapPieceEntry entry = MapPieces.mapPieceList.get(i);
            int colX = k + 20;
            int colY = b0 + 32 + i % COLS * COL_HEIGHT;
            // Color
            drawRect(colX, colY, colX + 20, colY + 6, 0xff000000);
            drawRect(colX + 1, colY + 1, colX + 20 - 1, colY + 6 - 1, entry.color | 0xff000000);
            // Color code
            this.fontRendererObj.setUnicodeFlag(false);
            GL11.glPushMatrix();
            GL11.glScalef(0.5F, 0.5F, 1.0F);
            this.fontRendererObj.drawString(String.format("#%06X", entry.color & 0xffffff), colX * 2, (colY + 7) * 2, 0x000000);
            GL11.glPopMatrix();
            this.fontRendererObj.setUnicodeFlag(originalUnicodeFlag);
            // Description
            this.fontRendererObj.drawString(entry.mapPiece.getLocalizedName(entry.color), colX + 24, colY + 1, 0x000000);
        }

        this.fontRendererObj.setUnicodeFlag(originalUnicodeFlag);

        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    @SideOnly(Side.CLIENT)
    static class NextPageButton extends GuiButton
    {
        private final boolean field_146151_o;

        public NextPageButton(int p_i1079_1_, int p_i1079_2_, int p_i1079_3_, boolean p_i1079_4_)
        {
            super(p_i1079_1_, p_i1079_2_, p_i1079_3_, 23, 13, "");
            this.field_146151_o = p_i1079_4_;
        }

        /**
         * Draws this button to the screen.
         */
        public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_)
        {
            if (this.visible)
            {
                boolean flag = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                p_146112_1_.getTextureManager().bindTexture(bookGuiTextures);
                int k = 0;
                int l = 192;

                if (flag)
                {
                    k += 23;
                }

                if (!this.field_146151_o)
                {
                    l += 13;
                }

                this.drawTexturedModalRect(this.xPosition, this.yPosition, k, l, 23, 13);
            }
        }
    }
}
