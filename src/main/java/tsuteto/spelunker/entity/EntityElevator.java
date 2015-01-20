package tsuteto.spelunker.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import tsuteto.spelunker.item.SpelunkerItem;
import tsuteto.spelunker.network.PacketDispatcher;
import tsuteto.spelunker.network.packet.PacketElevatorControlCl;
import tsuteto.spelunker.network.packet.PacketElevatorControlSv;
import tsuteto.spelunker.util.ModLog;

import java.util.List;

public class EntityElevator extends Entity
{
    public static final double moveSpeed = 0.2D;
    public static int MODE_STILL = 0;
    public static int MODE_UP = 1;
    public static int MODE_DOWN = 2;
    @SideOnly(Side.CLIENT)
    private double velocityX;
    @SideOnly(Side.CLIENT)
    private double velocityY;
    @SideOnly(Side.CLIENT)
    private double velocityZ;

    public EntityPlayer controlledBy = null;
    private double moveVelocity = 0.0D;
    private int prevMode = 0;

    public EntityElevator(World p_i1704_1_)
    {
        super(p_i1704_1_);
        this.preventEntitySpawning = true;
        this.setSize(3F, 4F);
        this.height = 0.2F;
    }

    public EntityElevator(World p_i1705_1_, double p_i1705_2_, double p_i1705_4_, double p_i1705_6_)
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

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return true;
    }

    protected void entityInit()
    {
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(1));
        this.dataWatcher.addObject(19, new Float(0.0F));
        this.dataWatcher.addObject(20, Byte.valueOf((byte) 0)); // EntityInvulnerable
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
        else if (!this.worldObj.isRemote && !this.isDead)
        {
            this.setForwardDirection(-this.getForwardDirection());
            this.setTimeSinceHit(10);
            this.setDamageTaken(this.getDamageTaken() + p_70097_2_ * 10.0F);
            this.setBeenAttacked();
            boolean flag = p_70097_1_.getEntity() instanceof EntityPlayer && ((EntityPlayer)p_70097_1_.getEntity()).capabilities.isCreativeMode;

            if (flag || this.getDamageTaken() > 40.0F)
            {
                if (!flag)
                {
                    this.entityDropItem(new ItemStack(SpelunkerItem.itemEntityPlacer, 1, EntityList.getEntityID(this)), 0.0F);
                }

                this.setDead();
            }

            return true;
        }
        else
        {
            return true;
        }
    }

    /**
     * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
     */
    @SideOnly(Side.CLIENT)
    public void performHurtAnimation()
    {
        this.setForwardDirection(-this.getForwardDirection());
        this.setTimeSinceHit(10);
        this.setDamageTaken(this.getDamageTaken() * 11.0F);
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return true;
    }

    /**
     * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
     * posY, posZ, yaw, pitch
     */
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double p_70056_1_, double p_70056_3_, double p_70056_5_, float p_70056_7_, float p_70056_8_, int p_70056_9_)
    {
//        if (this.isBoatEmpty)
//        {
//            this.boatPosRotationIncrements = p_70056_9_ + 5;
//        }
//        else
//        {
//            double d3 = p_70056_1_ - this.posX;
//            double d4 = p_70056_3_ - this.posY;
//            double d5 = p_70056_5_ - this.posZ;
//            double d6 = d3 * d3 + d4 * d4 + d5 * d5;
//
//            if (d6 <= 1.0D)
//            {
//                return;
//            }
//
//            this.boatPosRotationIncrements = 3;
//        }
//
//        this.boatX = p_70056_1_;
//        this.boatY = p_70056_3_;
//        this.boatZ = p_70056_5_;
//        this.boatYaw = (double)p_70056_7_;
//        this.boatPitch = (double)p_70056_8_;
        this.motionX = this.velocityX;
        this.motionY = this.velocityY;
        this.motionZ = this.velocityZ;
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    @SideOnly(Side.CLIENT)
    public void setVelocity(double p_70016_1_, double p_70016_3_, double p_70016_5_)
    {
        this.velocityX = this.motionX = p_70016_1_;
        this.velocityY = this.motionY = p_70016_3_;
        this.velocityZ = this.motionZ = p_70016_5_;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.getTimeSinceHit() > 0)
        {
            this.setTimeSinceHit(this.getTimeSinceHit() - 1);
        }

        if (this.getDamageTaken() > 0.0F)
        {
            this.setDamageTaken(this.getDamageTaken() - 1.0F);
        }

        if (!this.worldObj.isRemote)
        {
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(0.0D, 1.0D, 0.0D));
            boolean isControllerNeeded = this.controlledBy == null || !list.contains(this.controlledBy);

            if (isControllerNeeded)
            {
                // Release controller
                if (this.controlledBy != null)
                {
                    PacketDispatcher.packet(new PacketElevatorControlCl(this.getEntityId(), this.controlledBy.getEntityId(), false))
                            .sendToAllInDimension(this.dimension);
                    this.controlledBy = null;
                    ModLog.debug("Elevator disconnected from server");
                }

                // Allocate new controller
                for (Object obj : list)
                {
                    if (obj instanceof EntityPlayer)
                    {
                        this.controlledBy = (EntityPlayer) obj;
                        PacketDispatcher.packet(new PacketElevatorControlCl(this.getEntityId(), this.controlledBy.getEntityId(), true))
                                .sendToAllInDimension(this.dimension);
                        ModLog.debug("Elevator connected on server");
                        break;
                    }
                }
            }
        }
        else
        {
            this.controlOnClient();
        }


        if (this.controlledBy == null)
        {
            this.setNeutral();
        }

        if (this.worldObj.isRemote)
        {
            this.setVelocity(0.0D, moveVelocity, 0.0D);
        }
        else
        {
            this.motionX = 0.0D;
            this.motionY = moveVelocity;
            this.motionZ = 0.0D;
        }

        if (this.motionY != 0.0D)
        {

            List list1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(0.1D, Math.abs(this.motionY), 0.1D));

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

    public void moveUp()
    {
        moveVelocity = moveSpeed;
    }

    public void moveDown()
    {
        moveVelocity = -moveSpeed;
    }

    public void setNeutral()
    {
        moveVelocity = 0.0D;
    }

    @SideOnly(Side.CLIENT)
    private void controlOnClient()
    {
        if (this.controlledBy != null && this.controlledBy instanceof EntityPlayerSP)
        {
            //ModLog.debug(player.movementInput.moveForward);
            EntityPlayerSP player = (EntityPlayerSP)this.controlledBy;
            int mode;
            if (player.isSneaking())
            {
                if (player.movementInput.moveForward > 0.1f)
                {
                    mode = MODE_UP;
                } else if (player.movementInput.moveForward < -0.1f)
                {
                    mode = MODE_DOWN;
                } else
                {
                    mode = MODE_STILL;
                }

                player.setVelocity(0.0D, player.motionY, 0.0D);
            }
            else
            {
                mode = MODE_STILL;
            }

            if (mode == MODE_STILL) this.setNeutral();
            if (mode == MODE_UP) this.moveUp();
            if (mode == MODE_DOWN) this.moveDown();
            if (mode != this.prevMode)
            {
                PacketDispatcher.packet(new PacketElevatorControlSv(this.getEntityId(), mode)).sendToServer();
                this.prevMode = mode;
            }
        }
    }

    public void syncPosition(double newPosY)
    {
        double diff = newPosY - this.posY;
        if (Math.abs(diff) < 0.5f) return;
        ModLog.debug("Synced Position");

        List list1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(0.1D, Math.abs(this.motionY), 0.1D));

        for (Object obj : list1)
        {
            Entity entity = (Entity) obj;
            entity.posY += diff;
            entity.fallDistance = 0.0f;
            entity.onGround = true;
        }

        super.setPosition(this.posX, newPosY, this.posZ);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {}

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {}

    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

    public void setFire(int sec) {}

    @Override
    public boolean isEntityInvulnerable()
    {
        return super.isEntityInvulnerable() || this.dataWatcher.getWatchableObjectByte(20) != 0;
    }

    public void setEntityInvulnerable(boolean b)
    {
        this.dataWatcher.updateObject(20, Byte.valueOf((byte) (b ? 1 : 0)));
    }

    /**
     * Sets the damage taken from the last hit.
     */
    public void setDamageTaken(float p_70266_1_)
    {
        this.dataWatcher.updateObject(19, Float.valueOf(p_70266_1_));
    }

    /**
     * Gets the damage taken from the last hit.
     */
    public float getDamageTaken()
    {
        return this.dataWatcher.getWatchableObjectFloat(19);
    }

    /**
     * Sets the time to count down from since the last time entity was hit.
     */
    public void setTimeSinceHit(int p_70265_1_)
    {
        this.dataWatcher.updateObject(17, Integer.valueOf(p_70265_1_));
    }

    /**
     * Gets the time since the last hit.
     */
    public int getTimeSinceHit()
    {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    /**
     * Sets the forward direction of the entity.
     */
    public void setForwardDirection(int p_70269_1_)
    {
        this.dataWatcher.updateObject(18, Integer.valueOf(p_70269_1_));
    }

    /**
     * Gets the forward direction of the entity.
     */
    public int getForwardDirection()
    {
        return this.dataWatcher.getWatchableObjectInt(18);
    }
}
