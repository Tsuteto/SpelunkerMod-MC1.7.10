package tsuteto.spelunker.eventhandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import tsuteto.spelunker.constants.SpelunkerDifficulty;
import tsuteto.spelunker.constants.SpelunkerPacketType;
import tsuteto.spelunker.network.SpelunkerPacketDispatcher;
import tsuteto.spelunker.player.SpelunkerPlayerMP;
import tsuteto.spelunker.util.ModLog;
import tsuteto.spelunker.util.WatchBool;

import java.util.Random;

public abstract class GhostSpawnHandler
{
    public static GhostSpawnHandler create(EntityPlayer player, SpelunkerPlayerMP spelunker, Random rand)
    {
        if (spelunker.isInSpelunkerWorld())
        {
            return new GhostSpawnHandlerSpelunkerLevel(spelunker, rand);
        }
        else
        {
            return new GhostSpawnHandlerNormalWorld(spelunker, rand);
        }
    }

    protected EntityPlayer player;
    protected SpelunkerPlayerMP spelunker;
    protected Random rand;

    protected boolean isGhostComing = false;
    protected WatchBool statGhostComing;
    protected int timeUntilNextSpawn = -1;

    protected GhostSpawnHandler(SpelunkerPlayerMP spelunker, Random rand)
    {
        this.player = spelunker.player();
        this.spelunker = spelunker;
        this.rand = rand;
        this.statGhostComing = new WatchBool(isGhostComing);
    }

    public void updateGhostStat()
    {
        //ModLog.debug("Ghost list: " + SpelunkerMod.ghostList.size());
        isGhostComing = this.isGhostComing();

        if (statGhostComing.checkVal(isGhostComing))
        {
            if (isGhostComing)
            {
                new SpelunkerPacketDispatcher(SpelunkerPacketType.GHOST_COMING).sendPacketPlayer(player);
            }
            else
            {
                new SpelunkerPacketDispatcher(SpelunkerPacketType.GHOST_GONE).sendPacketPlayer(player);
            }
        }
    }

    public void spawnCheck()
    {
        double ghostSpawnRate = SpelunkerDifficulty.getSettings(player.worldObj).ghostSpawnRate;
        if (spelunker.isInCave() && ghostSpawnRate > 0 && !isGhostComing)
        {
            if (timeUntilNextSpawn < 0)
            {
                this.resetGhostSpawnTimer(ghostSpawnRate);
            }
            else
            {
                if (timeUntilNextSpawn == 0)
                {
                    if (!this.spawnGhost()) timeUntilNextSpawn = 21;
                }
                timeUntilNextSpawn--;
            }
        }
    }

    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        timeUntilNextSpawn = nbtTagCompound.getInteger("Spelunker_Ghost");
    }

    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        nbtTagCompound.setInteger("Spelunker_Ghost", timeUntilNextSpawn);
    }

    public abstract boolean isGhostComing();

    protected void resetGhostSpawnTimer(double ghostSpawnRate)
    {
        timeUntilNextSpawn = (int)(this.getTimeUntilNextSpawn() / ghostSpawnRate);
        ModLog.debug("Ghost spawns in %d", timeUntilNextSpawn);
    }

    protected abstract int getTimeUntilNextSpawn();

    protected abstract boolean spawnGhost();
}
