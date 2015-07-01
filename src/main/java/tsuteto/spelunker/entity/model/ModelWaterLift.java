package tsuteto.spelunker.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelWaterLift extends ModelBase
{
    public ModelRenderer top;
    public ModelRenderer body;

    public ModelWaterLift()
    {
        int w = 48;
        int d = 48;
        float axis = 0;
        top = new ModelRenderer(this, 0, 0).setTextureSize(128, 128);
        top.addBox((float) (-w / 2), (float) (-d / 2), -16.0F, w, d, 4);
        top.setRotationPoint(0.0F, axis, 0.0F);
        top.rotateAngleX = ((float)Math.PI / 2F);

        body = new ModelRenderer(this, 0, 52).setTextureSize(128, 128);
        body.addBox((float) (-w / 2 + 3), (float) (-d / 2 + 3), -12F, w - 6, d - 6, 12);
        body.setRotationPoint(0.0F, axis, 0.0F);
        body.rotateAngleX = ((float)Math.PI / 2F);

    }

    @Override
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        top.render(p_78088_7_);
        body.render(p_78088_7_);
    }

}
