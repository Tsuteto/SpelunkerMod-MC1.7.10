package tsuteto.spelunker.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import java.util.List;

/**
 * Defines Flash Luminary, originally "IlluminatingFlare" by ayashige
 *
 * @author ayashige, Tsuteto
 *
 */
public class EntityFlashBullet extends Entity
{
    private int ticksLive = 0;
    public int lightUpX = -1;
    public int lightUpY = -1;
    public int lightUpZ = -1;

    public EntityFlashBullet(World var1)
    {
        super(var1);
        this.noClip = true;
        this.ticksLive = 0;
        this.motionX = 0.0D;
        this.motionY = -0.01D;
        this.motionZ = 0.0D;
    }

    @Override
    protected void entityInit() {}

    public boolean canBePushed()
    {
        return false;
    }

    protected void collideWithEntity(Entity p_82167_1_) {}

    protected void collideWithNearbyEntities() {}

    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void fall(float p_70069_1_) {}

    protected void updateFallState(double p_70064_1_, boolean p_70064_3_) {}

    public boolean doesEntityNotTriggerPressurePlate()
    {
        return true;
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    protected void readEntityFromNBT(NBTTagCompound var1)
    {
        this.lightUpX = var1.getShort("LightX");
        this.lightUpY = var1.getShort("LightY");
        this.lightUpZ = var1.getShort("LightZ");
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    protected void writeEntityToNBT(NBTTagCompound var1)
    {
        var1.setInteger("LightX", this.lightUpX);
        var1.setInteger("LightY", this.lightUpY);
        var1.setInteger("LightZ", this.lightUpZ);
    }

    protected void onImpact(MovingObjectPosition var1)
    {
        if (var1.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)
        {
            var1.entityHit.attackEntityFrom(DamageSource.onFire, 2);
        }
        else
        {
            this.setDead();
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate()
    {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();
        ++this.ticksLive;

        int var8;

        if (!this.worldObj.isRemote)
        {
            if (this.ticksLive == 600)
            {
                this.setDead();
            }

            List<Entity> entitiesNearby = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            for (Entity entity : entitiesNearby)
            {
                entity.attackEntityFrom(DamageSource.onFire, 1);
            }

            Vec3 var1 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            Vec3 var2 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition var3 = this.worldObj.rayTraceBlocks(var1, var2);
            var1 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            var2 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (var3 != null)
            {
                var2 = Vec3.createVectorHelper(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
            }

            Entity var4 = null;
            List var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double var6 = 0.0D;

            for (var8 = 0; var8 < var5.size(); ++var8)
            {
                Entity var9 = (Entity)var5.get(var8);

                if (var9.canBeCollidedWith())
                {
                    float var10 = 0.3F;
                    AxisAlignedBB var11 = var9.boundingBox.expand(var10, var10, var10);
                    MovingObjectPosition var12 = var11.calculateIntercept(var1, var2);

                    if (var12 != null)
                    {
                        double var13 = var1.distanceTo(var12.hitVec);

                        if (var13 < var6 || var6 == 0.0D)
                        {
                            var4 = var9;
                            var6 = var13;
                        }
                    }
                }
            }

            if (var4 != null)
            {
                var3 = new MovingObjectPosition(var4);
            }

            if (var3 != null)
            {
                this.onImpact(var3);
            }

            if (this.posY < 0.0D)
            {
                this.setDead();
            }
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float var15 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        for (this.rotationPitch = (float)(Math.atan2(this.motionY, var15) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
        {
            ;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
        {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F)
        {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
        {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
        float var16 = 0.99F;
        float var17 = this.getGravityVelocity();
        int var7;

        if (this.isInWater())
        {
            for (var7 = 0; var7 < 4; ++var7)
            {
                float var18 = 0.25F;
                this.worldObj.spawnParticle("bubble", this.posX - this.motionX * var18, this.posY - this.motionY * var18, this.posZ - this.motionZ * var18, this.motionX, this.motionY, this.motionZ);
            }

            var16 = 0.8F;
        }

        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = 0.0D;
        this.motionY = -0.01D;
        this.motionZ = 0.0D;
        var7 = (int)this.posX;
        var8 = (int)this.posY;
        int var19 = (int)this.posZ;

        if (this.lightUpY == -1 || var7 != this.lightUpX || var8 != this.lightUpY || var19 != this.lightUpZ)
        {
            if (this.lightUpY != -1)
            {
                this.setLightValue(this.worldObj, this.lightUpX, this.lightUpY, this.lightUpZ, 0);
            }

            if (this.setLightValue(this.worldObj, var7, var8, var19, 15))
            {
                this.lightUpX = var7;
                this.lightUpY = var8;
                this.lightUpZ = var19;
            }
            else
            {
                this.lightUpX = -1;
                this.lightUpY = -1;
                this.lightUpZ = -1;
            }
        }
    }

    /**
     * Will get destroyed next tick.
     */
    @Override
    public void setDead()
    {
        if (this.lightUpY != -1)
        {
            this.setLightValue(this.worldObj, this.lightUpX, this.lightUpY, this.lightUpZ, 0);
        }

        this.isDead = true;
    }

    public boolean setLightValue(World var1, int var2, int var3, int var4, int var5)
    {
        if (var1.getBlock(var2, var3, var4).isAir(var1, var2, var3, var4))
        {
            return false;
        }
        else
        {
            var1.setLightValue(EnumSkyBlock.Block, var2, var3, var4, var5);
            var1.updateLightByType(EnumSkyBlock.Block, var2 - 1, var3, var4);
            var1.updateLightByType(EnumSkyBlock.Block, var2 + 1, var3, var4);
            var1.updateLightByType(EnumSkyBlock.Block, var2, var3 - 1, var4);
            var1.updateLightByType(EnumSkyBlock.Block, var2, var3 + 1, var4);
            var1.updateLightByType(EnumSkyBlock.Block, var2, var3, var4 - 1);
            var1.updateLightByType(EnumSkyBlock.Block, var2, var3, var4 + 1);
            return true;
        }
    }

    /**
     * Gets the amount of gravity to apply to the thrown entity with each tick.
     */
    protected float getGravityVelocity()
    {
        return 0.03F;
    }
}
