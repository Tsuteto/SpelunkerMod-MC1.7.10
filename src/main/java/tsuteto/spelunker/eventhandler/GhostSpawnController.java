package tsuteto.spelunker.eventhandler;

import net.minecraft.entity.player.EntityPlayerMP;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

import java.util.Random;

public abstract class GhostSpawnController
{
    protected GhostSpawnHandler handler;
    protected SpelunkerPlayerMP spelunker;
    protected EntityPlayerMP player;
    protected Random rand;

    public GhostSpawnController(GhostSpawnHandler handler, SpelunkerPlayerMP spelunker, Random rand)
    {
        this.handler = handler;
        this.spelunker = spelunker;
        this.player = spelunker.player();
        this.rand = rand;
    }

    public abstract boolean isGhostComing();

    protected abstract int getTimeUntilNextSpawn();

    protected abstract boolean spawnGhost();

    public abstract boolean canSpawn();
}
