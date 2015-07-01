package tsuteto.spelunker.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import tsuteto.spelunker.entity.model.ModelWaterLift;

public class RenderWaterLift extends Render
{
    private static final ResourceLocation textures = new ResourceLocation("spelunker", "textures/waterLift.png");
    protected ModelBase model;

    public RenderWaterLift()
    {
        this.shadowSize = 0.5F;
        this.model = new ModelWaterLift();
    }

    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)p_76986_2_, (float)p_76986_4_, (float)p_76986_6_);
        this.bindEntityTexture(p_76986_1_);
        this.model.render(p_76986_1_, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return textures;
    }
}
