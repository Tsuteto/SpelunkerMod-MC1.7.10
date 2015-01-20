package tsuteto.spelunker.world.gen;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import tsuteto.spelunker.block.SpelunkerBlocks;
import tsuteto.spelunker.world.levelmapper.MapPiece;
import tsuteto.spelunker.world.levelmapper.SpelunkerLevelMapper;

import java.util.Random;

public class WorldGenSpelunkerLevel extends WorldGenerator
{
    public WorldGenSpelunkerLevel() {}

    public WorldGenSpelunkerLevel(boolean p_i2013_1_)
    {
        super(p_i2013_1_);
    }

    @Override
    public boolean generate(World world, Random rand, int ox, int oy, int oz)
    {
        SpelunkerLevelMapper mapper = SpelunkerLevelMapper.getInstance();

        int w = mapper.getWidth();
        int h = mapper.getHeight();
        for (int i = 0; i < w; i++)
        {
            for (int j = 0; j < h; j++)
            {
                int x = w + ox - i;
                int y = h + oy - j;

                // Background wall
                this.setBlockAndNotifyAdequately(world, x, y, oz + 3, SpelunkerBlocks.blockWall, 0);

                // Keep room
                for (int k = 0; k < 3; k++)
                {
                    this.setBlockAndNotifyAdequately(world, x, y, k + oz, Blocks.air, 0);
                }
            }
        }

        for (int i = 0; i < w; i++)
        {
            for (int j = 0; j < h; j++)
            {
                MapPiece piece = mapper.getMapPiece(i, j);
                int x = w + ox - i;
                int y = h + oy - j;

                // Generate pieces
                if (piece != null)
                {
                    piece.place(this, world, x, y, oz);
                }

                if (piece != SpelunkerLevelMapper.wall)
                {
                    this.setBlockAndNotifyAdequately(world, x, y, oz - 1, SpelunkerBlocks.blockMagicWall, 3);
                }
            }
        }

        return true;
    }

    @Override
    public void setBlockAndNotifyAdequately(World p_150516_1_, int p_150516_2_, int p_150516_3_, int p_150516_4_, Block p_150516_5_, int p_150516_6_)
    {
        super.setBlockAndNotifyAdequately(p_150516_1_, p_150516_2_, p_150516_3_, p_150516_4_, p_150516_5_, p_150516_6_);
    }
}