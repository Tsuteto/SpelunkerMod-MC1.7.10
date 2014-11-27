package tsuteto.spelunker.entity.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelElevator extends ModelBase
{
    public ModelRenderer[] boatSides = new ModelRenderer[4];

    public ModelElevator()
    {
        byte b0 = 48;
        byte b2 = 48;
        byte b3 = 0;
        this.boatSides[0] = new ModelRenderer(this, 0, 0).setTextureSize(128, 128);
        this.boatSides[0].addBox((float)(-b0 / 2), (float)(-b2 / 2 + 2), -0.0F, b0, b2 - 4, 4, 0.0F);
        this.boatSides[0].setRotationPoint(0.0F, (float)b3, 0.0F);
        this.boatSides[0].rotateAngleX = ((float)Math.PI / 2F);

        // Fences
        b0 = 48;
        b2 = 9;
        this.boatSides[1] = new ModelRenderer(this, 0, 52).setTextureSize(128, 128);
        this.boatSides[1].addBox(-24.0F, -13.0F, -22.0F, b0, b2, 1, 0.0F);
        this.boatSides[1].setRotationPoint(0.0F, (float)b3, 0.0F);

        this.boatSides[2] = new ModelRenderer(this, 0, 52).setTextureSize(128, 128);
        this.boatSides[2].addBox(-24.0F, -13.0F, 21.0F, b0, b2, 1, 0.0F);
        this.boatSides[2].setRotationPoint(0.0F, (float)b3, 0.0F);

        this.boatSides[3] = new ModelRenderer(this, 0, 62).setTextureSize(128, 128);
        b0 = 42;
        b2 = 9;
        this.boatSides[3].addBox(-21.0F, -13.0F, -24.0F, b0, b2, 1, 0.0F);
        this.boatSides[3].setRotationPoint(0.0F, (float)b3, 0.0F);
        this.boatSides[3].rotateAngleY = ((float)Math.PI / 2F);

        // Pillars
//        this.boatSides[4] = new ModelRenderer(this, 104, 0).setTextureSize(128, 128);
//        b0 = 3;
//        byte b1 = 3;
//        b2 = 64;
//        this.boatSides[4].addBox(-24.0F, -68.0F, -22.0F, b0, b2, b1, 0.0F);
//        this.boatSides[4].setRotationPoint(0.0F, (float)b3, 0.0F);

        // Ceiling
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