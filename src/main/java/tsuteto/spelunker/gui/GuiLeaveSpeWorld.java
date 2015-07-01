package tsuteto.spelunker.gui;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import tsuteto.spelunker.block.tileentity.ContainerSpelunkerPortal;
import tsuteto.spelunker.block.tileentity.TileEntitySpelunkerPortalStage;
import tsuteto.spelunker.network.PacketDispatcher;
import tsuteto.spelunker.network.packet.PacketGuiControl;

public class GuiLeaveSpeWorld extends GuiScreen implements GuiYesNoCallback
{
    public Container inventorySlots;

    public GuiLeaveSpeWorld(InventoryPlayer invPlayer, TileEntitySpelunkerPortalStage spelunkerPortal)
    {
        this.inventorySlots = new ContainerSpelunkerPortal(invPlayer, spelunkerPortal);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.mc.thePlayer.openContainer = this.inventorySlots;
        GuiYesNo yesNoScreen = buildConfirmationScreen();
        mc.displayGuiScreen(yesNoScreen);
    }

    public GuiYesNo buildConfirmationScreen()
    {
        String s1 = I18n.format("Spelunker.leaveSpeWorld.question");
        String s2 = I18n.format("Spelunker.leaveSpeWorld.warning");
        String s3 = I18n.format("Spelunker.leaveSpeWorld.leave");
        String s4 = I18n.format("gui.cancel");
        GuiYesNo guiyesno = new GuiYesNo(this, s1, s2, s3, s4, 0);
        return guiyesno;
    }

    @Override
    public void confirmClicked(boolean p_73878_1_, int rowSelected)
    {
        if (p_73878_1_)
        {
            this.sendSelectedLevelInfo();
        }
        this.mc.displayGuiScreen(null);
    }

    protected void sendSelectedLevelInfo()
    {
        EntityClientPlayerMP player = this.mc.thePlayer;
        PacketDispatcher.packet(new PacketGuiControl(player.openContainer.windowId, 1, null)).sendToServer();
    }
}
