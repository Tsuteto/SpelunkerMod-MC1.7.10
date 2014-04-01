package tsuteto.spelunker.damage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IChatComponent;

public class EntityDamageSourcePetKilled extends EntityDamageSource
{
    public EntityDamageSourcePetKilled(String par1Str, Entity par2Entity)
    {
        super(par1Str, par2Entity);
    }

    @Override
    public Entity getEntity()
    {
        return this.damageSourceEntity;
    }

    /**
     * Returns the message to be displayed on player death.
     */
    @Override
    public IChatComponent func_151519_b(EntityLivingBase par1EntityLivingBase)
    {
        return new ChatComponentTranslation("death.attack." + this.damageType,
                par1EntityLivingBase.func_145748_c_(),
                this.damageSourceEntity.func_145748_c_());
    }
}
