package tsuteto.spelunker.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import tsuteto.spelunker.dimension.SpelunkerLevelManagerClient;
import tsuteto.spelunker.network.AbstractPacket;
import tsuteto.spelunker.network.MessageToClient;

public class PacketDimRegistration extends AbstractPacket implements MessageToClient
{
    private int[] dimIds;

    public PacketDimRegistration() {}

    public PacketDimRegistration(int... dimId)
    {
        this.dimIds = dimId;
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        buffer.writeShort(dimIds.length);
        for (int dimId : dimIds) buffer.writeInt(dimId);
    }

    @Override
    public void decodeFrom(ByteBuf buffer)
    {
        dimIds = new int[buffer.readShort()];
        for (int i = 0; i < dimIds.length; i++)
        {
            dimIds[i] = buffer.readInt();
        }
    }

    @Override
    public IMessage handleClientSide(EntityPlayer player)
    {
        for (int dimId : dimIds)
        {
            SpelunkerLevelManagerClient.register(dimId);
        }
        return null;
    }
}
