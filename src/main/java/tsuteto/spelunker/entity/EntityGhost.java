package tsuteto.spelunker.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.eventhandler.GhostSpawnHandler;
import tsuteto.spelunker.player.SpelunkerPlayerMP;
import tsuteto.spelunker.util.PlayerUtils;

public class EntityGhost extends EntityCreature implements IMob
{
    public enum Type
    {
        NORMAL(1.0F, 0.05F), TOUGH(3.0F, 0.05F), MASS(3.0F, 0.05F);

        public float scale;
        public float speed;

        Type(float scale, float speed)
        {
            this.scale = scale;
            this.speed = speed;
        }
    }

    public EntityGhost(World p_i1738_1_)
    {
        super(p_i1738_1_);
        this.noClip = true;
        this.setSize(3F, 3F);
        if (!p_i1738_1_.isRemote)
        {
            GhostSpawnHandler.ghostList.add(this);
        }
    }

    public EntityGhost(World world, double x, double y, double z)
    {
        this(world);
        this.setPosition(x, y, z);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
    }

    protected boolean isAIEnabled()
    {
        return true;
    }

    public boolean canBePushed()
    {
        return false;
    }

    protected void collideWithEntity(Entity p_82167_1_) {}

    protected void collideWithNearbyEntities() {}

    public void onUpdate()
    {
        super.onUpdate();
    }

    protected void updateAITasks()
    {
        super.updateAITasks();

        if (this.entityToAttack == null)
        {
            this.entityToAttack = this.findPlayerToAttack();

        }
        else if (this.entityToAttack.isEntityAlive())
        {
            float f = this.entityToAttack.getDistanceToEntity(this);

            if (this.canEntityBeSeen(this.entityToAttack))
            {
                this.attackEntity(this.entityToAttack, f);
            }
        }
        else
        {
            this.entityToAttack = null;
        }

        if (this.entityToAttack instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP)this.entityToAttack;
            SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(player);
            if (!spelunker.isInCave() || player.theItemInWorldManager.isCreative())
            {
                this.entityToAttack = null;
            }
        }

        if (this.entityToAttack != null)
        {
            double d0 = this.entityToAttack.posX + 0.5D - this.posX;
            double d1 = this.entityToAttack.posY + 0.1D - this.posY;
            double d2 = this.entityToAttack.posZ + 0.5D - this.posZ;
            this.motionX += (Math.signum(d0) * 0.5D - this.motionX) * 0.01D;
            this.motionY += (Math.signum(d1) * 0.5D - this.motionY) * 0.02D;
            this.motionZ += (Math.signum(d2) * 0.5D - this.motionZ) * 0.01D;

            float f = (float)(Math.atan2(this.motionZ, this.motionX) * 180.0D / Math.PI) - 90.0F;
            float f1 = MathHelper.wrapAngleTo180_float(f - this.rotationYaw);
            this.moveForward = this.getType().speed;
            this.rotationYaw += f1;
        }
    }

    protected void attackEntity(Entity p_70785_1_, float p_70785_2_)
    {
        if (this.attackTime <= 0 && p_70785_2_ < 2.0F && p_70785_1_.boundingBox.maxY > this.boundingBox.minY && p_70785_1_.boundingBox.minY < this.boundingBox.maxY)
        {
            this.attackTime = 20;
            this.attackEntityAsMob(p_70785_1_);
        }
    }

    protected EntityPlayer findPlayerToAttack()
    {
        EntityPlayer entityplayer = PlayerUtils.getClosestVulnerableSpelunkerToEntity(this, 32.0D);
        return entityplayer;
    }

    @Override
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_)
    {
        if (p_70097_1_.isProjectile() || p_70097_1_.canHarmInCreative())
        {
            return super.attackEntityFrom(p_70097_1_, p_70097_2_);
        }
        else
        {
            return false;
        }
    }

    @Override
    public void setDead()
    {
        super.setDead();
        if (!worldObj.isRemote)
        {
            GhostSpawnHandler.ghostList.remove(this);
        }
    }

    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
    {
//        int j = this.rand.nextInt(2 + p_70628_2_);
//        int k;
//
//        for (k = 0; k < j; ++k)
//        {
//            this.dropItem(Items., 1);
//        }
    }

    public boolean getCanSpawnHere()
    {
        return this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL
                && super.getCanSpawnHere();
    }

    public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_)
    {
        if (this.isInWater())
        {
            this.moveFlying(p_70612_1_, p_70612_2_, 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
        }
        else if (this.handleLavaMovement())
        {
            this.moveFlying(p_70612_1_, p_70612_2_, 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        }
        else
        {
            float f2 = 0.91F;

            if (this.onGround)
            {
                f2 = this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ)).slipperiness * 0.91F;
            }

            float f3 = 0.16277136F / (f2 * f2 * f2);
            this.moveFlying(p_70612_1_, p_70612_2_, this.onGround ? 0.1F * f3 : 0.02F);
            f2 = 0.91F;

            if (this.onGround)
            {
                f2 = this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ)).slipperiness * 0.91F;
            }

            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double)f2;
            this.motionY *= (double)f2;
            this.motionZ *= (double)f2;
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d1 = this.posX - this.prevPosX;
        double d0 = this.posZ - this.prevPosZ;
        float f4 = MathHelper.sqrt_double(d1 * d1 + d0 * d0) * 4.0F;

        if (f4 > 1.0F)
        {
            f4 = 1.0F;
        }

        this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

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

    public void setType(Type type)
    {
        this.dataWatcher.updateObject(16, Byte.valueOf((byte)type.ordinal()));
    }

    public Type getType()
    {
        return Type.values()[this.dataWatcher.getWatchableObjectByte(16)];
    }
}
