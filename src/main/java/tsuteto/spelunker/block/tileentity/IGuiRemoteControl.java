package tsuteto.spelunker.block.tileentity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public interface IGuiRemoteControl
{
    public void onGuiControl(EntityPlayer player, int eventId, ByteBuf buffer);
    public void updateRemoteData(int id, ByteBuf data);
}
