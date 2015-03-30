package tsuteto.spelunker.block.tileentity;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import org.apache.commons.io.Charsets;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.levelmap.MapSource;
import tsuteto.spelunker.levelmap.SpelunkerMapInfo;
import tsuteto.spelunker.network.PacketDispatcher;
import tsuteto.spelunker.network.packet.PacketContainerData;

import java.util.List;

abstract public class ContainerMapSelectorBase extends Container implements IGuiRemoteControl
{
    @SideOnly(Side.CLIENT)
    public List<SpelunkerMapInfo> mapList;
    @SideOnly(Side.CLIENT)
    public int mapRowSelected;

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);

        if (par1ICrafting instanceof EntityPlayerMP)
        {
            // Send the map list to client
            PacketContainerData packet = new PacketContainerData(this.windowId, 0, new PacketContainerData.DataHandler()
            {
                @Override
                public void addData(ByteBuf byteBuf)
                {
                    List<SpelunkerMapInfo> infoList = SpelunkerMod.mapManager().getInfoList();
                    byteBuf.writeShort(infoList.size());
                    for (SpelunkerMapInfo info : infoList)
                    {
                        byteBuf.writeByte(info.source.ordinal());
                        byte[] bytes1 = info.mapName.getBytes(Charsets.UTF_8);
                        byteBuf.writeShort(bytes1.length);
                        byteBuf.writeBytes(bytes1);
                        byte[] bytes2 = info.fileName.getBytes(Charsets.UTF_8);
                        byteBuf.writeShort(bytes2.length);
                        byteBuf.writeBytes(bytes2);
                    }

                }
            });
            PacketDispatcher.packet(packet).sendToPlayer((EntityPlayerMP) par1ICrafting);
        }
    }

    @Override
    public void onGuiControl(EntityPlayer player, int eventId, ByteBuf buffer)
    {
        if (eventId == 0)
        {
            onMapSelected(player, buffer);
        }
    }

    abstract protected void onMapSelected(EntityPlayer player, ByteBuf buffer);

    @SideOnly(Side.CLIENT)
    @Override
    public void updateRemoteData(int id, ByteBuf data)
    {
        // Receive map list
        if (id == 0)
        {
            this.mapList = Lists.newArrayList();

            int listLen = data.readShort();
            for (int i = 0; i < listLen; i++)
            {
                int source = data.readByte();
                int nameLen = data.readShort();
                byte[] nameBytes = new byte[nameLen];
                data.readBytes(nameBytes);
                int filenameLen = data.readShort();
                byte[] filenameBytes = new byte[filenameLen];
                data.readBytes(filenameBytes);

                SpelunkerMapInfo info = new SpelunkerMapInfo();
                info.source = MapSource.values()[source];
                info.mapName = new String(nameBytes, Charsets.UTF_8);
                info.fileName = new String(filenameBytes, Charsets.UTF_8);
                this.mapList.add(info);
            }

            this.onMapListReceived(data);
        }
    }

    @SideOnly(Side.CLIENT)
    protected void onMapListReceived(ByteBuf data) {}
}
