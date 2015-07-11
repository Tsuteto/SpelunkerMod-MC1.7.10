package tsuteto.spelunker.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import tsuteto.spelunker.init.SpelunkerItems;

public class EntitySpelunkart extends EntityMinecartEmpty
{
    public static final int TICKS_GONE = 200;

    private int ticksAbandoned = 0;

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

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (this.riddenByEntity != null)
        {
            ticksAbandoned = 0;
        }
        else if (ticksAbandoned++ > TICKS_GONE)
        {
            this.setDead();
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound p_70014_1_)
    {
        super.writeEntityToNBT(p_70014_1_);
        p_70014_1_.setShort("Abandoned", (short)this.ticksAbandoned);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound p_70037_1_)
    {
        super.readEntityFromNBT(p_70037_1_);
        this.ticksAbandoned = p_70037_1_.getShort("Abandoned");
    }

    @SideOnly(Side.CLIENT)
    public int getTicksAbandoned()
    {
        return this.ticksAbandoned;
    }
}
