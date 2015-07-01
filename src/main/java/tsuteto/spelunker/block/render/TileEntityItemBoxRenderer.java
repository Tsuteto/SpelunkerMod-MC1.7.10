package tsuteto.spelunker.block.render;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import tsuteto.spelunker.block.tileentity.TileEntityItemBox;

public class TileEntityItemBoxRenderer extends TileEntitySpecialRenderer
{
    private RenderItem itemRenderer = new RenderItem();

    public TileEntityItemBoxRenderer()
    {
        this.itemRenderer.setRenderManager(RenderManager.instance);
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick)
    {
        TileEntityItemBox itemBox = (TileEntityItemBox) te;
        Minecraft mc = FMLClientHandler.instance().getClient();
        if (itemBox != null)
        {
            if (itemBox.isItemAvailable() || mc.thePlayer.capabilities.isCreativeMode)
            {
                if (itemBox.itemEntity != null)
                {
                    itemRenderer.doRender(itemBox.itemEntity, x + 0.5D, y + 0.25D, z + 0.5D, 0.0F, partialTick);
                }
            }
        }
    }
}
