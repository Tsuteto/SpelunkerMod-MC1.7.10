package tsuteto.spelunker.block.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import tsuteto.spelunker.block.BlockSpelunkerPortal;
import tsuteto.spelunker.block.model.ModelSpelunkerPortalStage;
import tsuteto.spelunker.block.model.ModelSpelunkerPortalStatue;

public class TileEntitySpelunkerPortalRenderer extends TileEntitySpecialRenderer
{
    private final ModelBase modelStatue = new ModelSpelunkerPortalStatue();
    private final ModelBase modelStage = new ModelSpelunkerPortalStage();
    private final ResourceLocation texture_black = new ResourceLocation("spelunker", "textures/spelunkerPortal_black.png");
    private final ResourceLocation texture_red = new ResourceLocation("spelunker", "textures/spelunkerPortal_red.png");

    @Override
    public void renderTileEntityAt(TileEntity p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_)
    {
        int meta = p_147500_1_.getBlockMetadata();
        int i = meta & 7;
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

        this.bindTexture(this.getTexture(p_147500_1_));

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef((float)p_147500_2_ + 0.5F, (float)p_147500_4_ + 0.5F, (float)p_147500_6_ + 0.5F);
        float var10 = 0.0625F;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glRotatef((float)angle, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        if ((meta & 8) != 0)
        {
            this.modelStatue.render(null, 0.0F, 0.0F, 0.0F, p_147500_8_, 0.0F, var10);
        }
        else
        {
            this.modelStage.render(null, 0.0F, 0.0F, 0.0F, p_147500_8_, 0.0F, var10);
        }
        GL11.glPopMatrix();
    }

    private ResourceLocation getTexture(TileEntity te)
    {
        BlockSpelunkerPortal.Type type = ((BlockSpelunkerPortal)te.getBlockType()).type;
        if (type == BlockSpelunkerPortal.Type.SURVIVAL) return texture_black;
        else return texture_red;
    }
}