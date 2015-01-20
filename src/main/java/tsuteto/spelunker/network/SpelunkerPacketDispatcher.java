package tsuteto.spelunker.network;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import org.apache.logging.log4j.Level;
import tsuteto.spelunker.constants.SpelunkerPacketType;
import tsuteto.spelunker.util.ModLog;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Simplifies the process to dispatch packets for Spelunker
 *
 * @author Tsuteto
 *
 */
public class SpelunkerPacketDispatcher
{
    protected final SimpleNetworkWrapper networkHandler = PacketManager.getNetworkHandler();

    protected final SpelunkerCommonPacketHandler packet;
    private boolean packed = false;

    private ByteArrayOutputStream bytesStream;
	private DataOutputStream dataStream;

	public SpelunkerPacketDispatcher(SpelunkerPacketType type)
	{
        if (type.messageTo == Side.CLIENT)
        {
            packet = new SpelunkerClientPacketHandler();
        }
        else
        {
            packet = new SpelunkerServerPacketHandler();
        }
        bytesStream = new ByteArrayOutputStream();
        dataStream = new DataOutputStream(bytesStream);

        this.addByte((byte)(type.ordinal() & 0xff));
        //ModLog.debug("Packet: " + type.name());
	}

	public SpelunkerPacketDispatcher addInt(int val)
	{
        try
        {
            dataStream.writeInt(val);
        }
        catch (IOException e)
        {
            ModLog.log(Level.WARN, e, "Failed to set value to a packet.");
        }
        return this;
	}

    public SpelunkerPacketDispatcher addByte(byte val)
    {
        try
        {
            dataStream.writeByte(val);
        }
        catch (IOException e)
        {
            ModLog.log(Level.WARN, e, "Failed to set value to a packet.");
        }
        return this;
    }

    public SpelunkerPacketDispatcher addBool(boolean val)
    {
        try
        {
            dataStream.writeBoolean(val);
        }
        catch (IOException e)
        {
            ModLog.log(Level.WARN, e, "Failed to set value to a packet.");
        }
        return this;
    }
    public SpelunkerPacketDispatcher addShort(short val)
    {
        try
        {
            dataStream.writeShort(val);
        }
        catch (IOException e)
        {
            ModLog.log(Level.WARN, e, "Failed to set value to a packet.");
        }
        return this;
    }

    public SpelunkerPacketDispatcher addDouble(double val)
    {
        try
        {
            dataStream.writeDouble(val);
        }
        catch (IOException e)
        {
            ModLog.log(Level.WARN, e, "Failed to set value to a packet.");
        }
        return this;
    }

    public SpelunkerPacketDispatcher addFloat(float val)
    {
        try
        {
            dataStream.writeFloat(val);
        }
        catch (IOException e)
        {
            ModLog.log(Level.WARN, e, "Failed to set value to a packet.");
        }
        return this;
    }

    public SpelunkerPacketDispatcher addString(String val)
    {
       byte[] bytes = val.getBytes();
       try
       {
            dataStream.write(bytes);
            dataStream.writeShort(bytes.length);
        }
       catch (IOException e)
       {
            ModLog.log(Level.WARN, e, "Failed to set value to a packet.");
        }
       return this;
    }

    public void pack()
    {
        packet.setSendData(bytesStream.toByteArray());
        this.close();
        this.packed = true;
    }

    public SpelunkerCommonPacketHandler getPacket()
    {
        if (!this.packed) this.pack();
        return this.packet;
    }

    public void sendPacketPlayer(EntityPlayer player)
    {
        if (!this.packed) this.pack();
        if (((EntityPlayerMP)player).playerNetServerHandler != null)
        {
            networkHandler.sendTo(packet, (EntityPlayerMP)player);
            //ModLog.debug("-> Sent to client");
        }
    }

    public void sendPacketAll()
    {
        if (!this.packed) this.pack();
        networkHandler.sendToAll(packet);
    }

    public void sendPacketToServer()
    {
        if (!this.packed) this.pack();
        networkHandler.sendToServer(packet);
        //ModLog.debug("-> Sent to server");
    }

    private void close()
    {
        try
        {
            if (bytesStream != null) bytesStream.close();
        }
        catch (IOException e) {}
        try
        {
            if (dataStream != null) dataStream.close();
        }
        catch (IOException e) {}
    }

}
