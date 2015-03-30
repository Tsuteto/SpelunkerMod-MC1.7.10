package tsuteto.spelunker.network.packet;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import tsuteto.spelunker.block.tileentity.IGuiRemoteControl;
import tsuteto.spelunker.network.AbstractPacket;
import tsuteto.spelunker.network.MessageToClient;

public class PacketContainerData extends AbstractPacket implements MessageToClient
{
    private int windowId;
    private int dataId;
    private DataHandler dataHandler;
    private ByteBuf buffer;

    public PacketContainerData() {}

    public PacketContainerData(int windowId, int dataId, DataHandler dataHandler)
    {
        this.windowId = windowId;
        this.dataId = dataId;

        this.dataHandler = dataHandler;
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        buffer.writeByte(windowId);
        buffer.writeShort(dataId);
        dataHandler.addData(buffer);
    }

    @Override
    public void decodeInto(ByteBuf buffer)
    {
        this.windowId = buffer.readByte();
        this.dataId = buffer.readShort();
        this.buffer = buffer;
   }

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage handleClientSide(EntityPlayer player)
    {
        Minecraft mc = FMLClientHandler.instance().getClient();
        EntityClientPlayerMP entityclientplayermp = mc.thePlayer;

        if (entityclientplayermp.openContainer != null && entityclientplayermp.openContainer.windowId == windowId)
        {
            ((IGuiRemoteControl)entityclientplayermp.openContainer).updateRemoteData(dataId, buffer);
        }
        return null;
    }

    public static interface DataHandler
    {
        void addData(ByteBuf buffer);
    }
}
