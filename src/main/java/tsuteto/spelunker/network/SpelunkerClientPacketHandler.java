package tsuteto.spelunker.network;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import net.minecraft.entity.player.EntityPlayer;
import tsuteto.spelunker.network.packet.CommonClientPacketHandler;

public class SpelunkerClientPacketHandler extends SpelunkerCommonPacketHandler implements MessageToClient
{
    @SidedProxy(serverSide = "tsuteto.spelunker.network.packet.CommonClientPacketHandler", clientSide = "tsuteto.spelunker.network.packet.ClientPacketHandler")
    private static CommonClientPacketHandler clientHandler;

    @Override
    public IMessage handleClientSide(EntityPlayer player)
    {
        clientHandler.onPacketData(receivedData, player);
        return null;
    }
}
