package tsuteto.spelunker.world.levelmapper;

import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.levelmap.MapSource;
import tsuteto.spelunker.levelmap.SpelunkerMapInfo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SpelunkerLevelMapper
{
    private BufferedImage mapImg;

    public SpelunkerLevelMapper(SpelunkerMapInfo mapInfo)
    {
        try
        {
            if (mapInfo.source == MapSource.USER)
            {
                this.loadMap(new File(new File(SpelunkerMod.getSpelunkerDir(), SpelunkerMod.levelMapFileDir), mapInfo.fileName));
            }
            else if (mapInfo.source == MapSource.RESOURCE)
            {
                this.loadMap(SpelunkerLevelMapper.class.getResourceAsStream("/assets/spelunker/leveldata/" + mapInfo.fileName));
            }
            else
            {
                throw new IllegalArgumentException("Unhandled source of map");
            }
        }
        catch (IOException e)
        {
           throw new RuntimeException("Failed to load level mapping file", e);
        }
    }

    private void loadMap(File mapFile) throws IOException
    {
        this.loadMap(new FileInputStream(mapFile));
    }

    private void loadMap(InputStream inputstream) throws IOException
    {
        try
        {
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

    public MapPiece getMapPiece(int x, int y)
    {
        int color = this.getColor(x, y);
        return MapPieces.colorMapping.get(color);
    }

    public int getColor(int x, int y)
    {
        return mapImg.getRGB(x, y) & 0xffffff;
    }

    public int getHeight()
    {
        return mapImg.getHeight();
    }

    public int getWidth()
    {
        return mapImg.getWidth();
    }

    public void close()
    {
        mapImg = null;
    }
}
