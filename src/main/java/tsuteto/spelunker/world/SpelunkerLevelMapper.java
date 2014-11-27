package tsuteto.spelunker.world;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class SpelunkerLevelMapper
{
    private static SpelunkerLevelMapper instance = new SpelunkerLevelMapper();

    private final Map<Integer, TileType> colorToTileMapping = Maps.newHashMap();
    public final TileType air = new TileType(0x000000, Blocks.air);
    public final TileType wall = new TileType(0xc84e19, Blocks.dirt);
    public final TileType ladder = new TileType(0xc8ba19, Blocks.ladder);

    private BufferedImage mapImg;

    public static SpelunkerLevelMapper getInstance()
    {
        return instance;
    }

    private SpelunkerLevelMapper()
    {
        try
        {
            this.loadMap();
        }
        catch (IOException e)
        {
           throw new RuntimeException("Failed to load level mapping file", e);
        }
    }

    private void loadMap() throws IOException
    {
        InputStream inputstream = null;

        try
        {
            inputstream = this.getClass().getResource("/assets/spelunker/leveldata/testLevel.png").openStream();
            this.mapImg = ImageIO.read(inputstream);
        }
        finally
        {
            if (inputstream != null)
            {
                inputstream.close();
            }
        }
    }

    public TileType getTile(int x, int y)
    {
        int color = mapImg.getRGB(x, y) & 0xffffff;
        return colorToTileMapping.get(color);
    }

    public int getHeight()
    {
        return mapImg.getHeight();
    }

    public int getWidth()
    {
        return mapImg.getWidth();
    }

    class TileType
    {
        public int color;
        public Block block;
        public int meta;

        TileType(int color, Block block)
        {
            this(color, block, 0);
        }

        TileType(int color, Block block, int meta)
        {
            this.color = color;
            this.block = block;
            this.meta = meta;
            colorToTileMapping.put(color, this);
        }
    }
}
