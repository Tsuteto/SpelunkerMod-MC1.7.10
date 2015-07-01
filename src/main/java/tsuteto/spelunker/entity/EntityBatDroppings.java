package tsuteto.spelunker.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import tsuteto.spelunker.damage.SpelunkerDamageSource;
import tsuteto.spelunker.init.SpelunkerBlocks;

import java.util.List;

/**
 * Defines Bat Droppings
 *
 * @author Tsuteto
 *
 */
public class EntityBatDroppings extends EntityThrowable
{
    public EntityBatDroppings(World par1World)
    {
        super(par1World);
    }

    public EntityBatDroppings(World par1World, EntityLivingBase par2EntityLiving)
    {
        super(par1World, par2EntityLiving);

        this.setLocationAndAngles(par2EntityLiving.posX, par2EntityLiving.posY, par2EntityLiving.posZ, par2EntityLiving.rotationYaw, par2EntityLiving.rotationPitch);
        this.motionX = 0;
        this.motionZ = 0;
        this.motionY = 0;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (!this.worldObj.isRemote)
        {
            Entity var4 = null;
            List<Entity> var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            EntityLivingBase var8 = this.getThrower();
            for (Entity entity : var5)
            {
                if (entity.canBeCollidedWith())
                {
                    onImpact(new MovingObjectPosition(entity));
                    break;
                }
            }

            if (this.ticksExisted > 200)
            {
                this.setDead();
            }
        }

        if (this.motionY < -0.3F)
        {
            this.motionY = -0.3F;
        }
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    @Override
    protected void onImpact(MovingObjectPosition par1MovingObjectPosition)
    {
        if (par1MovingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            Block block = this.worldObj.getBlock(par1MovingObjectPosition.blockX, par1MovingObjectPosition.blockY, par1MovingObjectPosition.blockZ);
            if (block != SpelunkerBlocks.blockBatSpawner)
            {
                this.setDead();
                return;
            }
        }

        if (par1MovingObjectPosition.entityHit != null)
        {
            if (par1MovingObjectPosition.entityHit instanceof EntityPlayer)
            {
                par1MovingObjectPosition.entityHit.attackEntityFrom(SpelunkerDamageSource.droppings, 1);
            }
        }
    }

    @Override
    protected float func_70182_d()
    {
        return 0.01F;
    }
}
