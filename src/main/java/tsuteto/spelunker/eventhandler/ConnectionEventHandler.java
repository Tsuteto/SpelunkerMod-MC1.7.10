package tsuteto.spelunker.eventhandler;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.dimension.SpelunkerLevelManagerClient;
import tsuteto.spelunker.network.packet.PacketDimRegistration;

public class ConnectionEventHandler
{
    private boolean connectedToMultiServer = false;

    @SubscribeEvent
    public void onConnectionReceived(FMLNetworkEvent.ServerConnectionFromClientEvent event)
    {
        SpelunkerMod.levelManager().syncAllLevels(event.manager);
    }

    @SubscribeEvent
    public void connectionOpened(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        connectedToMultiServer = !FMLClientHandler.instance().getClient().isSingleplayer();
    }

    @SubscribeEvent
    public void onConnectionClosed(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
        if (connectedToMultiServer)
        {
            SpelunkerLevelManagerClient.unregisterAll();
            connectedToMultiServer = false;
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPacketData(FMLNetworkEvent.ClientCustomPacketEvent event)
    {
        onPacketData(event.manager, event.packet, FMLClientHandler.instance().getClient().thePlayer);
    }

    public void onPacketData(NetworkManager manager, FMLProxyPacket packet, EntityPlayer player)
    {
        ByteBuf data = packet.payload();
        PacketDimRegistration handler = new PacketDimRegistration();
        handler.decodeInto(data);
        handler.handleClientSide(player);
    }

}
