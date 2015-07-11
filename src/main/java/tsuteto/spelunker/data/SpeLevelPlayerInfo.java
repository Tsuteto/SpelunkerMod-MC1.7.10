package tsuteto.spelunker.data;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;

import java.util.List;

public class SpeLevelPlayerInfo
{
    private long startTime;
    private long cpTime;
    private long finishTime;
    private boolean isCheated;
    private ChunkCoordinates respawn;

    private List<Byte> passedCheckPoints = Lists.newArrayList();
    private boolean isCleared;

    public SpeLevelPlayerInfo(NBTTagCompound nbt)
    {
        this.readFromNBT(nbt);
    }

    public SpeLevelPlayerInfo()
    {
        this.startTime = -1;
        this.cpTime = -1;
        this.finishTime = -1;
        this.isCheated = false;
        this.isCleared = false;
    }

    private void readFromNBT(NBTTagCompound nbt)
    {
        this.startTime = nbt.getLong("starttime");
        this.cpTime = nbt.getLong("cptime");
        this.finishTime = nbt.getLong("finishtime");
        this.isCheated = nbt.getBoolean("cheated");
        if (nbt.hasKey("respawn"))
        {
            NBTTagCompound respawn = nbt.getCompoundTag("respawn");
            this.respawn = new ChunkCoordinates(respawn.getInteger("x"), respawn.getInteger("y"), respawn.getInteger("z"));
        }
        this.isCleared = nbt.getBoolean("cleared");

        byte[] chkpts = nbt.getByteArray("chkpts");
        for (byte chkpt : chkpts)
        {
            passedCheckPoints.add(chkpt);
        }
    }

    public void writeToNBT(NBTTagCompound root)
    {
        NBTTagCompound stat = new NBTTagCompound();
        stat.setLong("starttime", startTime);
        stat.setLong("cptime", cpTime);
        stat.setLong("finishtime", finishTime);
        stat.setBoolean("cheated", isCheated);
        if (this.respawn != null)
        {
            NBTTagCompound respawn = new NBTTagCompound();
            respawn.setInteger("x", this.respawn.posX);
            respawn.setInteger("y", this.respawn.posY);
            respawn.setInteger("z", this.respawn.posZ);
            stat.setTag("respawn", respawn);
        }

        stat.setBoolean("cleared", isCleared);
        byte[] chkpts = new byte[passedCheckPoints.size()];
        for (int i = 0; i < passedCheckPoints.size(); i++)
        {
            chkpts[i] = (byte) (int) passedCheckPoints.get(i);
        }
        stat.setByteArray("chkpts", chkpts);

        root.setTag("spelvl", stat);
    }

    public boolean isCheckPointPassed(int no)
    {
        return this.passedCheckPoints.contains((byte)no);
    }

    public int getCheckPointCount()
    {
        return this.passedCheckPoints.size();
    }

    public void setCheckPointPassed(int no)
    {
        Byte b = (byte)no;
        if (!this.passedCheckPoints.contains(b))
        {
            this.passedCheckPoints.add(b);
        }
    }

    public boolean isCleared()
    {
        return this.isCleared;
    }

    public void setCleared(boolean isCleared)
    {
        this.isCleared = isCleared;
    }

    public long getStartTime()
    {
        return this.startTime;
    }

    public void setStartTime(long startTime)
    {
        this.startTime = startTime;
    }

    public long getFinishTime()
    {
        return this.finishTime;
    }

    public int markSplitTime(EntityPlayer player)
    {
        long timeFrom = this.cpTime != -1 ? this.cpTime : this.startTime;
        int splitTime = (int)(player.worldObj.getTotalWorldTime() - timeFrom);
        this.cpTime = player.worldObj.getTotalWorldTime();
        return splitTime;
    }

    public int getTotalTime(EntityPlayer player)
    {
        long timeTo = this.finishTime != -1 ? this.finishTime : player.worldObj.getTotalWorldTime();
        return (int)(timeTo - this.startTime);
    }

    public void setFinishTime(long tick)
    {
        this.finishTime = tick;
    }

    public boolean isCheated()
    {
        return this.isCheated;
    }

    public void setCheated()
    {
        this.isCheated = true;
    }

    public boolean isStarted()
    {
        return this.startTime != -1;
    }

    public void setRespawnPoint(ChunkCoordinates coord)
    {
        this.respawn = coord;
    }

    public ChunkCoordinates getRespawnPoint()
    {
        return this.respawn;
    }
}
