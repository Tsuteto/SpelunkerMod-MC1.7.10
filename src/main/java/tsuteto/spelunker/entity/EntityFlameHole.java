package tsuteto.spelunker.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tsuteto.spelunker.init.SpelunkerBlocks;
import tsuteto.spelunker.item.ItemEntityPlacer;

import java.util.List;

public class EntityFlameHole extends Entity
{
    public static final int cycleTicks = 100;
    public static final int cycleSteps = 10;

    public int tileX;
    public int tileY;
    public int tileZ;

    public int ticksOffset;

    public EntityFlameHole(World p_i1704_1_)
    {
        super(p_i1704_1_);
        this.setSize(13.0F / 16.0F, 0.125F);
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
        switch (dir)
        {
            default:
                this.setPositionAndRotation(this.tileX + 0.5D, this.tileY + 1.0F - this.height, this.tileZ + 0.5D, 0.0F, 180.0F);
                break;
        }
    }

    public static ItemEntityPlacer.ISpawnHandler getSpawnHandler()
    {
        return new ItemEntityPlacer.DefaultSpawnHandler()
        {
            @Override
            public Entity spawn(World world, EntityPlayer player, int entityId, double x, double y, double z, int side)
            {
                ForgeDirection dir = ForgeDirection.getOrientation(side);
                if (dir == ForgeDirection.DOWN)
                {
                    EntityFlameHole entity = (EntityFlameHole) super.spawn(world, player, entityId, x, y, z, side);
                    if (entity == null) return null;
                    entity.setTilePosition(x, y, z);
                    entity.determinOrientation(dir);
                    entity.ticksOffset = (int) (world.getTotalWorldTime() % cycleTicks);

                    // Light block
                    world.setBlock(entity.tileX, entity.tileY, entity.tileZ, SpelunkerBlocks.blockLight);

                    return entity;
                }
                else
                {
                    return null;
                }
            }
        };
    }

    protected void entityInit()
    {
        this.dataWatcher.addObject(17, Byte.valueOf((byte) 0)); // flame strength
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

    public EntityFlameHole(World p_i1705_1_, double p_i1705_2_, double p_i1705_4_, double p_i1705_6_)
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

    @Override
    public void applyEntityCollision(Entity p_70108_1_) {}

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (!this.worldObj.isRemote)
        {
            this.setFlameProgress((int)((this.worldObj.getTotalWorldTime() + ticksOffset) % cycleTicks) / (cycleTicks / cycleSteps));
        }

        double flame = this.getFlameStrength();

        if (this.worldObj.isRemote)
        {
            float steamX = (float) this.posX;
            float steamY = (float) this.posY;
            float steamZ = (float) this.posZ;
            for (int i = 0; i <= 2 * flame; i++)
            {
                float steamRandX = this.rand.nextFloat() * 0.2F - 0.1F;
                float steamRandZ = this.rand.nextFloat() * 0.2F - 0.1F;
                double gRand1 = this.rand.nextGaussian() * 0.01D;
                double gRand2 = -0.20D * flame;
                double gRand3 = this.rand.nextGaussian() * 0.01D;
                this.worldObj.spawnParticle("flame", (steamX + steamRandX), steamY, (steamZ + steamRandZ), gRand1, gRand2, gRand3);
            }
        }

        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this,
                this.boundingBox.addCoord(0.0D, flame * -2.0D, 0.0D));
        for (Object entry : list)
        {
            Entity entity = (Entity) entry;
            entity.attackEntityFrom(DamageSource.onFire, 4.0F);
            entity.setFire(3);
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        nbt.setInteger("TileX", this.tileX);
        nbt.setInteger("TileY", this.tileY);
        nbt.setInteger("TileZ", this.tileZ);

        nbt.setShort("TicksOffset", (short)this.ticksOffset);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        this.tileX = nbt.getInteger("TileX");
        this.tileY = nbt.getInteger("TileY");
        this.tileZ = nbt.getInteger("TileZ");
        this.determinOrientation(ForgeDirection.DOWN);

        this.ticksOffset = nbt.getShort("TicksOffset");
    }

    public void setFlameProgress(int val)
    {
        this.dataWatcher.updateObject(17, Byte.valueOf((byte) val));
    }

    public int getFlameProgress()
    {
        return this.dataWatcher.getWatchableObjectByte(17);
    }

    public double getFlameStrength()
    {
        double half = cycleSteps / 2;
        return 1.0D - Math.abs(half - this.getFlameProgress()) / half;
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
    public void setDead()
    {
        super.setDead();

        Block block = this.worldObj.getBlock(this.tileX, this.tileY, this.tileZ);
        if (block == SpelunkerBlocks.blockLight)
        {
            this.worldObj.setBlockToAir(this.tileX, this.tileY, this.tileZ);
        }
    }
}
