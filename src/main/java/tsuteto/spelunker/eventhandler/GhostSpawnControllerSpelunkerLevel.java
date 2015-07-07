package tsuteto.spelunker.eventhandler;

import tsuteto.spelunker.data.SpeLevelPlayerInfo;
import tsuteto.spelunker.entity.EntityGhost;
import tsuteto.spelunker.player.SpelunkerPlayerMP;
import tsuteto.spelunker.util.ModLog;

import java.util.Random;

public class GhostSpawnControllerSpelunkerLevel extends GhostSpawnController
{
    protected GhostSpawnControllerSpelunkerLevel(GhostSpawnHandler handler, SpelunkerPlayerMP spelunker, Random rand)
    {
        super(handler, spelunker, rand);
    }

    @Override
    public int getTimeUntilNextSpawn()
    {
        return this.rand.nextInt(20 * 60 * 3) + 400;
    }

    public boolean isSpawnValid()
    {
        return !this.isCleared();
    }

    @Override
    public boolean isGhostComing()
    {
        if (!this.isCleared())
        {
            for (EntityGhost ghost : GhostSpawnHandler.ghostList)
            {
                if (player.dimension == ghost.dimension
                        && ghost.isEntityAlive() && ghost.getEntityToAttack() != null
                        && player.getDistanceToEntity(ghost) < 32.0D)
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean spawnGhost()
    {
        for (int i = 0; i < 10; i++)
        {
            double x = (int)player.posX + rand.nextInt(40) - rand.nextInt(40) + 0.5D;
            double y = (int)player.posY + rand.nextInt(40) - rand.nextInt(40) + 0.5D;
            double z = 1.5D;
            EntityGhost ghost = new EntityGhost(player.worldObj, x , y, z);
            double dist = ghost.getDistanceToEntity(player);
            ModLog.debug("Ghost trying to spawn at (%.1f, %.1f, %.1f), dist=%.2f", x, y, z, dist);
            if (ghost.getDistanceToEntity(player) < 32.0D
                    && player.worldObj.getClosestVulnerablePlayerToEntity(ghost, 20) == null)
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

    private boolean isCleared()
    {
        SpeLevelPlayerInfo levelInfo = spelunker.getWorldInfo().getSpeLevelInfo();
        return levelInfo != null && levelInfo.isCleared();
    }
}
