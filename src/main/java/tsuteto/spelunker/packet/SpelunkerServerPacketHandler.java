package tsuteto.spelunker.packet;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class SpelunkerServerPacketHandler extends SpelunkerCommonPacketHandler implements MessageToServer
{
    private ServerPacketHandler serverHandler = new ServerPacketHandler();

    @Override
    public IMessage handleServerSide(EntityPlayer player)
    {
        serverHandler.onPacketData(receivedData, player);
        return null;
    }
}
