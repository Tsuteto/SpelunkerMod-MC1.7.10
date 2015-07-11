package tsuteto.spelunker.data;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.dimension.SpelunkerLevelInfo;
import tsuteto.spelunker.levelmap.SpelunkerMapInfo;

import java.util.Map;

public class SpeLevelRecordManager
{
    private final SpelunkerSaveHandlerRecords saveHandler;
    public final Map<String, SpeLevelRecordInfo> mapRecords = Maps.newHashMap();

    public SpeLevelRecordManager(SpelunkerSaveHandlerRecords saveHandler)
    {
        this.saveHandler = saveHandler;
    }

    public SpeLevelRecordManager(SpelunkerSaveHandlerRecords saveHandler, NBTTagCompound nbt)
    {
        this(saveHandler);

        NBTTagList mapList = nbt.getTagList("recorddata", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < mapList.tagCount(); i++)
        {
            NBTTagCompound entry = mapList.getCompoundTagAt(i);
            SpeLevelRecordInfo record = new SpeLevelRecordInfo(entry);
            if (record.verify())
            {
                mapRecords.put(record.mapFileName, record);
            }
        }
    }

    public SpeLevelRecordInfo.Record getRecord(EntityPlayer player, SpelunkerLevelInfo level)
    {
        return this.getRecord(player, level.mapFileName);
    }

    public SpeLevelRecordInfo.Record getRecord(EntityPlayer player, String mapFileName)
    {
        SpeLevelRecordInfo mapRecord = this.mapRecords.get(mapFileName);
        if (mapRecord != null)
        {
            return mapRecord.getRecord(player);
        }
        else
        {
            return null;
        }
    }

    public void registerRecord(SpelunkerLevelInfo level, EntityPlayer player, int time)
    {
        SpelunkerMapInfo map = SpelunkerMod.mapManager().getInfo(level.mapFileName);
        if (!this.mapRecords.containsKey(level.mapFileName))
        {
            this.mapRecords.put(map.fileName, new SpeLevelRecordInfo(map));
        }
        SpeLevelRecordInfo mapRecord = this.mapRecords.get(map.fileName);
        mapRecord.registerRecord(player, time);

        this.save();
    }

    public boolean clearRecord(EntityPlayer player, String mapFileName)
    {
        SpeLevelRecordInfo mapRecord = this.mapRecords.get(mapFileName);
        if (mapRecord != null)
        {
            boolean cleared = mapRecord.clearRecord(player);

            if (mapRecord.records.size() == 0)
            {
                this.mapRecords.remove(mapFileName);
            }

            this.save();
            return cleared;
        }
        else
        {
            return false;
        }
    }

    public NBTTagCompound getNBTTagCompound()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        updateTagCompound(nbttagcompound);
        return nbttagcompound;
    }

    private void updateTagCompound(NBTTagCompound nbt)
    {
        NBTTagList mapList = new NBTTagList();
        for (SpeLevelRecordInfo entry : mapRecords.values())
        {
            mapList.appendTag(entry.getNBTTagCompound());
        }
        nbt.setTag("recorddata", mapList);
    }

    public void save()
    {
        this.saveHandler.saveData(this);
    }
}
