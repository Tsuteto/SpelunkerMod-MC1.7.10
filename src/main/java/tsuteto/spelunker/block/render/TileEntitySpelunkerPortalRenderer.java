package tsuteto.spelunker.block.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import tsuteto.spelunker.block.model.ModelSpelunkerPortal;
import tsuteto.spelunker.block.tileentity.TileEntitySpelunkerPortal;

public class TileEntitySpelunkerPortalRenderer extends TileEntitySpecialRenderer
{
    private final ModelBase model = new ModelSpelunkerPortal();
    private final ResourceLocation texture = new ResourceLocation("spelunker", "textures/spelunkerPortal.png");

    public void renderTileEntityAt(TileEntitySpelunkerPortal p_147500_1_, float p_147500_2_, float p_147500_4_, float p_147500_6_, float p_147500_8_)
    {
        int i = p_147500_1_.getBlockMetadata();
        short angle = 0;
        if (i == 2)
        {
            angle = 0;
        }

        if (i == 3)
        {
            angle = 180;
        }

        if (i == 4)
        {
            angle = 90;
        }

        if (i == 5)
        {
            angle = -90;
        }

        this.bindTexture(texture);

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef(p_147500_2_ + 0.5F, p_147500_4_ + 1.5F, p_147500_6_ + 0.5F);
        float var10 = 0.0625F;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glRotatef((float)angle, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        this.model.render(null, 0.0F, 0.0F, 0.0F, p_147500_8_, 0.0F, var10);
        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_)
    {
        this.renderTileEntityAt((TileEntitySpelunkerPortal)p_147500_1_, (float)p_147500_2_, (float)p_147500_4_, (float)p_147500_6_, p_147500_8_);
    }
}