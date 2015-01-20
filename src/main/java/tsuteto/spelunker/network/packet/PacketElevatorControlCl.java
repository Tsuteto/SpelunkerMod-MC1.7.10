package tsuteto.spelunker.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import tsuteto.spelunker.entity.EntityElevator;
import tsuteto.spelunker.network.AbstractPacket;
import tsuteto.spelunker.network.MessageToClient;
import tsuteto.spelunker.util.ModLog;

public class PacketElevatorControlCl extends AbstractPacket implements MessageToClient
{
    public int elevatorEntityId;
    public int controllerEntityId;
    public boolean isConnection;

    public PacketElevatorControlCl() {}

    /**
     * Server side constructor
     *
     * @param elevatorEntityId
     * @param controllerEntityId
     * @param isConnection
     */
    public PacketElevatorControlCl(int elevatorEntityId, int controllerEntityId, boolean isConnection)
    {
        this.elevatorEntityId = elevatorEntityId;
        this.controllerEntityId = controllerEntityId;
        this.isConnection = isConnection;
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        buffer.writeInt(elevatorEntityId);
        buffer.writeInt(controllerEntityId);
        buffer.writeBoolean(isConnection);
    }

    @Override
    public void decodeInto(ByteBuf buffer)
    {
        this.elevatorEntityId = buffer.readInt();
        this.controllerEntityId = buffer.readInt();
        this.isConnection = buffer.readBoolean();
    }

    @Override
    public IMessage handleClientSide(EntityPlayer player)
    {
        //SpelunkerPlayerSP spelunker = SpelunkerMod.getSpelunkerPlayer(player);

        if (this.isConnection)
        {
            Entity entity = player.worldObj.getEntityByID(this.elevatorEntityId);
            if (entity instanceof EntityElevator)
            {
                EntityElevator elevator = (EntityElevator) entity;
                elevator.controlledBy = (EntityPlayer)elevator.worldObj.getEntityByID(controllerEntityId);
                ModLog.debug("Elevator connected on client");
            }
        }
        else
        {
            Entity entity = player.worldObj.getEntityByID(this.elevatorEntityId);
            if (entity instanceof EntityElevator)
            {
                EntityElevator elevator = (EntityElevator) entity;
                elevator.controlledBy = null;
                ModLog.debug("Elevator disconnected from client");
            }
        }
        return null;
    }

}
