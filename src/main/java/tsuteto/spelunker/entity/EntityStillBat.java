package tsuteto.spelunker.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import tsuteto.spelunker.block.SpelunkerBlocks;

public class EntityStillBat extends EntityAmbientCreature
{
    public ChunkCoordinates homePos;
    private ChunkCoordinates targetPos;

    private BatDroppingsHandler droppingsHandler = BatDroppingsHandler.forStillBats();

    public EntityStillBat(World p_i1680_1_)
    {
        super(p_i1680_1_);
        this.setSize(0.5F, 0.9F);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
    }

    protected float getSoundVolume()
    {
        return 0.4F;
    }

    protected float getSoundPitch()
    {
        return super.getSoundPitch() * 0.95F;
    }

    protected String getLivingSound()
    {
        return "mob.bat.idle";
    }

    protected String getHurtSound()
    {
        return "mob.bat.hurt";
    }

    protected String getDeathSound()
    {
        return "mob.bat.death";
    }

    public boolean canBePushed()
    {
        return false;
    }

    protected void collideWithEntity(Entity p_82167_1_) {}

    protected void collideWithNearbyEntities() {}

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(6.0D);
    }

    protected boolean isAIEnabled()
    {
        return true;
    }

    public void onUpdate()
    {
        super.onUpdate();
        this.motionY *= 0.6000000238418579D;
        droppingsHandler.onEntityUpdate(this);
    }

    protected void updateAITasks()
    {
        super.updateAITasks();

        if (this.homePos == null)
        {
            this.setDead();
        }

        if (this.targetPos == null || this.rand.nextInt(10) == 0 || this.targetPos.getDistanceSquared((int)this.posX, (int)this.posY, (int)this.posZ) < 1.0F)
        {
            this.targetPos = new ChunkCoordinates(
                    this.homePos.posX + this.rand.nextInt(2) - this.rand.nextInt(2),
                    this.homePos.posY,
                    this.homePos.posZ + this.rand.nextInt(2) - this.rand.nextInt(2));
        }

        double d0 = (double)this.targetPos.posX + 0.5D - this.posX;
        double d1 = (double)this.targetPos.posY + 0.1D - this.posY;
        double d2 = (double)this.targetPos.posZ + 0.5D - this.posZ;
        this.motionX += (Math.signum(d0) * 0.5D - this.motionX) * 0.1D;
        this.motionY += (Math.signum(d1) * 0.7D - this.motionY) * 0.1D;
        this.motionZ += (Math.signum(d2) * 0.5D - this.motionZ) * 0.1D;
        float f = (float)(Math.atan2(this.motionZ, this.motionX) * 180.0D / Math.PI) - 90.0F;
        float f1 = MathHelper.wrapAngleTo180_float(f - this.rotationYaw);
        this.moveForward = 0.5F;
        this.rotationYaw += f1;
    }

    @Override
    public void onDeath(DamageSource p_70645_1_)
    {
        super.onDeath(p_70645_1_);

        if (!this.worldObj.isRemote)
        {
            this.worldObj.scheduleBlockUpdate(homePos.posX, homePos.posY, homePos.posZ, SpelunkerBlocks.blockBatSpawner, 100);
        }
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float p_70069_1_) {}

    /**
     * Takes in the distance the entity has fallen this tick and whether its on the ground to update the fall distance
     * and deal fall damage if landing on the ground.  Args: distanceFallenThisTick, onGround
     */
    protected void updateFallState(double p_70064_1_, boolean p_70064_3_) {}

    /**
     * Return whether this entity should NOT trigger a pressure plate or a tripwire.
     */
    public boolean doesEntityNotTriggerPressurePlate()
    {
        return true;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else
        {
            return super.attackEntityFrom(p_70097_1_, p_70097_2_);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound p_70037_1_)
    {
        super.readEntityFromNBT(p_70037_1_);

        this.homePos = new ChunkCoordinates(
                p_70037_1_.getInteger("homeX"),
                p_70037_1_.getInteger("homeY"),
                p_70037_1_.getInteger("homeZ"));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound p_70014_1_)
    {
        super.writeEntityToNBT(p_70014_1_);
        p_70014_1_.setInteger("homeX", this.homePos.posX);
        p_70014_1_.setInteger("homeY", this.homePos.posY);
        p_70014_1_.setInteger("homeZ", this.homePos.posZ);
    }
}