package tsuteto.spelunker.world.levelmapper;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import tsuteto.spelunker.item.ItemEntityPlacer;
import tsuteto.spelunker.world.gen.WorldGenSpelunkerLevel;

import java.util.List;

public class MapPieceEntity extends MapPieceSingle
{
    private List<TileEntryEntity> tileMapping = Lists.newArrayList();

    public MapPieceEntity(String name, int color)
    {
        super(name, color);
    }

    public MapPieceEntity addEntity(int x, int y, int z, int side, String name)
    {
        return this.addEntity(x, y, z, side, name, null);
    }

    public MapPieceEntity addEntity(int x, int y, int z, int side, String name, TileEntryEntity.IHandler handler)
    {
        this.tileMapping.add(new TileEntryEntity(x, y, z, side, name).setHandler(handler));
        return this;
    }

    public MapPieceEntity addEntity(int z, int side, String name)
    {
        this.tileMapping.add(new TileEntryEntity(z, side, name));
        return this;
    }

    public MapPieceEntity addEntity(int x, int side, String name, TileEntryEntity.IHandler handler)
    {
        this.tileMapping.add(new TileEntryEntity(x, side, name).setHandler(handler));
        return this;
    }

    @Override
    public void place(WorldGenSpelunkerLevel gen, World world, int ox, int oy, int oz)
    {
        for (TileEntryEntity entry : this.tileMapping)
        {
            int x = ox + entry.offsetX;
            int y = oy + entry.offsetY;
            int z = oz + entry.offsetZ;
            // Spawn Entity
            Entity entity = ItemEntityPlacer.spawnEntity(
                    world, null, ItemEntityPlacer.getIdFromName(entry.entityName), x + 0.5D, y + 0.5D, z + 0.5D, entry.side);
            if (entity != null && entry.handler != null)
            {
                entry.handler.apply(world, x, y, z, entity);
            }
        }
    }

}
