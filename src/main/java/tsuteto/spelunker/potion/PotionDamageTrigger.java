package tsuteto.spelunker.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;

public class PotionDamageTrigger extends Potion
{
    DamageSource damageSource;
    float damageAmount;

    protected PotionDamageTrigger(int par1, boolean par2, int par3)
    {
        super(par1, par2, par3);
        this.damageSource = DamageSource.magic;
        this.damageAmount = 1;
    }

    protected PotionDamageTrigger setDamageSource(DamageSource source, float amount)
    {
        this.damageSource = source;
        this.damageAmount = amount;
        return this;
    }

    @Override
    public void performEffect(EntityLivingBase par1EntityLivingBase, int par2)
    {
        par1EntityLivingBase.attackEntityFrom(this.damageSource, this.damageAmount);
    }

    @Override
    public boolean isReady(int par1, int par2)
    {
        return par1 == 1;
    }

    @Override
    protected PotionDamageTrigger setIconIndex(int par1, int par2)
    {
        super.setIconIndex(par1, par2);
        return this;
    }

    @Override
    public PotionDamageTrigger setPotionName(String par1Str)
    {
        super.setPotionName(par1Str);
        return this;
    }
}
