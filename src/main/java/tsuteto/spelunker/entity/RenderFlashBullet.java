package tsuteto.spelunker.entity;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

/**
 * Renders the luminary of Flash, originally "IlluminatingFlare" by ayashige
 *
 * @author ayashige, Tsuteto
 *
 */
public class RenderFlashBullet extends Render
{
    private static final ResourceLocation texture = new ResourceLocation("spelunker", "icons.png");

    public void renderFlare(EntityFlashBullet var1, double var2, double var4, double var6, float var8, float var9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)var2, (float)var4, (float)var6);
        this.bindEntityTexture(var1);
        Tessellator var11 = Tessellator.instance;
        int var12 = 0;
        int var13 = 30;
        double var14 = (var12 / 256.0F);
        double var16 = ((var12 + 15F) / 256.0F);
        double var18 = (var13 / 256.0F);
        double var20 = ((var13 + 15F) / 256.0F);
        double var22 = -0.45D;
        double var24 = 0.45D;
        double var26 = -0.45D;
        double var28 = 0.45D;
        double var30 = -0.5D;
        double var32 = 0.5D;
        var11.startDrawingQuads();
        var11.setNormal(0.0F, 1.0F, 0.0F);
        var11.addVertexWithUV(var22, var32, var26, var14, var18);
        var11.addVertexWithUV(var22, var30, var26, var14, var20);
        var11.addVertexWithUV(var24, var30, var28, var16, var20);
        var11.addVertexWithUV(var24, var32, var28, var16, var18);
        var11.addVertexWithUV(var24, var32, var28, var14, var18);
        var11.addVertexWithUV(var24, var30, var28, var14, var20);
        var11.addVertexWithUV(var22, var30, var26, var16, var20);
        var11.addVertexWithUV(var22, var32, var26, var16, var18);
        var11.addVertexWithUV(var22, var32, var28, var14, var18);
        var11.addVertexWithUV(var22, var30, var28, var14, var20);
        var11.addVertexWithUV(var24, var30, var26, var16, var20);
        var11.addVertexWithUV(var24, var32, var26, var16, var18);
        var11.addVertexWithUV(var24, var32, var26, var14, var18);
        var11.addVertexWithUV(var24, var30, var26, var14, var20);
        var11.addVertexWithUV(var22, var30, var28, var16, var20);
        var11.addVertexWithUV(var22, var32, var28, var16, var18);
        var11.addVertexWithUV(var22, 0.0D, var26, var14, var18);
        var11.addVertexWithUV(var22, 0.0D, var28, var14, var20);
        var11.addVertexWithUV(var24, 0.0D, var28, var16, var20);
        var11.addVertexWithUV(var24, 0.0D, var26, var16, var18);
        var11.addVertexWithUV(var24, 0.0D, var26, var14, var18);
        var11.addVertexWithUV(var24, 0.0D, var28, var14, var20);
        var11.addVertexWithUV(var22, 0.0D, var28, var16, var20);
        var11.addVertexWithUV(var22, 0.0D, var26, var16, var18);
        var11.draw();
        GL11.glPopMatrix();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    @Override
    public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9)
    {
        this.renderFlare((EntityFlashBullet)var1, var2, var4, var6, var8, var9);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return texture;
    }
}
