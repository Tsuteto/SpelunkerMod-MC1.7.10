package tsuteto.spelunker.entity.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import tsuteto.spelunker.entity.model.ModelDynamitePrimed;

@SideOnly(Side.CLIENT)
public class RenderDynamitePrimed extends Render
{
    private final ModelDynamitePrimed model = new ModelDynamitePrimed(0, 0, 64, 32);

    private static final ResourceLocation texture = new ResourceLocation("spelunker", "textures/dynamitePrimed.png");

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);

        GL11.glTranslatef((float)par2, (float)par4, (float)par6);

        float var10 = 0.0625F;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        this.bindEntityTexture(par1Entity);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        this.model.render(par1Entity, 0.0F, 0.0F, 0.0F, this.renderManager.playerViewY, 0.0F, var10);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return texture;
    }
}
