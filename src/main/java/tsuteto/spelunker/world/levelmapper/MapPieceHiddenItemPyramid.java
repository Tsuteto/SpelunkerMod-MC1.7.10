package tsuteto.spelunker.world.levelmapper;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import tsuteto.spelunker.world.gen.WorldGenSpelunkerLevel;

public class MapPieceHiddenItemPyramid extends MapPieceItemHidden
{
    MapPieceHiddenItemPyramid(String name, int color)
    {
        super(name, color);
    }

    @Override
    public void place(WorldGenSpelunkerLevel gen, World world, int x, int y, int z, SpelunkerLevelMapper mapper, int mapX, int mapY)
    {
        super.place(gen, world, x, y, z, mapper, mapX, mapY);

        gen.setBlockAndNotifyAdequately(
                world, x, y, z + 2, Blocks.sandstone, 2);
        gen.setBlockAndNotifyAdequately(
                world, x, y, z + 3, Blocks.air, 0);
        gen.setBlockAndNotifyAdequately(
                world, x, y, z + 4, Blocks.air, 0);
        gen.setBlockAndNotifyAdequately(
                world, x, y, z + 5, Blocks.air, 0);
        gen.setBlockAndNotifyAdequately(
                world, x, y, z + 6, Blocks.sandstone, 2);

    }
}
