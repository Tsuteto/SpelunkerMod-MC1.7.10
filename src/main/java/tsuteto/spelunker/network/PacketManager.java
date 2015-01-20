package tsuteto.spelunker.network;

import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import tsuteto.spelunker.util.ModLog;

public class PacketManager
{
    public static String CP_CHANNEL_NAME;
    private static SimpleNetworkWrapper networkHandler = null;
    private static FMLEventChannel cpChannel = null;
    private int id = 0;

    public static PacketManager init(String modId)
    {
        CP_CHANNEL_NAME = modId + "_cp";

        networkHandler = NetworkRegistry.INSTANCE.newSimpleChannel(modId);
        cpChannel = NetworkRegistry.INSTANCE.newEventDrivenChannel(CP_CHANNEL_NAME);
        return new PacketManager();
    }

    public static SimpleNetworkWrapper getNetworkHandler()
    {
        return networkHandler;
    }

    private PacketManager() {}

    @SuppressWarnings("unchecked")
    public PacketManager registerPacket(Class<? extends AbstractPacket> packetClass)
    {
        Class<AbstractPacket> message = (Class<AbstractPacket>)packetClass;
        if (MessageToServer.class.isAssignableFrom(packetClass))
        {
            networkHandler.registerMessage(packetClass, message, id, Side.SERVER);
            ModLog.debug("Registered Packet: %s at ID %d", packetClass.getName(), id);
            id++;
        }

        if (MessageToClient.class.isAssignableFrom(packetClass))
        {
            networkHandler.registerMessage(packetClass, message, id, Side.CLIENT);
            ModLog.debug("Registered Packet: %s at ID %d", packetClass.getName(), id);
            id++;
        }
        return this;
    }

    public PacketManager registerEventHandler(Object handler)
    {
        cpChannel.register(handler);
        return this;
    }
}
