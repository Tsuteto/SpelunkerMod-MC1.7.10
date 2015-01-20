package tsuteto.spelunker.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelDynamitePrimed extends ModelBase
{
    public ModelRenderer body;
    public ModelRenderer fuse;

    public float rotateAngleX;

    public ModelDynamitePrimed(int par1, int par2, int par3, int par4)
    {
        this.textureWidth = par3;
        this.textureHeight = par4;

        body = new ModelRenderer(this, 0, 0);
        body.addBox(-3.0F, 2.0F, -3.0F, 6, 6, 6, 0.0F);

        fuse = new ModelRenderer(this, 24, 0);
        fuse.mirror = true;
        fuse.addBox(-2.5F, -8.0F, 0.0F, 5, 10, 0, 0.0F);

        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.fuse.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    @Override
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);
        this.body.render(par7);
        this.fuse.render(par7);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    @Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
        super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
        this.fuse.rotateAngleY = (par4 + rotateAngleX) / (180F / (float)Math.PI);
        this.fuse.rotateAngleX = par5 / (180F / (float)Math.PI);
    }
}
