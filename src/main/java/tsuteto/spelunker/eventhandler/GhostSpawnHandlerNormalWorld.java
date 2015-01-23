package tsuteto.spelunker.eventhandler;

import net.minecraft.util.MathHelper;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.entity.EntityGhost;
import tsuteto.spelunker.player.SpelunkerPlayerMP;
import tsuteto.spelunker.util.ModLog;

import java.util.Random;

public class GhostSpawnHandlerNormalWorld extends GhostSpawnHandler
{
    protected GhostSpawnHandlerNormalWorld(SpelunkerPlayerMP spelunker, Random rand)
    {
        super(spelunker, rand);
    }

    @Override
    public int getTimeUntilNextSpawn()
    {
        return this.rand.nextInt(20 * 60 * 10) + 600;
    }

    @Override
    public boolean isGhostComing()
    {
        for (EntityGhost ghost : SpelunkerMod.ghostList)
        {
            if (player.dimension == ghost.dimension
                    && ghost.isEntityAlive() && ghost.getEntityToAttack() != null
                    && player.getDistanceToEntity(ghost) < 32.0D)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean spawnGhost()
    {
        for (int i = 0; i < 10; i++)
        {
            int remaining = rand.nextInt(25) + 40;
            int dx = rand.nextInt(remaining);
            int dy = rand.nextInt(remaining -= dx);
            int dz = remaining - dy;
            float dist = MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
            ModLog.debug("Ghost at (%d, %d, %d) dist=%f", dx, dy, dz, dist);
            double x = dx + player.posX;
            double y = dy + player.posY;
            double z = dz + player.posZ;
            if (dist < 32.0D && player.worldObj.getClosestVulnerablePlayer(x, y, z, 24.0D) == null)
            {
                EntityGhost ghost = new EntityGhost(player.worldObj, x, y, z);
                ghost.setType(EntityGhost.Type.NORMAL);
                if (player.worldObj.spawnEntityInWorld(ghost))
                {
                    return true;
                }
            }
        }
        return false;
    }
}
