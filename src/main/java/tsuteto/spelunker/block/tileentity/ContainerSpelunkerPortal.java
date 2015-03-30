package tsuteto.spelunker.block.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.common.DimensionManager;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.block.BlockSpelunkerPortal;
import tsuteto.spelunker.block.SpelunkerBlocks;
import tsuteto.spelunker.dimension.DimensionRegenerator;
import tsuteto.spelunker.levelmap.SpelunkerMapInfo;

public class ContainerSpelunkerPortal extends ContainerMapSelectorBase
{
    protected final TileEntitySpelunkerPortal spelunkerPortal;

    public ContainerSpelunkerPortal(InventoryPlayer invPlayer, TileEntitySpelunkerPortal spelunkerPortal)
    {
        this.spelunkerPortal = spelunkerPortal;
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_)
    {
        return this.spelunkerPortal.isUseableByPlayer(p_75145_1_);
    }

    public void onMapSelected(EntityPlayer player, ByteBuf buffer)
    {
        // Map selected
        boolean isRegeneration = buffer.readBoolean();
        int dimId;
        if (isRegeneration)
        {
            dimId = spelunkerPortal.levelInfo.dimId;
            DimensionRegenerator.regenerateDimension(dimId);
        }
        else
        {
            dimId = DimensionManager.getNextFreeDimId();
        }

        // Update Spelunker level info
        int rowNum = buffer.readShort();
        SpelunkerMapInfo info = SpelunkerMod.mapManager().getInfoList().get(rowNum);
        spelunkerPortal.registerLevel(dimId, info);

        // Set owner if null
        if (spelunkerPortal.owner == null)
        {
            spelunkerPortal.owner = player.getUniqueID().toString();
        }

        // Teleport to the level
        ((BlockSpelunkerPortal) SpelunkerBlocks.blockSpelunkerPortal).teleportPlayerToLevel(
                spelunkerPortal.getWorldObj(), spelunkerPortal.xCoord, spelunkerPortal.yCoord, spelunkerPortal.zCoord, player, spelunkerPortal);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onMapListReceived(ByteBuf data)
    {
        if (spelunkerPortal.levelInfo != null && spelunkerPortal.levelInfo.mapFileName != null)
        {
            for (int i = 0; i < mapList.size(); i++)
            {
                if (spelunkerPortal.levelInfo.mapFileName.equals(mapList.get(i).fileName))
                {
                    mapRowSelected = i;
                    break;
                }
            }
        }
    }
}
