package tsuteto.spelunker.eventhandler;

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
            if (ghost.isEntityAlive() && ghost.getEntityToAttack() != null && player.getDistanceToEntity(ghost) < 32.0D)
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
            int x = rand.nextInt(remaining);
            int y = rand.nextInt(remaining -= x);
            int z = remaining - y;
            double dist = Math.sqrt(x * x + y * y + z * z);
            ModLog.debug("Ghost at (%d, %d, %d) dist=%f", x, y, z, dist);
            EntityGhost ghost = new EntityGhost(player.worldObj, x + player.posX, y + player.posY, z + player.posZ);
            if (dist < 32.0D && player.worldObj.getClosestVulnerablePlayerToEntity(ghost, 24.0D) == null)
            {
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
