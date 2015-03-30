package tsuteto.spelunker.world.levelmapper;

import net.minecraft.world.World;
import tsuteto.spelunker.world.gen.WorldGenSpelunkerLevel;

public interface MapPiece
{
    void place(WorldGenSpelunkerLevel gen, World world, int x, int y, int z);

    int getColor();
    String getName();
}
