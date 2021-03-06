// Date: 2015/01/17 21:54:20
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package tsuteto.spelunker.block.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Model for Spelunker Portal Block
 *
 * @author Originally created by 超田中氏 (Tyotanaka) - Thx!
 */
public class ModelSpelunkerPortalStatue extends ModelBase
{
    //fields
    ModelRenderer helmet1;
    ModelRenderer helmet2;
    ModelRenderer helmet3;
    ModelRenderer light1;
    ModelRenderer light2;
    ModelRenderer head1;
    ModelRenderer head2;
    ModelRenderer head3;
    ModelRenderer body1;
    ModelRenderer body2;
    ModelRenderer body3;
    ModelRenderer body4;
    ModelRenderer body5;
    ModelRenderer body6;
    ModelRenderer leg1;
    ModelRenderer leg2;
    ModelRenderer leg3;

    public ModelSpelunkerPortalStatue()
    {
        textureWidth = 64;
        textureHeight = 64;

        helmet1 = new ModelRenderer(this, 48, 0);
        helmet1.addBox(0F, 0F, 0F, 5, 1, 2);
        helmet1.setRotationPoint(-3F, -8F, -1F);
        helmet1.setTextureSize(64, 64);
        helmet1.mirror = true;
        setRotation(helmet1, 0F, 0F, 0F);
        helmet2 = new ModelRenderer(this, 45, 0);
        helmet2.addBox(0F, 0F, 0F, 7, 1, 2);
        helmet2.setRotationPoint(-4F, -7F, -1F);
        helmet2.setTextureSize(64, 64);
        helmet2.mirror = true;
        setRotation(helmet2, 0F, 0F, 0F);
        helmet3 = new ModelRenderer(this, 42, 0);
        helmet3.addBox(0F, 0F, 0F, 9, 1, 2);
        helmet3.setRotationPoint(-5F, -6F, -1F);
        helmet3.setTextureSize(64, 64);
        helmet3.mirror = true;
        setRotation(helmet3, 0F, 0F, 0F);
        light1 = new ModelRenderer(this, 26, 0);
        light1.addBox(0F, 0F, 0F, 1, 1, 2);
        light1.setRotationPoint(3F, -7F, -1F);
        light1.setTextureSize(64, 64);
        light1.mirror = true;
        setRotation(light1, 0F, 0F, 0F);
        light2 = new ModelRenderer(this, 26, 0);
        light2.addBox(0F, 0F, 0F, 1, 3, 2);
        light2.setRotationPoint(4F, -8F, -1F);
        light2.setTextureSize(64, 64);
        light2.mirror = true;
        setRotation(light2, 0F, 0F, 0F);
        head1 = new ModelRenderer(this, 0, 0);
        head1.addBox(0F, 0F, 0F, 7, 1, 2);
        head1.setRotationPoint(-4F, -5F, -1F);
        head1.setTextureSize(64, 64);
        head1.mirror = true;
        setRotation(head1, 0F, 0F, 0F);
        head2 = new ModelRenderer(this, 0, 3);
        head2.addBox(0F, 0F, 0F, 8, 1, 2);
        head2.setRotationPoint(-4F, -4F, -1F);
        head2.setTextureSize(64, 64);
        head2.mirror = true;
        setRotation(head2, 0F, 0F, 0F);
        head3 = new ModelRenderer(this, 2, 6);
        head3.addBox(0F, 0F, 0F, 6, 1, 2);
        head3.setRotationPoint(-3F, -3F, -1F);
        head3.setTextureSize(64, 64);
        head3.mirror = true;
        setRotation(head3, 0F, 0F, 0F);
        body1 = new ModelRenderer(this, 2, 9);
        body1.addBox(0F, 0F, 0F, 5, 1, 2);
        body1.setRotationPoint(-3F, -2F, -1F);
        body1.setTextureSize(64, 64);
        body1.mirror = true;
        setRotation(body1, 0F, 0F, 0F);
        body2 = new ModelRenderer(this, 0, 12);
        body2.addBox(0F, 0F, 0F, 6, 1, 2);
        body2.setRotationPoint(-4F, -1F, -1F);
        body2.setTextureSize(64, 64);
        body2.mirror = true;
        setRotation(body2, 0F, 0F, 0F);
        body3 = new ModelRenderer(this, 0, 15);
        body3.addBox(0F, 0F, 0F, 7, 1, 2);
        body3.setRotationPoint(-4F, 0F, -1F);
        body3.setTextureSize(64, 64);
        body3.mirror = true;
        setRotation(body3, 0F, 0F, 0F);
        body4 = new ModelRenderer(this, 0, 18);
        body4.addBox(0F, 0F, 0F, 7, 1, 2);
        body4.setRotationPoint(-4F, 1F, -1F);
        body4.setTextureSize(64, 64);
        body4.mirror = true;
        setRotation(body4, 0F, 0F, 0F);
        body5 = new ModelRenderer(this, 2, 21);
        body5.addBox(0F, 0F, 0F, 6, 1, 2);
        body5.setRotationPoint(-3F, 2F, -1F);
        body5.setTextureSize(64, 64);
        body5.mirror = true;
        setRotation(body5, 0F, 0F, 0F);
        body6 = new ModelRenderer(this, 4, 24);
        body6.addBox(0F, 0F, 0F, 5, 1, 2);
        body6.setRotationPoint(-2F, 3F, -1F);
        body6.setTextureSize(64, 64);
        body6.mirror = true;
        setRotation(body6, 0F, 0F, 0F);
        leg1 = new ModelRenderer(this, 54, 4);
        leg1.addBox(0F, 0F, 0F, 3, 2, 2);
        leg1.setRotationPoint(-1F, 4F, -1F);
        leg1.setTextureSize(64, 64);
        leg1.mirror = true;
        setRotation(leg1, 0F, 0F, 0F);
        leg2 = new ModelRenderer(this, 54, 0);
        leg2.addBox(0F, 0F, 0F, 3, 2, 2);
        leg2.setRotationPoint(-1F, 6F, -1F);
        leg2.setTextureSize(64, 64);
        leg2.mirror = true;
        setRotation(leg2, 0F, 0F, 0F);
        leg3 = new ModelRenderer(this, 56, 0);
        leg3.addBox(0F, 0F, 0F, 1, 1, 2);
        leg3.setRotationPoint(2F, 7F, -1F);
        leg3.setTextureSize(64, 64);
        leg3.mirror = true;
        setRotation(leg3, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        helmet1.render(f5);
        helmet2.render(f5);
        helmet3.render(f5);
        light1.render(f5);
        light2.render(f5);
        head1.render(f5);
        head2.render(f5);
        head3.render(f5);
        body1.render(f5);
        body2.render(f5);
        body3.render(f5);
        body4.render(f5);
        body5.render(f5);
        body6.render(f5);
        leg1.render(f5);
        leg2.render(f5);
        leg3.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
