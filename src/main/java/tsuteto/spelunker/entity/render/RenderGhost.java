package tsuteto.spelunker.entity.render;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import tsuteto.spelunker.entity.model.ModelGhost;

public class RenderGhost extends RenderLiving
{
    public static final ResourceLocation texture = new ResourceLocation("spelunker", "textures/ghost.png");

    public RenderGhost(float p_i1262_2_)
    {
        super(new ModelGhost(), p_i1262_2_);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return texture;
    }
}
