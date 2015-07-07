package tsuteto.spelunker.data;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.levelmap.SpelunkerMapInfo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class SpeLevelRecordInfo
{
    public String mapFileName;
    public long revision;
    public Map<UUID, Record> records = Maps.newHashMap();

    public SpeLevelRecordInfo(SpelunkerMapInfo mapInfo)
    {
        this.mapFileName = mapInfo.fileName;
        this.revision = mapInfo.revision;
    }

    public SpeLevelRecordInfo(NBTTagCompound nbt)
    {
        mapFileName = nbt.getString("map");
        revision = nbt.getLong("rev");
        records.clear();
        NBTTagList recordNBT = nbt.getTagList("rec",  Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < recordNBT.tagCount(); i++)
        {
            NBTTagCompound entry = recordNBT.getCompoundTagAt(i);
            Record record = new Record();
            record.playerName = entry.getString("name");
            record.uuid = UUID.fromString(entry.getString("UUID"));
            record.time = entry.getInteger("time");
            record.checksum = entry.getByteArray("data");
            if (record.verify())
            {
                records.put(record.uuid, record);
            }
        }
    }

    public boolean verify()
    {
        SpelunkerMapInfo mapInfo = SpelunkerMod.mapManager().getInfo(this.mapFileName);
        return mapInfo.revision == this.revision;
    }

    public Record getRecord(EntityPlayer player)
    {
        return records.get(player.getUniqueID());
    }

    public void registerRecord(EntityPlayer player, int time)
    {
        Record record = new Record();
        record.playerName = player.getDisplayName();
        record.uuid = player.getUniqueID();
        record.time = time;
        record.setNewChecksum();
        records.put(player.getUniqueID(), record);
    }

    public NBTTagCompound getNBTTagCompound()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        updateTagCompound(nbt);
        return nbt;
    }

    private void updateTagCompound(NBTTagCompound nbt)
    {
        nbt.setString("map", mapFileName);
        nbt.setLong("rev", revision);
        NBTTagList recordNBT = new NBTTagList();
        for (Record record : records.values())
        {
            NBTTagCompound entry = new NBTTagCompound();
            entry.setString("name", record.playerName);
            entry.setString("UUID", record.uuid.toString());
            entry.setInteger("time", record.time);
            entry.setByteArray("data", record.checksum);
            recordNBT.appendTag(entry);
        }
        nbt.setTag("rec", recordNBT);
    }

    public static class Record
    {
        public String playerName;
        public UUID uuid;
        public int time;
        private byte[] checksum;

        public byte[] genDigest()
        {
            try
            {
                MessageDigest md = MessageDigest.getInstance("MD5");
                String data = playerName + time;
                md.update(data.getBytes());
                return md.digest();
            }
            catch (NoSuchAlgorithmException e)
            {
                throw new RuntimeException(e);
            }
        }

        public void setNewChecksum()
        {
            this.checksum = this.genDigest();
        }

        private boolean verify()
        {
            return Arrays.equals(checksum, this.genDigest());
        }
    }
}
