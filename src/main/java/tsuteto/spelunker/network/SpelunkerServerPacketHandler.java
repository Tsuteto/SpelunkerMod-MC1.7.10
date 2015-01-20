package tsuteto.spelunker.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import net.minecraft.entity.player.EntityPlayer;
import tsuteto.spelunker.network.packet.ServerPacketHandler;

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
