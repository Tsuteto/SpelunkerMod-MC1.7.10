package tsuteto.spelunker.world;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenSpelunkerLevel extends WorldGenerator
{
    @Override
    public boolean generate(World p_76484_1_, Random p_76484_2_, int ox, int oy, int oz)
    {
        SpelunkerLevelMapper mapper = SpelunkerLevelMapper.getInstance();

        int w = mapper.getWidth();
        int h = mapper.getHeight();
        for (int i = 0; i < w; i++)
        {
            for (int j = 0; j < h; j++)
            {
                SpelunkerLevelMapper.TileType tileType = mapper.getTile(i, j);
                int x = w + ox - i;
                int y = h + oy - j;
                if (tileType != null)
                {
                    for (int k = 0; k < 3; k++)
                    {
                        this.setBlockAndNotifyAdequately(p_76484_1_, x, y, k + oz, tileType.block, tileType.meta);
                    }
                }
                this.setBlockAndNotifyAdequately(p_76484_1_, x, y, 3 + oz, Blocks.stone, 0);
            }
        }

        return true;
    }
}
