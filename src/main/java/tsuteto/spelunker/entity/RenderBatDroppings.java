package tsuteto.spelunker.entity;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Renders Bat Droppings
 *
 * @author Tsuteto
 *
 */
@SideOnly(Side.CLIENT)
public class RenderBatDroppings extends Render
{
    private static final ResourceLocation texture = new ResourceLocation("spelunker", "textures/batDroppings.png");

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        this.bindEntityTexture(par1Entity);
        Tessellator var10 = Tessellator.instance;

        this.func_77026_a(var10);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    private void func_77026_a(Tessellator par1Tessellator)
    {
        float var3 = 0;
        float var4 = 1;
        float var5 = 0;
        float var6 = 1;
        float var7 = 1.0F;
        float var8 = 0.5F;
        float var9 = 0.25F;
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        //GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setNormal(0.0F, 1.0F, 0.0F);
        par1Tessellator.addVertexWithUV((0.0F - var8), (0.0F - var9), 0.0D, var3, var6);
        par1Tessellator.addVertexWithUV((var7 - var8), (0.0F - var9), 0.0D, var4, var6);
        par1Tessellator.addVertexWithUV((var7 - var8), (var7 - var9), 0.0D, var4, var5);
        par1Tessellator.addVertexWithUV((0.0F - var8), (var7 - var9), 0.0D, var3, var5);
        par1Tessellator.draw();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return texture;
    }
}
