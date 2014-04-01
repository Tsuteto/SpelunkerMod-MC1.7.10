package tsuteto.spelunker.entity;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;


/**
 * Renders blaster bullets, referred to EntityFN5728SS190 by MMM
 */
public class RenderGunBullet extends Render
{
    public void renderBullet(EntityGunBullet entityBullet, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        GL11.glRotatef((entityBullet.prevRotationYaw + (entityBullet.rotationYaw - entityBullet.prevRotationYaw) * par9) - 90F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(entityBullet.prevRotationPitch + (entityBullet.rotationPitch - entityBullet.prevRotationPitch) * par9, 0.0F, 0.0F, 1.0F);
        Tessellator tessellator = Tessellator.instance;
        int i = 0;
        float f8 = 0.1625F;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glRotatef(45F, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(f8, f8, f8);
        GL11.glTranslatef(-4F, 0.0F, 0.0F);
        GL11.glNormal3f(f8, 0.0F, 0.0F);

        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(1.0F, 0.3F, 0.4F, 1.0F);
        tessellator.addVertex(4.5D, -0.5D, 0.0D);
        tessellator.addVertex(4.5D, 0.0D, -0.5D);
        tessellator.addVertex(4.5D, 0.5D, 0.0D);
        tessellator.addVertex(4.5D, 0.0D, 0.5D);
        tessellator.draw();

        GL11.glNormal3f(-f8, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(1.0F, 0.3F, 0.4F, 1.0F);
        tessellator.addVertex(4.5D, 0.0D, 0.5D);
        tessellator.addVertex(4.5D, 0.5D, 0.0D);
        tessellator.addVertex(4.5D, 0.0D, -0.5D);
        tessellator.addVertex(4.5D, -0.5D, 0.0D);
        tessellator.draw();

        for (int j = 0; j < 4; j++)
        {
            GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, f8);
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_F(1.0F, 0.3F, 0.4F, 1.0F);
            tessellator.addVertex(4.5D, -0.5D, 0.0D);
            tessellator.addVertex(6.5D, -0.5D, 0.0D);
            tessellator.addVertex(6.5D, 0.5D, 0.0D);
            tessellator.addVertex(4.5D, 0.5D, 0.0D);
            tessellator.draw();
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
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
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        renderBullet((EntityGunBullet)par1Entity, par2, par4, par6, par8, par9);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return null;
    }
}
