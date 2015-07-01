package tsuteto.spelunker.world.levelmapper;

import net.minecraft.world.World;
import tsuteto.spelunker.world.gen.WorldGenSpelunkerLevel;

public class MapPieceBlank extends MapPieceSingle
{
    MapPieceBlank(String name, int color)
    {
        super(name, color);
    }

    @Override
    public void place(WorldGenSpelunkerLevel gen, World world, int x, int y, int z, SpelunkerLevelMapper mapper, int mapX, int mapY) {}
}
