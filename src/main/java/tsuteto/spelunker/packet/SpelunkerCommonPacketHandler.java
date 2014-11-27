package tsuteto.spelunker.packet;

import io.netty.buffer.ByteBuf;

abstract public class SpelunkerCommonPacketHandler extends AbstractPacket
{
    private byte[] sendData;
    protected ByteBuf receivedData;

    public void setSendData(byte[] bytes)
    {
        this.sendData = bytes;
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        buffer.writeBytes(sendData);
    }

    @Override
    public void decodeInto(ByteBuf buffer)
    {
        this.receivedData = buffer;
    }
}
