package tsuteto.spelunker.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import tsuteto.spelunker.item.SpelunkerItem;

import java.util.List;

public class EntitySteamHole extends Entity
{
    public static final int steamDuration = 60;
    public static final int cycleTicks = 200;

    public EntitySteamHole(World p_i1704_1_)
    {
        super(p_i1704_1_);
        this.preventEntitySpawning = true;
        this.setSize(0.375F, 0.25F);
    }

    protected void entityInit()
    {
        this.dataWatcher.addObject(17, Byte.valueOf((byte) 0)); // steaming flag
    }

    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
     * pushable on contact, like boats or minecarts.
     */
    public AxisAlignedBB getCollisionBox(Entity p_70114_1_)
    {
        return p_70114_1_.boundingBox;
    }

    /**
     * returns the bounding box for this entity
     */
    public AxisAlignedBB getBoundingBox()
    {
        return this.boundingBox;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return false;
    }

    public EntitySteamHole(World p_i1705_1_, double p_i1705_2_, double p_i1705_4_, double p_i1705_6_)
    {
        this(p_i1705_1_);
        this.setPosition(p_i1705_2_, p_i1705_4_ + (double)this.yOffset, p_i1705_6_);
        this.prevPosX = p_i1705_2_;
        this.prevPosY = p_i1705_4_;
        this.prevPosZ = p_i1705_6_;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_)
    {
        boolean flag = p_70097_1_.getEntity() instanceof EntityPlayer && ((EntityPlayer)p_70097_1_.getEntity()).capabilities.isCreativeMode;

        if (flag)
        {
            this.setDead();
        }
        return false;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return true;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (!this.worldObj.isRemote)
        {
            this.setSteaming(this.ticksExisted % cycleTicks < steamDuration);
        }

        if (this.isSteaming())
        {
            if (this.worldObj.isRemote)
            {
                float steamX = (float) this.posX;
                float steamY = (float) this.posY + 0.3F;
                float steamZ = (float) this.posZ;
                float steamRandX = this.rand.nextFloat() * 0.1F - 0.05F;
                float steamRandZ = this.rand.nextFloat() * 0.1F - 0.05F;
                double gRand1 = this.rand.nextGaussian() * 0.01D;
                double gRand2 = this.rand.nextDouble() * 0.1D + 0.05D;
                double gRand3 = this.rand.nextGaussian() * 0.01D;
                this.worldObj.spawnParticle("explode", (steamX + steamRandX), steamY, (steamZ + steamRandZ), gRand1, gRand2, gRand3);

            }
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(0.0D, 2.0D, 0.0D).expand(0.2D, 0.0D, 0.2D));
            for (Object entry : list)
            {
                Entity entity = (Entity) entry;
                entity.attackEntityFrom(DamageSource.onFire, 4.0F);
            }
        }

        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.2D, 0.1D, 0.2D));
        for (Object entry : list)
        {
            Entity entity = (Entity) entry;
            if (this.getDistanceToEntity(entity) > this.getDistance(entity.posX + entity.motionX, entity.posY + entity.motionY, entity.posZ + entity.motionZ))
            {
                float f = -0.5F;
                entity.motionX = (double)(-MathHelper.sin(entity.rotationYaw / 180.0F * (float) Math.PI) * f);
                entity.motionZ = (double)(MathHelper.cos(entity.rotationYaw / 180.0F * (float)Math.PI) * f);
                entity.motionY = 0.3D;
                entity.fallDistance = 0.0F;
            }
        }

    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound p_70014_1_)
    {

    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {}

    public void setSteaming(boolean b)
    {
        this.dataWatcher.updateObject(17, Byte.valueOf((byte) (b ? 1 : 0)));
    }

    public boolean isSteaming()
    {
        return this.dataWatcher.getWatchableObjectByte(17) != 0;
    }


    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

    public void setFire(int sec) {}

}
