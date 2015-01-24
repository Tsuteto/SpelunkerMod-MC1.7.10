package tsuteto.spelunker.eventhandler;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import tsuteto.spelunker.constants.SpelunkerDifficulty;
import tsuteto.spelunker.constants.SpelunkerPacketType;
import tsuteto.spelunker.entity.EntityGhost;
import tsuteto.spelunker.network.SpelunkerPacketDispatcher;
import tsuteto.spelunker.player.SpelunkerPlayerMP;
import tsuteto.spelunker.util.ModLog;
import tsuteto.spelunker.util.WatchBool;

import java.util.List;
import java.util.Random;

public abstract class GhostSpawnHandler
{
    public static List<EntityGhost> ghostList = Lists.newArrayList();

    public static GhostSpawnHandler create(SpelunkerPlayerMP spelunker, Random rand)
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

    public static void onWorldClosed()
    {
        ghostList.clear();
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
        if (player.isEntityAlive() && spelunker.isInCave() && ghostSpawnRate > 0 && !isGhostComing)
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
        if (nbtTagCompound.hasKey("Spelunker_Ghost") && nbtTagCompound.getTag("Spelunker_Ghost") instanceof NBTTagCompound)
        {
            NBTTagCompound tag = nbtTagCompound.getCompoundTag("Spelunker_Ghost");
            timeUntilNextSpawn = tag.getInteger("Timer");
        }
        ModLog.debug("GSH Loaded: Timer=" + timeUntilNextSpawn);
    }

    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("Timer", timeUntilNextSpawn);

        nbtTagCompound.setTag("Spelunker_Ghost", tag);
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
