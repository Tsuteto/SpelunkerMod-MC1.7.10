package tsuteto.spelunker.eventhandler;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.client.event.RenderLivingEvent;
import org.lwjgl.opengl.GL11;
import tsuteto.spelunker.entity.EntityGhost;
import tsuteto.spelunker.texture.SpeTexture;

public class RenderEvent
{
    @SubscribeEvent
    public void renderEntitySpecials(RenderLivingEvent.Specials.Post event)
    {

        if (event.entity instanceof EntityGhost)
        {
            Minecraft mc = FMLClientHandler.instance().getClient();
            EntityPlayerSP player = mc.thePlayer;

            if (!player.canEntityBeSeen(event.entity))
            {
                Gui gui = new Gui();
                RenderManager renderManager = RenderManager.instance;

                double x = event.x;
                double y = event.y;
                double z = event.z;
                float f = 6.4F;
                float f1 = 0.016666668F * f;

                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0, 0xf0);

                GL11.glPushMatrix();
                GL11.glTranslatef((float) x, (float) y + event.entity.height / 2 + 0.5F, (float) z);
                GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                GL11.glScalef(-f1, -f1, f1);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glTranslatef(0.0F, 0.25F / f1, 0.0F);
                GL11.glDepthMask(false);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                mc.getTextureManager().bindTexture(SpeTexture.main);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
                gui.drawTexturedModalRect(-8, -9, 90, 0, 17, 18);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
            }
        }
    }
}
