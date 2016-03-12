package tsuteto.spelunker.world.levelmapper;

import com.google.common.collect.Maps;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.levelmap.MapSource;
import tsuteto.spelunker.levelmap.SpelunkerMapInfo;
import tsuteto.spelunker.util.ModLog;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class SpelunkerLevelMapper
{
    static private HashMap<SpelunkerMapInfo, BufferedImage> cache = Maps.newHashMap();

    private BufferedImage mapImg;

    public SpelunkerLevelMapper(SpelunkerMapInfo mapInfo)
    {
        try
        {
            if (mapInfo.source == MapSource.USER)
            {
                this.loadMap(mapInfo, new File(new File(SpelunkerMod.getSpelunkerDir(), SpelunkerMod.levelMapFileDir), mapInfo.fileName));
            }
            else if (mapInfo.source == MapSource.RESOURCE)
            {
                this.loadMap(mapInfo, SpelunkerLevelMapper.class.getResourceAsStream("/assets/spelunker/leveldata/" + mapInfo.fileName));
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

    private void loadMap(SpelunkerMapInfo mapInfo, File mapFile) throws IOException
    {
        this.loadMap(mapInfo, new FileInputStream(mapFile));
    }

    private void loadMap(SpelunkerMapInfo mapInfo, InputStream inputstream) throws IOException
    {
        if (cache.containsKey(mapInfo))
        {
            this.mapImg = cache.get(mapInfo);
            ModLog.debug("Map data loaded from CACHE: %s", mapInfo.fileName);
        }
        else
        {
            try
            {
                this.mapImg = ImageIO.read(inputstream);
                cache.put(mapInfo, this.mapImg);
                ModLog.debug("Map data loaded from DISK: %s", mapInfo.fileName);
            }
            finally
            {
                if (inputstream != null)
                {
                    inputstream.close();
                }
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
