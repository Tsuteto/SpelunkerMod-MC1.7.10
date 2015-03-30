package tsuteto.spelunker.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import tsuteto.spelunker.block.tileentity.ContainerMapSelectorBase;
import tsuteto.spelunker.levelmap.MapSource;
import tsuteto.spelunker.levelmap.SpelunkerMapInfo;

abstract public class GuiMapSelectorBase<T extends ContainerMapSelectorBase> extends GuiScreen
{
    public T inventorySlots;
    private GuiMapSelectorBase.List mapList;

    public GuiMapSelectorBase(T inventorySlots)
    {
        this.inventorySlots = inventorySlots;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.mc.thePlayer.openContainer = this.inventorySlots;
        this.mapList = new GuiMapSelectorBase.List();
        this.mapList.registerScrollButtons(7, 8);
    }

    protected void actionPerformed(GuiButton p_146284_1_)
    {
        if (p_146284_1_.enabled)
        {
            switch (p_146284_1_.id)
            {
                case 5:
                    break;
                case 6:
                    // Clicked done
                    this.onDoneClicked();
                    break;
                case 100:
                    // Clicked cancel
                    this.mc.displayGuiScreen(null);
                    break;
                default:
                    this.mapList.actionPerformed(p_146284_1_);
            }
        }
    }

    abstract protected void onDoneClicked();

    abstract protected void sendSelectedLevelInfo(final int rowSelected);

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.mapList.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
        this.drawCenteredString(this.fontRendererObj, I18n.format("Spelunker.mapSelect"), this.width / 2, 16, 16777215);
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    public boolean doesGuiPauseGame()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    class List extends GuiSlot
    {
        public List()
        {
            super(GuiMapSelectorBase.this.mc, GuiMapSelectorBase.this.width, GuiMapSelectorBase.this.height, 32, GuiMapSelectorBase.this.height - 65 + 4, 18);
        }

        protected int getSize()
        {
            java.util.List mapList = GuiMapSelectorBase.this.inventorySlots.mapList;
            return mapList == null ? 0 : mapList.size();
        }

        /**
         * The element in the slot that was clicked, boolean for whether it was double clicked or not
         */
        protected void elementClicked(int p_148144_1_, boolean p_148144_2_, int p_148144_3_, int p_148144_4_)
        {
            GuiMapSelectorBase.this.inventorySlots.mapRowSelected = p_148144_1_;
        }

        /**
         * Returns true if the element passed in is currently selected
         */
        protected boolean isSelected(int p_148131_1_)
        {
            return p_148131_1_ == GuiMapSelectorBase.this.inventorySlots.mapRowSelected;
        }

        /**
         * Return the height of the content being scrolled
         */
        protected int getContentHeight()
        {
            return this.getSize() * 18;
        }

        protected void drawBackground()
        {
            GuiMapSelectorBase.this.drawDefaultBackground();
        }

        protected void drawSlot(int p_148126_1_, int p_148126_2_, int p_148126_3_, int p_148126_4_, Tessellator p_148126_5_, int p_148126_6_, int p_148126_7_)
        {
            java.util.List<SpelunkerMapInfo> mapList = GuiMapSelectorBase.this.inventorySlots.mapList;
            if (mapList != null)
            {
                SpelunkerMapInfo info = mapList.get(p_148126_1_);
                int color = info.source == MapSource.USER ? 0xffffff : 0xccccff;
                GuiMapSelectorBase.this.drawCenteredString(GuiMapSelectorBase.this.fontRendererObj, info.mapName, this.width / 2, p_148126_3_ + 1, color);
            }
        }
    }

}
