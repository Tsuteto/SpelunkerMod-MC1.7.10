package tsuteto.spelunker.entity.render;

import net.minecraft.client.renderer.entity.RenderMinecart;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.ResourceLocation;

public class RenderSpelunkart extends RenderMinecart
{
    private static final ResourceLocation texture = new ResourceLocation("spelunker", "textures/spelunkart.png");

    @Override
    protected ResourceLocation getEntityTexture(EntityMinecart p_110775_1_)
    {
        return texture;
    }
}
