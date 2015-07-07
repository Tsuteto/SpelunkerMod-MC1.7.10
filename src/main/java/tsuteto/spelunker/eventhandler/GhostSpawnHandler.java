package tsuteto.spelunker.eventhandler;

import com.google.common.collect.Lists;
import cpw.mods.fml.server.FMLServerHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import tsuteto.spelunker.constants.SpelunkerDifficulty;
import tsuteto.spelunker.constants.SpelunkerPacketType;
import tsuteto.spelunker.entity.EntityGhost;
import tsuteto.spelunker.network.SpelunkerPacketDispatcher;
import tsuteto.spelunker.player.SpelunkerPlayerMP;
import tsuteto.spelunker.util.ModLog;
import tsuteto.spelunker.util.WatchBool;
import tsuteto.spelunker.world.WorldProviderSpelunker;

import java.util.List;
import java.util.Random;

public class GhostSpawnHandler
{
    public static List<EntityGhost> ghostList = Lists.newArrayList();

    public static void resetGhostList()
    {
        ghostList.clear();

        MinecraftServer server = FMLServerHandler.instance().getServer();
        for (World world : server.worldServers)
        {
            for (Object obj : world.getLoadedEntityList())
            {
                if (obj instanceof EntityGhost)
                {
                    ghostList.add((EntityGhost) obj);
                }
            }
        }
    }

    public static void onWorldClosed()
    {
        ghostList.clear();
    }

    protected GhostSpawnController controller;

    protected EntityPlayer player;
    protected SpelunkerPlayerMP spelunker;
    protected Random rand;

    protected boolean isGhostComing = false;
    protected WatchBool statGhostComing;
    protected int timeUntilNextSpawn = -1;

    public GhostSpawnHandler(SpelunkerPlayerMP spelunker, Random rand)
    {
        this.player = spelunker.player();
        this.spelunker = spelunker;
        this.rand = rand;
        this.statGhostComing = new WatchBool(isGhostComing);
    }

    public void initController(SpelunkerPlayerMP spelunker, Random rand)
    {
        GhostSpawnController prevController = this.controller;
        if (spelunker.player().worldObj.provider instanceof WorldProviderSpelunker)
        {
            this.controller = new GhostSpawnControllerSpelunkerLevel(this, spelunker, rand);
        }
        else
        {
            this.controller = new GhostSpawnControllerNormalWorld(this, spelunker, rand);
        }
        ModLog.debug("GSH controller: %s", this.controller.getClass().getName());

        if (prevController != null && this.controller.getClass() != prevController.getClass())
        {
            this.resetGhostSpawnTimer();
        }
    }

    public void updateGhostStat()
    {
        //ModLog.debug("Ghost list: " + SpelunkerMod.ghostList.size());
        isGhostComing = this.isGhostComing();

        if (statGhostComing.checkVal(isGhostComing))
        {
            if (isGhostComing)
            {
                new SpelunkerPacketDispatcher(SpelunkerPacketType.GHOST_COMING)
                        .sendPacketPlayer(player);
            }
            else
            {
                new SpelunkerPacketDispatcher(SpelunkerPacketType.GHOST_GONE).sendPacketPlayer(player);
            }
        }
    }

    public void spawnCheck()
    {
        if (!controller.isSpawnValid()) return;

        double ghostSpawnRate = SpelunkerDifficulty.getSettings(player.worldObj).ghostSpawnRate;
        if (!player.capabilities.isCreativeMode && player.isEntityAlive() && spelunker.isInCave() && ghostSpawnRate > 0 && !isGhostComing)
        {
            if (timeUntilNextSpawn < 0)
            {
                this.resetGhostSpawnTimer();
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

    public boolean isGhostComing()
    {
        return this.controller.isGhostComing();
    }

    public void resetGhostSpawnTimer()
    {
        double ghostSpawnRate = SpelunkerDifficulty.getSettings(player.worldObj).ghostSpawnRate;
        timeUntilNextSpawn = (int)(this.getTimeUntilNextSpawn() / ghostSpawnRate);
        ModLog.debug("Ghost spawns in %d", timeUntilNextSpawn);
    }

    protected int getTimeUntilNextSpawn()
    {
        return this.controller.getTimeUntilNextSpawn();
    }

    protected boolean spawnGhost()
    {
        return this.controller.spawnGhost();
    }

    public void eliminate()
    {
        for (EntityGhost ghost : ghostList)
        {
            if (this.player.equals(ghost.getAttackTarget()))
            {
                ghost.setDead();
            }
        }
    }
}
