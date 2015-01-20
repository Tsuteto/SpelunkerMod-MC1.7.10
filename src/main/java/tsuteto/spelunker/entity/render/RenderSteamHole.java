package tsuteto.spelunker.entity.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import tsuteto.spelunker.entity.EntitySteamHole;
import tsuteto.spelunker.entity.model.ModelSteamHole;

@SideOnly(Side.CLIENT)
public class RenderSteamHole extends Render
{
    private static final ResourceLocation textures = new ResourceLocation("spelunker", "textures/steamHole.png");
    /** instance of ModelBoat for rendering */
    protected ModelBase model;

    public RenderSteamHole()
    {
        this.shadowSize = 0.0F;
        this.model = new ModelSteamHole();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntitySteamHole entity, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_, (float) p_76986_6_);

        switch (entity.dir)
        {
            case DOWN:
                GL11.glTranslatef(0.0F, entity.height, 0.0F);
                break;
            case NORTH:
            case SOUTH:
            case WEST:
            case EAST:
                float offsetX = entity.width / 2.0F * -entity.dir.offsetX;
                float offsetY = entity.width / 2.0F;
                float offsetZ = entity.width / 2.0F * -entity.dir.offsetZ;
                GL11.glTranslatef(offsetX, offsetY, offsetZ);
                break;
        }

        float f4 = 0.75F;
        GL11.glScalef(f4, f4, f4);
        GL11.glScalef(1.0F / f4, 1.0F / f4, 1.0F / f4);
        this.bindEntityTexture(entity);
        this.model.render(entity, 0.0F, 0.0F, 0.0F, entity.rotationYaw, entity.rotationPitch, 0.0625F);
        GL11.glPopMatrix();
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntitySteamHole p_110775_1_)
    {
        return textures;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.getEntityTexture((EntitySteamHole)p_110775_1_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.doRender((EntitySteamHole)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}
