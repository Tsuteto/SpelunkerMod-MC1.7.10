package tsuteto.spelunker.world.levelmapper;

import com.google.common.collect.Maps;
import tsuteto.spelunker.block.tileentity.TileEntityItemBoxHidden;
import tsuteto.spelunker.block.tileentity.TileEntityItemSpawnPoint;

import java.util.HashMap;

public class HiddenItemMapper
{
    private HashMap<Integer, TileEntityItemSpawnPoint> pointList = Maps.newHashMap();
    private HashMap<Integer, TileEntityItemBoxHidden> itemBoxList = Maps.newHashMap();

    public void registerPoint(int id, TileEntityItemSpawnPoint point)
    {
        this.pointList.put(id, point);
        if (this.itemBoxList.containsKey(id))
        {
            TileEntityItemBoxHidden itemBox = this.itemBoxList.get(id);
            point.targetX = itemBox.xCoord;
            point.targetY = itemBox.yCoord;
            point.targetZ = itemBox.zCoord;
        }
    }

    public void registerItemBox(int id, TileEntityItemBoxHidden itemBox)
    {
        this.itemBoxList.put(id, itemBox);
        if (this.pointList.containsKey(id))
        {
            TileEntityItemSpawnPoint point = this.pointList.get(id);
            point.targetX = itemBox.xCoord;
            point.targetY = itemBox.yCoord;
            point.targetZ = itemBox.zCoord;
        }
    }
}
