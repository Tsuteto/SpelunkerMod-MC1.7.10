package tsuteto.spelunker.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import org.apache.logging.log4j.Level;
import tsuteto.spelunker.SpelunkerMod;
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
    private PacketPipeline pipeline = SpelunkerMod.packetPipeline;
    private SpelunkerPacketHandler packet = new SpelunkerPacketHandler();
    private boolean packed = false;

    private ByteArrayOutputStream bytesStream;
	private DataOutputStream dataStream;

	public SpelunkerPacketDispatcher(SpelunkerPacketType type)
	{
        bytesStream = new ByteArrayOutputStream();
        dataStream = new DataOutputStream(bytesStream);

        this.addByte((byte)(type.ordinal() & 0xff));
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

    public SpelunkerPacketHandler getPacket()
    {
        if (!this.packed) this.pack();
        return this.packet;
    }

    public void sendPacketPlayer(EntityPlayer player)
    {
        if (!this.packed) this.pack();
        if (((EntityPlayerMP)player).playerNetServerHandler != null)
        {
            pipeline.sendTo(packet, (EntityPlayerMP)player);
        }
    }

    public void sendPacketAll()
    {
        if (!this.packed) this.pack();
        pipeline.sendToAll(packet);
    }

    public void sendPacketToServer()
    {
        if (!this.packed) this.pack();
        pipeline.sendToServer(packet);
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
