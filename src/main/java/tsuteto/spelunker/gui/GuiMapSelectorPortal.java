package tsuteto.spelunker.gui;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import tsuteto.spelunker.block.tileentity.ContainerSpelunkerPortal;
import tsuteto.spelunker.block.tileentity.TileEntitySpelunkerPortalStage;
import tsuteto.spelunker.network.PacketDispatcher;
import tsuteto.spelunker.network.packet.PacketGuiControl;

public class GuiMapSelectorPortal extends GuiMapSelectorBase<ContainerSpelunkerPortal> implements GuiYesNoCallback
{
    private TileEntitySpelunkerPortalStage spelunkerPortal;
    private boolean isRegeneration = false;

    public GuiMapSelectorPortal(InventoryPlayer invPlayer, TileEntitySpelunkerPortalStage spelunkerPortal)
    {
        super(new ContainerSpelunkerPortal(invPlayer, spelunkerPortal));
        this.spelunkerPortal = spelunkerPortal;
        this.isRegeneration = spelunkerPortal.levelInfo != null;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.add(new GuiOptionButton(6, this.width / 2 - 155 + 160, this.height - 38,
                this.isRegeneration ? I18n.format("Spelunker.mapSelect.switchMap") : I18n.format("gui.done")));
        this.buttonList.add(new GuiOptionButton(100, this.width / 2 - 155, this.height - 38, I18n.format("gui.cancel")));
    }

    protected void sendSelectedLevelInfo(final int rowSelected)
    {
        EntityClientPlayerMP player = this.mc.thePlayer;
        PacketDispatcher.packet(new PacketGuiControl(player.openContainer.windowId, 0, new PacketGuiControl.DataHandler()
        {
            @Override
            public void addData(ByteBuf buffer)
            {
                buffer.writeBoolean(isRegeneration);
                buffer.writeShort(rowSelected);
            }
        })).sendToServer();
    }

    @Override
    protected void onDoneClicked()
    {
        if (isRegeneration)
        {
            GuiYesNo yesNoScreen = buildConfirmationScreen(this, this.spelunkerPortal.levelInfo.levelName, this.inventorySlots.mapRowSelected);
            mc.displayGuiScreen(yesNoScreen);
        }
        else
        {
            this.sendSelectedLevelInfo(this.inventorySlots.mapRowSelected);
            this.mc.displayGuiScreen(null);
        }
    }

    public static GuiYesNo buildConfirmationScreen(GuiYesNoCallback p_152129_0_, String p_152129_1_, int p_152129_2_)
    {
        String s1 = I18n.format("Spelunker.mapSelect.switchQuestion", p_152129_1_);
        String s2 = I18n.format("Spelunker.mapSelect.switchWarning");
        String s3 = I18n.format("Spelunker.mapSelect.switchMap");
        String s4 = I18n.format("gui.cancel");
        GuiYesNo guiyesno = new GuiYesNo(p_152129_0_, s1, s2, s3, s4, p_152129_2_);
        return guiyesno;
    }

    @Override
    public void confirmClicked(boolean p_73878_1_, int rowSelected)
    {
        if (p_73878_1_)
        {
            this.sendSelectedLevelInfo(rowSelected);
            this.mc.displayGuiScreen(null);
        }
        else
        {
            this.mc.displayGuiScreen(this);
        }
    }
}
