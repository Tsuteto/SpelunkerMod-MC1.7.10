package tsuteto.spelunker.entity.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelSteamHole extends ModelBase
{
    public ModelRenderer[] models = new ModelRenderer[1];

    public ModelSteamHole()
    {
        byte b0 = 6;
        byte b2 = 4;
        this.models[0] = new ModelRenderer(this, 0, 0);
        this.models[0].addBox((float)(-b0 / 2), 0.0f, (float)(-b0 / 2), b0, b2, 6, 0.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        for (int i = 0; i < models.length; ++i)
        {
            this.models[i].render(p_78088_7_);
        }
    }
}