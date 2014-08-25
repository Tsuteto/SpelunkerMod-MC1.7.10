package tsuteto.spelunker.entity;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import tsuteto.spelunker.item.EnumGunMaterial;

import java.util.List;

/**
 * Defines Blaster Bullets, referred to EntityFN5728SS190 by MMM
 *
 * @author Tsuteto
 *
 */
public class EntityGunBullet extends Entity implements IProjectile, IEntityAdditionalSpawnData
{
    protected int xTile;
    protected int yTile;
    protected int zTile;
    protected Block inTile;
    protected boolean inGround = false;
    protected int ticksInGround;
    protected int ticksInAir;
    public Entity shootingEntity;
    private double damageMultiplier;
    private int knockback;
    private EnumGunMaterial gunmaterial;

    public EntityGunBullet(World world)
    {
    	super(world);
        xTile = -1;
        yTile = -1;
        zTile = -1;
        inTile = Blocks.air;
        ticksInAir = 0;
		ticksInGround = 0;
		yOffset = 0.0F;
        damageMultiplier = 1D;
        knockback = 0;
    }

    /**
     * Constructor for multi-player client
     * @param world
     * @param px
     * @param py
     * @param pz
     */
	public EntityGunBullet(World world, double px, double py, double pz)
	{
		this(world);
        gunmaterial = EnumGunMaterial.CLIENT;
		this.setLocationAndAngles(px, py, pz, 0F, 0F);
	}

	/**
	 * Constructor for single-player client
	 * @param world
	 * @param px
	 * @param py
	 * @param pz
	 * @param par3Gunmaterial
	 */
	public EntityGunBullet(World world, double px, double py, double pz, EnumGunMaterial par3Gunmaterial)
	{
		this(world);
        gunmaterial = par3Gunmaterial;
		this.setLocationAndAngles(px, py, pz, 0F, 0F);
	}

	/**
	 * Constructor for single-player server
	 * @param par1World
	 * @param par2EntityLiving
	 * @param par3Gunmaterial
	 * @param f
	 */
    public EntityGunBullet(World par1World, EntityLivingBase par2EntityLiving, EnumGunMaterial par3Gunmaterial, float f)
    {
    	super(par1World);

        this.gunmaterial = par3Gunmaterial;
        this.shootingEntity = par2EntityLiving;
        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(par2EntityLiving.posX, par2EntityLiving.posY + par2EntityLiving.getEyeHeight(), par2EntityLiving.posZ, par2EntityLiving.rotationYaw, par2EntityLiving.rotationPitch);
        this.posX -= (MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        this.motionX = (-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionZ = (MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionY = (-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
        this.setThrowableHeading(motionX, motionY, motionZ, par3Gunmaterial.getBulletSpeed(), f);
	}

    @Override
    protected void entityInit()
    {
    }

    @Override
    public void setThrowableHeading(double par1, double par3, double par5, float par7, float par8)
    {
        float f2 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
        par1 /= f2;
        par3 /= f2;
        par5 /= f2;
        par1 += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * par8;
        par3 += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * par8;
        par5 += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * par8;
        par1 *= par7;
        par3 *= par7;
        par5 *= par7;
        this.motionX = par1;
        this.motionY = par3;
        this.motionZ = par5;
        float f3 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(par1, par5) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(par3, f3) * 180.0D / Math.PI);
        this.ticksInGround = 0;
	}

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
        this.setPosition(par1, par3, par5);
        this.setRotation(par7, par8);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setVelocity(double par1, double par3, double par5)
    {
        this.motionX = par1;
        this.motionY = par3;
        this.motionZ = par5;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(par1, par5) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(par3, f) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(this.motionY, f) * 180.0D / Math.PI);
        }

        Block i = worldObj.getBlock(xTile, yTile, zTile);

        if (i.isAir(worldObj, xTile, yTile, zTile))
        {
            i.setBlockBoundsBasedOnState(worldObj, xTile, yTile, zTile);
            AxisAlignedBB axisalignedbb = i.getCollisionBoundingBoxFromPool(worldObj, xTile, yTile, zTile);

            // Hit blocks
            if (axisalignedbb != null && axisalignedbb.isVecInside(Vec3.createVectorHelper(posX, posY, posZ)))
            {
            	inGround = true;
            	setDead();
            }
        }

        // Check collision
        ticksInAir++;
        Vec3 var16 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        Vec3 var2 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition var3 = this.worldObj.rayTraceBlocks(var16, var2);
        var16 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        var2 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if (var3 != null)
        {
            var2 = Vec3.createVectorHelper(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
        }

        if (!this.worldObj.isRemote)
        {
            Entity var4 = null;
            List var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double var6 = 0.0D;

            for (int var9 = 0; var9 < var5.size(); ++var9)
            {
                Entity var10 = (Entity)var5.get(var9);

                if (gunmaterial == EnumGunMaterial.ORIGINAL && !(var10 instanceof EntityGhast)) {
                    continue;
                }

                if (var10.canBeCollidedWith() && (var10 != this.shootingEntity || this.ticksInAir >= 5))
                {
                    float var11 = 0.3F;
                    AxisAlignedBB var12 = var10.boundingBox.expand(var11, var11, var11);
                    MovingObjectPosition var13 = var12.calculateIntercept(var16, var2);

                    if (var13 != null)
                    {
                        double var14 = var16.distanceTo(var13.hitVec);

                        if (var14 < var6 || var6 == 0.0D)
                        {
                            var4 = var10;
                            var6 = var14;
                        }
                    }
                }
            }

            if (var4 != null)
            {
                var3 = new MovingObjectPosition(var4);
            }
        }

        if (var3 != null)
        {
            if (var3.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.worldObj.getBlock(var3.blockX, var3.blockY, var3.blockZ) == Blocks.portal)
            {
                this.setInPortal();
            }
            else
            {
                this.onImpact(var3);
            }
        }

        // Reach the limit of reach
        if (!worldObj.isRemote && ticksInAir > gunmaterial.getBulletReach())
        {
        	setDead();
        }

        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        float f3 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
        rotationYaw = (float)(Math.atan2(motionX, motionZ) * 180D / Math.PI);

        for (this.rotationPitch = (float)(Math.atan2(this.motionY, f3) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
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

        rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
        rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
        //float f4 = 0.99F;
        //float f6 = 0.0F;

        if (isInWater())
        {
            for (int k1 = 0; k1 < 4; k1++)
            {
                float f8 = 0.25F;
                worldObj.spawnParticle("bubble", posX - motionX * f8, posY - motionY * f8, posZ - motionZ * f8, motionX, motionY, motionZ);
            }

            //f4 = 0.8F;
        }

        // Fly straight
        //motionX *= f4;
        //motionY *= f4;
        //motionZ *= f4;
        //motionY -= f6;
        setPosition(posX, posY, posZ);
        //System.out.println(String.format("%d: %.2f, %.2f, %.2f", ticksInAir, posX, posY, posZ));
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    @Override
    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
    {
    }

	protected void onImpact(MovingObjectPosition movingobjectposition) {
        if (movingobjectposition.entityHit != null)
        {
            int damageAmount = (int)Math.ceil(gunmaterial.getDamageVsEntity() * (damageMultiplier / 10D + 1));
            double prevEntityMotionX = movingobjectposition.entityHit.motionX;
            double prevEntityMotionY = movingobjectposition.entityHit.motionY;
            double prevEntityMotionZ = movingobjectposition.entityHit.motionZ;
            DamageSource damagesource = DamageSource.causeThrownDamage(this, this.shootingEntity);

            if (isBurning())
            {
                movingobjectposition.entityHit.setFire(5);
            }

            if (movingobjectposition.entityHit.attackEntityFrom(damagesource, damageAmount))
            {
                if (movingobjectposition.entityHit instanceof EntityLiving)
                {
                    EntityLiving var24 = (EntityLiving)movingobjectposition.entityHit;
                    var24.setArrowCountInEntity(var24.getArrowCountInEntity() + 1);

                    movingobjectposition.entityHit.motionX = prevEntityMotionX;
                    movingobjectposition.entityHit.motionY = prevEntityMotionY;
                    movingobjectposition.entityHit.motionZ = prevEntityMotionZ;

                    if (knockback > 0)
                    {
                        float f7 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
                        if (f7 > 0.0F)
                        {
                        	// Knock back
                            movingobjectposition.entityHit.addVelocity((motionX * knockback * 0.6D) / f7, 0.1D, (motionZ * knockback * 0.6D) / f7);
                        }
                    }
                }
            }
            else
            {
            	// Hit entities which refuse damage
            }
        }
        else
        {
        	// Hit blocks
            xTile = movingobjectposition.blockX;
            yTile = movingobjectposition.blockY;
            zTile = movingobjectposition.blockZ;
            inTile = worldObj.getBlock(xTile, yTile, zTile);
//            inData = worldObj.getBlockMetadata(xTile, yTile, zTile);
            motionX = (float)(movingobjectposition.hitVec.xCoord - posX);
            motionY = (float)(movingobjectposition.hitVec.yCoord - posY);
            motionZ = (float)(movingobjectposition.hitVec.zCoord - posZ);
            float f2 = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
            double d1 = 0.05000000074505806D;
            posX -= (motionX / f2) * d1;
            posY -= (motionY / f2) * d1;
            posZ -= (motionZ / f2) * d1;
            posX += motionX;
            posY += motionY;
            posZ += motionZ;
            this.ticksInAir = 0;
        }

        for(int i = 0; i < 4; i++)
        {
            worldObj.spawnParticle("snowballpoof", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        }

        //System.out.println(String.format("[Hit] %d: %.2f, %.2f, %.2f", ticksInAir, posX, posY, posZ));
        inGround = true;

        if (!worldObj.isRemote)
        {
        	setDead();
        }
	}

    public double getDamageMultiplier()
    {
        return damageMultiplier;
    }
    public void setDamageMultiplier(double d)
    {
        damageMultiplier = d;
    }
    public void setKnockBack(int i)
    {
        knockback = i;
    }

    public EnumGunMaterial getGunMaterial()
    {
    	return gunmaterial;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setShort("xTile", (short)this.xTile);
        par1NBTTagCompound.setShort("yTile", (short)this.yTile);
        par1NBTTagCompound.setShort("zTile", (short)this.zTile);
        par1NBTTagCompound.setShort("inTile", (short) Block.getIdFromBlock(this.inTile));
        par1NBTTagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
        par1NBTTagCompound.setString("material", gunmaterial.toString());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        this.xTile = par1NBTTagCompound.getShort("xTile");
        this.yTile = par1NBTTagCompound.getShort("yTile");
        this.zTile = par1NBTTagCompound.getShort("zTile");
        this.inTile = Block.getBlockById(par1NBTTagCompound.getShort("inTile"));
        this.inGround = par1NBTTagCompound.getByte("inGround") == 1;
        gunmaterial = EnumGunMaterial.valueOf(par1NBTTagCompound.getString("material"));
    }

    @Override
    public void writeSpawnData(ByteBuf data)
    {
        data.writeInt(this.shootingEntity == null ? this.getEntityId() : shootingEntity.getEntityId());
        data.writeInt(Float.floatToIntBits((float)motionX));
        data.writeInt(Float.floatToIntBits((float)motionY));
        data.writeInt(Float.floatToIntBits((float)motionZ));
        data.writeByte(gunmaterial.ordinal());
    }

    @Override
    public void readSpawnData(ByteBuf data)
    {
        int lthrower = data.readInt();
        if (lthrower != 0) {
            Entity lentity = worldObj.getEntityByID(lthrower);
            if (lentity instanceof EntityLiving) {
                this.shootingEntity = lentity;
            }
        }
        motionX = Float.intBitsToFloat(data.readInt());
        motionY = Float.intBitsToFloat(data.readInt());
        motionZ = Float.intBitsToFloat(data.readInt());
        setVelocity(motionX, motionY, motionZ);
        gunmaterial = EnumGunMaterial.values()[data.readByte()];
    }
}
