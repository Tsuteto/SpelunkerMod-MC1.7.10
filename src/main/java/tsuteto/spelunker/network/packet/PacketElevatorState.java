package tsuteto.spelunker.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import tsuteto.spelunker.entity.EntityElevator;
import tsuteto.spelunker.network.AbstractPacket;
import tsuteto.spelunker.network.MessageToClient;

public class PacketElevatorState extends AbstractPacket implements MessageToClient
{
    public int elevatorEntityId;
    public int mode;
    public double posY;

    public PacketElevatorState() {}

    /**
     * Client side constructor
     *
     * @param elevatorEntityId
     * @param posY
     */
    public PacketElevatorState(int elevatorEntityId, int mode, double posY)
    {
        this.elevatorEntityId = elevatorEntityId;
        this.mode = mode;
        this.posY = posY;
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        buffer.writeInt(elevatorEntityId);
        buffer.writeInt(mode);
        buffer.writeDouble(posY);
    }

    @Override
    public void decodeInto(ByteBuf buffer)
    {
        this.elevatorEntityId = buffer.readInt();
        this.mode = buffer.readInt();
        this.posY = buffer.readDouble();
    }

    @Override
    public IMessage handleClientSide(EntityPlayer player)
    {
        Entity entity = player.worldObj.getEntityByID(this.elevatorEntityId);

        if (entity instanceof EntityElevator)
        {
            EntityElevator elevator = (EntityElevator)entity;
            if (mode == EntityElevator.MODE_STILL) elevator.setNeutral();
            if (mode == EntityElevator.MODE_UP) elevator.moveUp();
            if (mode == EntityElevator.MODE_DOWN) elevator.moveDown();

            elevator.syncPosition(this.posY);
        }
        return null;
    }
}
