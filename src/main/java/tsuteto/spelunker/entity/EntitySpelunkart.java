package tsuteto.spelunker.entity;

import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import tsuteto.spelunker.init.SpelunkerItems;

public class EntitySpelunkart extends EntityMinecartEmpty
{
    public EntitySpelunkart(World p_i1722_1_)
    {
        super(p_i1722_1_);
    }

    public EntitySpelunkart(World p_i1723_1_, double p_i1723_2_, double p_i1723_4_, double p_i1723_6_)
    {
        super(p_i1723_1_, p_i1723_2_, p_i1723_4_, p_i1723_6_);
    }

    public void killMinecart(DamageSource p_94095_1_)
    {
        this.setDead();
        ItemStack itemstack = new ItemStack(SpelunkerItems.itemSpelunkart, 1);
        this.entityDropItem(itemstack, 0.0F);
    }
}
