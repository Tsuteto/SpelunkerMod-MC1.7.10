package tsuteto.spelunker.entity.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import tsuteto.spelunker.entity.EntityFlash;

/**
 * Renders the projectile of Flash, originally "IlluminatingFlare" by ayashige
 *
 * @author ayashige, Tsuteto
 *
 */
public class RenderFlash extends Render
{
    private static final ResourceLocation texture = new ResourceLocation("spelunker", "textures/items/flash.png");

    public void renderFlare(EntityFlash var1, double var2, double var4, double var6, float var8, float var9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)var2, (float)var4, (float)var6);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        byte var10 = 17;
        this.bindEntityTexture(var1);
        Tessellator var11 = Tessellator.instance;
        float var12 = 0;
        float var13 = 1;
        float var14 = 0;
        float var15 = 1;
        float var16 = 1.0F;
        float var17 = 0.5F;
        float var18 = 0.25F;
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        var11.startDrawingQuads();
        var11.setNormal(0.0F, 1.0F, 0.0F);
        var11.addVertexWithUV((0.0F - var17), (0.0F - var18), 0.0D, var12, var15);
        var11.addVertexWithUV((var16 - var17), (0.0F - var18), 0.0D, var13, var15);
        var11.addVertexWithUV((var16 - var17), (var16 - var18), 0.0D, var13, var14);
        var11.addVertexWithUV((0.0F - var17), (var16 - var18), 0.0D, var12, var14);
        var11.draw();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
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
        this.renderFlare((EntityFlash)var1, var2, var4, var6, var8, var9);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return texture;
    }
}
