package tsuteto.spelunker.network.packet;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import tsuteto.spelunker.entity.EntityElevator;
import tsuteto.spelunker.gui.TitleController;
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

                Minecraft mc = FMLClientHandler.instance().getClient();
                if (this.controllerEntityId == mc.thePlayer.getEntityId())
                {
                    GameSettings gamesettings = mc.gameSettings;
                    String sneakKey = GameSettings.getKeyDisplayString(gamesettings.keyBindSneak.getKeyCode());
                    String forwardKey = GameSettings.getKeyDisplayString(gamesettings.keyBindForward.getKeyCode());
                    String backKey = GameSettings.getKeyDisplayString(gamesettings.keyBindBack.getKeyCode());
                    TitleController.instance().setInstruction(I18n.format("Spelunker.elevatorHelp", sneakKey, forwardKey, backKey));
                }
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
