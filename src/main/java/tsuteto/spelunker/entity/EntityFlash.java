package tsuteto.spelunker.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import tsuteto.spelunker.block.BlockBatSpawner;
import tsuteto.spelunker.damage.SpelunkerDamageSource;
import tsuteto.spelunker.init.SpelunkerBlocks;

import java.util.List;

/**
 * Defines projectile of Flash, originally "IlluminatingFlare" by ayashige
 *
 * @author ayashige, Tsuteto
 *
 */
public class EntityFlash extends EntityThrowable
{
    private int ticksInAir;

    public EntityFlash(World var1)
    {
        super(var1);
        this.ticksInAir = 0;
    }

    public EntityFlash(World var1, EntityLivingBase var2)
    {
        this(var1, var2, 0.4F);
    }

    public EntityFlash(World var1, EntityLivingBase var2, float var3)
    {
        super(var1, var2);
        this.ticksInAir = 0;
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, var3, 1.0F);
    }

    public EntityFlash(World var1, double var2, double var4, double var6)
    {
        super(var1, var2, var4, var6);
        this.ticksInAir = 0;
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    @Override
    protected void onImpact(MovingObjectPosition var1)
    {
        EntityLivingBase thrower = this.getThrower();

        if (var1 != null && var1.entityHit != null)
        {
            var1.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, thrower), 0);
        }

        // Attack bats around
        List<Entity> entityList = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.boundingBox.expand(15.0D, 10.0D, 15.0D));
        for (Entity entity : entityList)
        {
            if (entity instanceof EntityLivingBase)
            {
                EntityLivingBase bat = (EntityLivingBase)entity;
                if (bat.canEntityBeSeen(this))
                {
                    bat.attackEntityFrom(SpelunkerDamageSource.causeFlashDamage(this, thrower), bat.getHealth());
                }
            }
        }

        for (int i = -15; i < 15; i++)
        {
            for (int j = -10; j < 10; j++)
            {
                for (int k = -15; k < 15; k++)
                {
                    int x = MathHelper.floor_double(this.posX + i);
                    int y = MathHelper.floor_double(this.posY + j);
                    int z = MathHelper.floor_double(this.posZ + k);
                    Block block = this.worldObj.getBlock(x, y, z);
                    if (block == SpelunkerBlocks.blockBatSpawner)
                    {
                        MovingObjectPosition mop = worldObj.rayTraceBlocks(Vec3.createVectorHelper(this.posX, this.posY, this.posZ), Vec3.createVectorHelper(x, y, z));
                        if (mop == null)
                        {
                            DamageSource dmgSrc;
                            dmgSrc = SpelunkerDamageSource.causeFlashDamage(this, thrower);
                            ((BlockBatSpawner) block).eliminateBats(this.worldObj, x, y, z, dmgSrc);
                        }
                    }
                }
            }
        }

        if (!this.worldObj.isRemote)
        {
            double var3, var5, var7;
            EntityFlashBullet var2 = new EntityFlashBullet(this.worldObj);

            if (var1 != null)
            {
                if (var1.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                {
                    var3 = var1.hitVec.xCoord;
                    var5 = var1.hitVec.yCoord;
                    var7 = var1.hitVec.zCoord;

                    double shift = 0.5D;
                    switch (var1.sideHit)
                    {
                        case 0:
                            var5 -= shift;
                            break;

                        case 1:
                            var5 += shift;
                            break;

                        case 2:
                            var7 -= shift;
                            break;

                        case 3:
                            var7 += shift;
                            break;

                        case 4:
                            var3 -= shift;
                            break;

                        case 5:
                            var3 += shift;
                    }
                }
                else
                {
                    var3 = this.posX;
                    var5 = this.posY;
                    var7 = this.posZ;
                }
            }
            else
            {
                var3 = this.posX;
                var5 = this.posY;
                var7 = this.posZ;
            }
            var2.setLocationAndAngles(var3, var5, var7, this.rotationYaw, 0.0F);
            this.worldObj.spawnEntityInWorld(var2);
        }

        for (int var9 = 0; var9 < 8; ++var9)
        {
            this.worldObj.spawnParticle("snowballpoof", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
        }

        if (!this.worldObj.isRemote)
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
        super.onUpdate();
        ++this.ticksInAir;

        if (this.ticksInAir >= 10 && !worldObj.isRemote)
        {
            onImpact(null);
        }
    }
}
