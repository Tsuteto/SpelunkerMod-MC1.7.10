package tsuteto.spelunker.world.levelmapper;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

class TileEntryEntity
{
    int offsetX;
    int offsetY;
    int offsetZ;
    int side;
    String entityName;
    IHandler handler = null;

    public TileEntryEntity(int offsetZ, int side, String entityName)
    {
        this(0, 0, offsetZ, side, entityName);
    }

    public TileEntryEntity(int offsetX, int offsetY, int offsetZ, int side, String entityName)
    {
        this.entityName = entityName;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.side = side;
    }

    public TileEntryEntity setHandler(IHandler handler)
    {
        this.handler = handler;
        return this;
    }

    interface IHandler
    {
        void apply(World world, int offsetX, int offsetY, int offsetZ, Entity entity);
    }
}
