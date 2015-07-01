package tsuteto.spelunker.entity;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.List;

import static tsuteto.spelunker.entity.EntityWaterLift.Stat.DOWN;
import static tsuteto.spelunker.entity.EntityWaterLift.Stat.UP;

public class EntityWaterLift extends Entity implements IEntityAdditionalSpawnData, IEntityPlatform
{
    public static final double moveSpeed = 0.08D;

    enum Stat
    {
        UP, DOWN
    }
    public int initPosX;
    public int initPosY;
    public int initPosZ;
    public Stat currStatCl = Stat.UP;
    public int distance;
    private int stillTime;

    @SideOnly(Side.CLIENT)
    private double velocityX;
    @SideOnly(Side.CLIENT)
    private double velocityY;
    @SideOnly(Side.CLIENT)
    private double velocityZ;

    public EntityWaterLift(World p_i1582_1_)
    {
        super(p_i1582_1_);
        this.preventEntitySpawning = true;
        this.noClip = true;
        this.setSize(3F, 3F);
        this.height = 1.0F;
    }

    public EntityWaterLift(World p_i1705_1_, double p_i1705_2_, double p_i1705_4_, double p_i1705_6_)
    {
        this(p_i1705_1_);
        this.setPosition(p_i1705_2_, p_i1705_4_ + (double)this.yOffset, p_i1705_6_);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = p_i1705_2_;
        this.prevPosY = p_i1705_4_;
        this.prevPosZ = p_i1705_6_;
    }

    @Override
    protected void entityInit()
    {
        this.dataWatcher.addObject(17, (byte) 0); // isStill
        this.dataWatcher.addObject(18, (byte) UP.ordinal()); // stat
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound p_70037_1_)
    {
        this.initPosX = p_70037_1_.getInteger("InitPosX");
        this.initPosY = p_70037_1_.getInteger("InitPosY");
        this.initPosZ = p_70037_1_.getInteger("InitPosZ");
        this.distance = p_70037_1_.getByte("Distance");
        this.setStat(p_70037_1_.getByte("Stat"));
        this.stillTime = p_70037_1_.getByte("StillTime");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound p_70014_1_)
    {
        p_70014_1_.setInteger("InitPosX", this.initPosX);
        p_70014_1_.setInteger("InitPosY", this.initPosY);
        p_70014_1_.setInteger("InitPosZ", this.initPosZ);
        p_70014_1_.setByte("Distance", (byte)this.distance);
        p_70014_1_.setByte("Stat", (byte)this.getStat().ordinal());
        p_70014_1_.setByte("StillTime", (byte)this.stillTime);
    }

    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double p_70056_1_, double p_70056_3_, double p_70056_5_, float p_70056_7_, float p_70056_8_, int p_70056_9_)
    {
        this.motionX = this.velocityX;
        this.motionY = this.velocityY;
        this.motionZ = this.velocityZ;
    }

    @SideOnly(Side.CLIENT)
    public void setVelocity(double p_70016_1_, double p_70016_3_, double p_70016_5_)
    {
        this.velocityX = this.motionX = p_70016_1_;
        this.velocityY = this.motionY = p_70016_3_;
        this.velocityZ = this.motionZ = p_70016_5_;
    }

    protected boolean canTriggerWalking()
    {
        return true;
    }

    public AxisAlignedBB getCollisionBox(Entity p_70114_1_)
    {
        return p_70114_1_.boundingBox;
    }

    public AxisAlignedBB getBoundingBox()
    {
        return this.boundingBox;
    }

    public boolean canBePushed()
    {
        return false;
    }

    public boolean canBeCollidedWith()
    {
        return true;
    }

    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_)
    {
        boolean flag = p_70097_1_.getEntity() instanceof EntityPlayer && ((EntityPlayer)p_70097_1_.getEntity()).capabilities.isCreativeMode;

        if (flag)
        {
            this.setDead();
        }
        return false;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        Stat currStat = this.getStat();
        if (currStat == DOWN && this.posY <= this.initPosY)
        {
            setStat(UP);
            stillTime = 60;
            this.setPosition(this.posX, this.initPosY, this.posZ);
        }
        if (currStat == UP && this.posY >= this.initPosY + distance)
        {
            setStat(DOWN);
            stillTime = 60;
            this.setPosition(this.posX, this.initPosY + distance, this.posZ);
        }

        if (this.worldObj.isRemote)
        {
            if (!this.isStill() && this.isSyncInStat())
            {
                // Attempting to sync
                if (stillTime > 0 && this.getStat() == DOWN)
                {
                    this.setPosition(this.posX, this.initPosY + distance, this.posZ);
                }

                stillTime = 0;
            }
        }
        else
        {
            if (stillTime > 0)
            {
                this.setStill(true);
                stillTime--;
            }
            else
            {
                this.setStill(false);
            }
        }

        //ModLog.debug("waterlift: %.3f [%s]", this.posY, (worldObj.isRemote ? "CLIENT" : "SERVER"));

        double speed = 0.0D;
        int waterX = initPosX;
        int waterY = (int) this.posY;
        int waterZ = initPosZ;
        if (stillTime == 0)
        {
            if (this.getStat() == UP)
            {
                speed = moveSpeed;
                if (waterY >= this.initPosY && this.worldObj.isAirBlock(waterX, waterY, waterZ))
                {
                    this.worldObj.setBlock(waterX, waterY, waterZ, Blocks.water);
                }
            }
            else if (this.getStat() == DOWN)
            {
                speed = -moveSpeed;
                if (waterY >= this.initPosY && this.worldObj.getBlock(waterX, waterY, waterZ) == Blocks.water)
                {
                    this.worldObj.setBlockToAir(waterX, waterY , waterZ);
                }
            }
        }

        if (this.worldObj.isRemote)
        {
            this.setVelocity(0.0D, speed, 0.0D);
        }
        else
        {
            this.motionX = 0.0D;
            this.motionY = speed;
            this.motionZ = 0.0D;
        }

        if (speed != 0.0D)
        {
            List list1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(-0.1D, 0.25D, -0.1D));
            //ModLog.debug("list: " + list1.size());
            for (Object obj : list1)
            {
                Entity entity = (Entity) obj;
                entity.motionY = Math.max(this.motionY, entity.motionY);
                entity.fallDistance = 0.0f;
                entity.onGround = true;
            }

            this.moveEntity(this.motionX, this.motionY, this.motionZ);
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
    }

    @Override
    public void setDead()
    {
        super.setDead();

        if (!this.worldObj.isRemote)
        {
            for (int y = (int) this.posY; y >= this.initPosY; y--)
            {
                Block b = this.worldObj.getBlock(initPosX, y, initPosZ);
                if (b == Blocks.water || b == Blocks.flowing_water)
                {
                    this.worldObj.setBlockMetadataWithNotify(initPosX, y, initPosZ, 8, 3);
                }
            }
        }
    }

    public void setStat(Stat val)
    {
        if (worldObj.isRemote)
        {
            this.currStatCl = val;
        }
        else
        {
            this.dataWatcher.updateObject(18, (byte)val.ordinal());
        }
    }

    public void setStat(int val)
    {
        if (worldObj.isRemote)
        {
            this.currStatCl = Stat.values()[val];
        }
        else
        {
            this.dataWatcher.updateObject(18, (byte)val);
        }
    }

    public Stat getStat()
    {
        if (worldObj.isRemote)
        {
            return this.currStatCl;
        }
        else
        {
            return Stat.values()[this.dataWatcher.getWatchableObjectByte(18)];
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean isSyncInStat()
    {
        return this.currStatCl == Stat.values()[this.dataWatcher.getWatchableObjectByte(18)];
    }

    public void setStill(boolean val)
    {
        this.dataWatcher.updateObject(17, (byte) (val ? 1 : 0));
    }

    public boolean isStill()
    {
        return this.dataWatcher.getWatchableObjectByte(17) != 0;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        buffer.writeFloat(((float)this.motionX));
        buffer.writeFloat(((float)this.motionY));
        buffer.writeFloat(((float)this.motionZ));

        buffer.writeInt(this.initPosX);
        buffer.writeInt(this.initPosY);
        buffer.writeInt(this.initPosZ);
        buffer.writeByte(this.distance);
        buffer.writeByte(this.currStatCl.ordinal());
        buffer.writeByte(this.stillTime);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData)
    {
        this.motionX = additionalData.readFloat();
        this.motionY = additionalData.readFloat();
        this.motionZ = additionalData.readFloat();
        setVelocity(this.motionX, this.motionY, this.motionZ);

        this.initPosX = additionalData.readInt();
        this.initPosY = additionalData.readInt();
        this.initPosZ = additionalData.readInt();
        this.distance = additionalData.readByte();
        this.currStatCl = Stat.values()[additionalData.readByte()];
        this.stillTime = additionalData.readByte();
    }
}
