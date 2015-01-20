package tsuteto.spelunker.entity.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelSteamHole extends ModelBase
{
    public ModelRenderer body;

    public ModelSteamHole()
    {
        byte b0 = 6;
        byte b2 = 4;
        this.body = new ModelRenderer(this, 0, 0);
        this.body.addBox(-b0 / 2.0F, 0.0F, -b0 / 2.0F, b0, b2, 6, 0.0F);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        this.body.render(p_78088_7_);
    }

    @Override
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        this.body.rotateAngleY = p_78087_4_ / (180F / (float)Math.PI);
        this.body.rotateAngleX = p_78087_5_ / (180F / (float)Math.PI);
    }
}