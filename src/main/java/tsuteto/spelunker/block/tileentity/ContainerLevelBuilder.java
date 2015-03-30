package tsuteto.spelunker.block.tileentity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.item.ItemLevelBuilder;
import tsuteto.spelunker.levelmap.SpelunkerMapInfo;

public class ContainerLevelBuilder extends ContainerMapSelectorBase
{
    int x;
    int y;
    int z;

    public ContainerLevelBuilder(InventoryPlayer invPlayer, int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_)
    {
        return true;
    }

    @Override
    protected void onMapSelected(EntityPlayer player, ByteBuf buffer)
    {
        int rowNum = buffer.readShort();
        SpelunkerMapInfo info = SpelunkerMod.mapManager().getInfoList().get(rowNum);
        ItemLevelBuilder.buildLevel(player.worldObj, x, y, z, info);
    }
}
