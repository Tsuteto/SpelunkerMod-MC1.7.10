package tsuteto.spelunker.world.levelmapper;

import net.minecraft.client.resources.I18n;
import net.minecraft.world.World;
import tsuteto.spelunker.world.gen.WorldGenSpelunkerLevel;

abstract public class MapPiece
{
    public final String name;

    public MapPiece(String name)
    {
        this.name = name;
    }

    public abstract void place(WorldGenSpelunkerLevel gen, World world, int x, int y, int z, SpelunkerLevelMapper mapper, int mapX, int mapY);

    public String getName(int color)
    {
        return this.name;
    }

    public String getLocalizedName(int color)
    {
        return I18n.format("Spelunker.mapPiece." + this.getName(color));
    }
}
