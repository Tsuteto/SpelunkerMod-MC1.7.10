package tsuteto.spelunker.item;

import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.world.World;
import tsuteto.spelunker.entity.EntityFlash;

public class BehaviorDispenseFlash extends BehaviorProjectileDispense
{
    /**
     * Return the projectile entity spawned by this dispense behavior.
     */
    protected IProjectile getProjectileEntity(World par1World, IPosition par2IPosition)
    {
        return new EntityFlash(par1World, par2IPosition.getX(), par2IPosition.getY(), par2IPosition.getZ());
    }
}
