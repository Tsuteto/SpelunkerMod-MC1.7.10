package tsuteto.spelunker.gui;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import tsuteto.spelunker.block.tileentity.ContainerLevelBuilder;
import tsuteto.spelunker.network.PacketDispatcher;
import tsuteto.spelunker.network.packet.PacketGuiControl;

public class GuiMapSelectorBuilder extends GuiMapSelectorBase<ContainerLevelBuilder>
{
    public GuiMapSelectorBuilder(InventoryPlayer invPlayer, int x, int y, int z)
    {
        super(new ContainerLevelBuilder(invPlayer, x, y, z));
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.add(new GuiOptionButton(6, this.width / 2 - 155 + 160, this.height - 38, I18n.format("gui.done")));
        this.buttonList.add(new GuiOptionButton(100, this.width / 2 - 155, this.height - 38, I18n.format("gui.cancel")));
    }

    @Override
    protected void onDoneClicked()
    {
        this.sendSelectedLevelInfo(this.inventorySlots.mapRowSelected);
        this.mc.displayGuiScreen(null);
    }

    @Override
    protected void sendSelectedLevelInfo(final int rowSelected)
    {
        EntityClientPlayerMP player = this.mc.thePlayer;
        PacketDispatcher.packet(new PacketGuiControl(player.openContainer.windowId, 0, new PacketGuiControl.DataHandler()
        {
            @Override
            public void addData(ByteBuf buffer)
            {
                buffer.writeShort(rowSelected);
            }
        })).sendToServer();
    }
}
