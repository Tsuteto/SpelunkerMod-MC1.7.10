package tsuteto.spelunker.entity.render;

import net.minecraft.client.renderer.entity.RenderMinecart;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.ResourceLocation;
import tsuteto.spelunker.entity.EntitySpeBoat;
import tsuteto.spelunker.entity.EntitySpelunkart;

public class RenderSpelunkart extends RenderMinecart
{
    private static final ResourceLocation texture = new ResourceLocation("spelunker", "textures/spelunkart.png");

    @Override
    protected ResourceLocation getEntityTexture(EntityMinecart p_110775_1_)
    {
        return texture;
    }

    @Override
    public void doRender(EntityMinecart p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        int ticksAbandoned = ((EntitySpelunkart)p_76986_1_).getTicksAbandoned();
        if (ticksAbandoned > EntitySpeBoat.TICKS_GONE - 60 && ticksAbandoned % 2 == 0) return;
        super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}
