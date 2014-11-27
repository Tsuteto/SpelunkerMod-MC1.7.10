package tsuteto.spelunker.entity.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelLift extends ModelBase
{
    public ModelRenderer[] boatSides = new ModelRenderer[1];

    public ModelLift()
    {
        byte b0 = 32;
        byte b2 = 32;
        byte b3 = 0;
        this.boatSides[0] = new ModelRenderer(this, 0, 0).setTextureSize(128, 64);
        this.boatSides[0].addBox((float)(-b0 / 2), (float)(-b2 / 2 + 2), -0.0F, b0, b2 - 4, 4, 0.0F);
        this.boatSides[0].setRotationPoint(0.0F, (float)b3, 0.0F);
        this.boatSides[0].rotateAngleX = ((float)Math.PI / 2F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        for (int i = 0; i < boatSides.length; ++i)
        {
            this.boatSides[i].render(p_78088_7_);
        }
    }
}