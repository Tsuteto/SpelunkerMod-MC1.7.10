package tsuteto.spelunker.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import net.minecraft.entity.player.EntityPlayer;

public interface MessageToServer
{
    public IMessage handleServerSide(EntityPlayer player);
}
