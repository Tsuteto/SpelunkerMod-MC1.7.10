package tsuteto.spelunker.packet;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import net.minecraft.entity.player.EntityPlayer;

public class SpelunkerClientPacketHandler extends SpelunkerCommonPacketHandler implements MessageToClient
{
    @SidedProxy(serverSide = "tsuteto.spelunker.packet.CommonClientPacketHandler", clientSide = "tsuteto.spelunker.packet.ClientPacketHandler")
    private static CommonClientPacketHandler clientHandler;

    @Override
    public IMessage handleClientSide(EntityPlayer player)
    {
        clientHandler.onPacketData(receivedData, player);
        return null;
    }
}
