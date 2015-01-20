package tsuteto.spelunker.entity;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tsuteto.spelunker.item.ItemEntityPlacer;

import java.util.List;

public class EntitySteamHole extends Entity implements IEntityAdditionalSpawnData
{
    public static final int steamDuration = 60;
    public static final int cycleTicks = 200;

    public ForgeDirection dir;
    public int tileX;
    public int tileY;
    public int tileZ;

    public boolean isSteamable;
    public int ticksOffset;

    public EntitySteamHole(World p_i1704_1_)
    {
        super(p_i1704_1_);
        this.setSize(0.25F, 0.25F); // 0.375F, 0.25F
        this.preventEntitySpawning = true;
        this.noClip = true;
    }

    public void setTilePosition(double x, double y, double z)
    {
        this.tileX = MathHelper.floor_double(x);
        this.tileY = MathHelper.floor_double(y);
        this.tileZ = MathHelper.floor_double(z);
    }

    public void determinOrientation(ForgeDirection dir)
    {
        this.dir = dir;
        switch (dir)
        {
            case UP:
                this.setPositionAndRotation(this.tileX + 0.5D, this.tileY, this.tileZ + 0.5D, 0.0F, 0.0F);
                break;
            case DOWN:
                this.setPositionAndRotation(this.tileX + 0.5D, this.tileY + 1.0F - this.height, this.tileZ + 0.5D, 0.0F, 180.0F);
                break;
            case NORTH:
                this.setPositionAndRotation(this.tileX + 0.5D, this.tileY + 0.5D - this.height / 2.0D, this.tileZ + 1.0D - this.height / 2.0D, -180.0F, 90.0F);
                break;
            case SOUTH:
                this.setPositionAndRotation(this.tileX + 0.5D, this.tileY + 0.5D - this.height / 2.0D, this.tileZ + this.height / 2.0D, 180.0F, -90.0F);
                break;
            case WEST:
                this.setPositionAndRotation(this.tileX + 1.0D - this.height / 2.0D, this.tileY + 0.5D - this.height / 2.0D, this.tileZ + 0.5D, 90.0F, -90.0F);
                break;
            case EAST:
                this.setPositionAndRotation(this.tileX + this.height / 2.0D, this.tileY + 0.5D - this.height / 2.0D, this.tileZ + 0.5D, -90.0F, -90.0F);
                break;
        }
    }

    public static ItemEntityPlacer.ISpawnHandler getSpawnHandlerSteamable()
    {
        return new ItemEntityPlacer.DefaultSpawnHandler()
        {
            @Override
            public Entity spawn(World world, EntityPlayer player, int entityId, double x, double y, double z, int side)
            {
                EntitySteamHole entity = (EntitySteamHole) super.spawn(world, player, entityId, x, y, z, side);
                if (entity == null) return null;
                entity.setTilePosition(x, y, z);
                entity.determinOrientation(ForgeDirection.getOrientation(side));
                entity.ticksOffset = (int) (world.getTotalWorldTime() % cycleTicks);
                entity.isSteamable = true;
                return entity;
            }
        };
    }

    public static ItemEntityPlacer.ISpawnHandler getSpawnHandlerPeddle()
    {
        return new ItemEntityPlacer.DefaultSpawnHandler()
        {
            @Override
            public Entity spawn(World world, EntityPlayer player, int entityId, double x, double y, double z, int side)
            {
                EntitySteamHole entity = (EntitySteamHole) super.spawn(world, player, entityId, x, y, z, side);
                if (entity == null) return null;
                entity.setTilePosition(x, y, z);
                entity.determinOrientation(ForgeDirection.getOrientation(side));
                entity.isSteamable = false;
                return entity;
            }
        };
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
            if (this.isSteamable)
            {
                this.setSteaming((this.worldObj.getTotalWorldTime() + ticksOffset) % cycleTicks < steamDuration);
            }
        }

        if (this.isSteaming())
        {
            if (this.worldObj.isRemote)
            {
                float steamX = (float) this.posX + this.dir.offsetX * 0.3F;
                float steamY = (float) this.posY + this.dir.offsetY * 0.3F;
                float steamZ = (float) this.posZ + this.dir.offsetZ * 0.3F;
                float steamRandX = this.rand.nextFloat() * 0.1F - 0.05F;
                float steamRandZ = this.rand.nextFloat() * 0.1F - 0.05F;
                double gRand1 = this.dir.offsetX == 0 ? this.rand.nextGaussian() * 0.01D : (this.rand.nextDouble() * 0.05D + 0.15D) * this.dir.offsetX;
                double gRand2 = this.dir.offsetY == 0 ? this.rand.nextGaussian() * 0.01D : (this.dir.offsetY == 1 ? this.rand.nextDouble() * 0.05D + 0.10D : this.rand.nextDouble() * -0.05D - 0.15D);
                double gRand3 = this.dir.offsetZ == 0 ? this.rand.nextGaussian() * 0.01D : (this.rand.nextDouble() * 0.05D + 0.15D) * this.dir.offsetZ;
                this.worldObj.spawnParticle("explode", (steamX + steamRandX), steamY, (steamZ + steamRandZ), gRand1, gRand2, gRand3);

            }
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this,
                    this.boundingBox.addCoord(this.dir.offsetX * 2.0D, this.dir.offsetY * 2.0D, this.dir.offsetZ * 2.0D)
                    .expand(this.dir.offsetX == 0 ? 0.2D : 0.0D, this.dir.offsetY == 0 ? 0.2D : 0.0D, this.dir.offsetZ == 0 ? 0.2D : 0.0D));
            for (Object entry : list)
            {
                Entity entity = (Entity) entry;
                entity.attackEntityFrom(DamageSource.onFire, 4.0F);
            }
        }

        if (this.worldObj.isRemote)
        {
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.2D, 0.1D, 0.2D), new IEntitySelector()
            {
                @Override
                public boolean isEntityApplicable(Entity entity)
                {
                    return !(entity instanceof EntityGhost);
                }
            });
            for (Object entry : list)
            {
                Entity entity = (Entity) entry;
                if (this.getDistanceToEntity(entity) > this.getDistance(entity.posX + entity.motionX, entity.posY + entity.motionY, entity.posZ + entity.motionZ))
                {
                    float f = -0.6F;
                    entity.motionX = (double) (-MathHelper.sin(entity.rotationYaw / 180.0F * (float) Math.PI) * f);
                    entity.motionZ = (double) (MathHelper.cos(entity.rotationYaw / 180.0F * (float) Math.PI) * f);
                    entity.motionY = 0.25D;
                    entity.fallDistance = 0.0F;
                }
            }
        }

    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        nbt.setByte("Direction", (byte) this.dir.ordinal());
        nbt.setInteger("TileX", this.tileX);
        nbt.setInteger("TileY", this.tileY);
        nbt.setInteger("TileZ", this.tileZ);

        nbt.setBoolean("Steam", this.isSteamable);
        nbt.setShort("TicksOffset", (short)this.ticksOffset);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        this.dir = ForgeDirection.getOrientation(nbt.getByte("Direction"));
        this.tileX = nbt.getInteger("TileX");
        this.tileY = nbt.getInteger("TileY");
        this.tileZ = nbt.getInteger("TileZ");
        this.determinOrientation(dir);

        this.isSteamable = nbt.getBoolean("Steam");
        this.ticksOffset = nbt.getShort("TicksOffset");
    }

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

    protected boolean shouldSetPosAfterLoading()
    {
        return false;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        buffer.writeByte((byte) this.dir.ordinal());
    }

    @Override
    public void readSpawnData(ByteBuf additionalData)
    {
        this.dir = ForgeDirection.getOrientation(additionalData.readByte());
    }
}
