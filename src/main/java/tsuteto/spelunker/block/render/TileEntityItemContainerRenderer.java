package tsuteto.spelunker.block.render;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import tsuteto.spelunker.block.tileentity.TileEntityItemBox;
import tsuteto.spelunker.util.ModLog;

public class TileEntityItemContainerRenderer extends TileEntitySpecialRenderer
{
    private RenderItem itemRenderer = new RenderItem();

    public TileEntityItemContainerRenderer()
    {
        this.itemRenderer.setRenderManager(RenderManager.instance);
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick)
    {
        TileEntityItemBox itemContainer = (TileEntityItemBox)te;
        if (itemContainer != null && itemContainer.isItemAvailable())
        {
            if (itemContainer.itemEntity != null)
            {
                itemRenderer.doRender(itemContainer.itemEntity, x + 0.5D, y + 0.25D, z + 0.5D, 0.0F, partialTick);
            }
            else
            {
                ModLog.debug("ItemEntity is null!");
            }
        }
    }
}
