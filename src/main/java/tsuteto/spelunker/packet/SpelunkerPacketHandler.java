package tsuteto.spelunker.packet;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public class SpelunkerPacketHandler extends AbstractPacket
{
    @SidedProxy(serverSide = "tsuteto.spelunker.packet.CommonClientPacketHandler", clientSide = "tsuteto.spelunker.packet.ClientPacketHandler")
    private static CommonClientPacketHandler clientHandler;

    private ServerPacketHandler serverHandler = new ServerPacketHandler();

    private byte[] sendData;
    private ByteBuf receivedData;

    public void setSendData(byte[] bytes)
    {
       this.sendData = bytes;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeBytes(sendData);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        this.receivedData = buffer;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handleClientSide(EntityPlayer player)
    {
        clientHandler.onPacketData(receivedData, player);
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        serverHandler.onPacketData(receivedData, player);
    }
}
