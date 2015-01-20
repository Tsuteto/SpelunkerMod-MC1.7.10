package tsuteto.spelunker.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import tsuteto.spelunker.entity.EntityElevator;
import tsuteto.spelunker.network.AbstractPacket;
import tsuteto.spelunker.network.MessageToServer;
import tsuteto.spelunker.network.PacketDispatcher;

public class PacketElevatorControlSv extends AbstractPacket implements MessageToServer
{
    public int elevatorEntityId;
    public int mode; // 0: Still, 1: Up, 2: Down, 3: SetPosition

    public PacketElevatorControlSv() {}

    /**
     * Client side constructor
     *
     * @param elevatorEntityId
     * @param mode
     */
    public PacketElevatorControlSv(int elevatorEntityId, int mode)
    {
        this.elevatorEntityId = elevatorEntityId;
        this.mode = mode;
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        buffer.writeInt(elevatorEntityId);
        buffer.writeByte(mode);
    }

    @Override
    public void decodeInto(ByteBuf buffer)
    {
        this.elevatorEntityId = buffer.readInt();
        this.mode = buffer.readByte();
    }

    @Override
    public IMessage handleServerSide(EntityPlayer player)
    {
        Entity entity = player.worldObj.getEntityByID(this.elevatorEntityId);

        if (entity instanceof EntityElevator)
        {
            EntityElevator elevator = (EntityElevator)entity;
            if (elevator.controlledBy != null && player.getEntityId() == elevator.controlledBy.getEntityId())
            {
                if (mode == EntityElevator.MODE_STILL) elevator.setNeutral();
                if (mode == EntityElevator.MODE_UP) elevator.moveUp();
                if (mode == EntityElevator.MODE_DOWN) elevator.moveDown();
                PacketDispatcher.packet(new PacketElevatorState(this.elevatorEntityId, mode, elevator.posY))
                        .sendToAllAround(elevator.posX, elevator.posY, elevator.posZ, 64, elevator.dimension);
                return null;
            }
        }
        return null;
    }
}
