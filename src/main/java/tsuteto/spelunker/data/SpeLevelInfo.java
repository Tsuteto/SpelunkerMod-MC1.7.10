package tsuteto.spelunker.data;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class SpeLevelInfo
{
    private long startTime;
    private long finishTime;
    private boolean isCheated;

    private List<Byte> passedCheckPoints = Lists.newArrayList();
    private boolean isCleared;

    public SpeLevelInfo(NBTTagCompound nbt)
    {
        this.readFromNBT(nbt);
    }

    public SpeLevelInfo(EntityPlayer player)
    {
        startTime = player.worldObj.getTotalWorldTime();
        finishTime = -1;
        isCheated = player.worldObj.getWorldInfo().areCommandsAllowed();
        isCleared = false;
    }

    private void readFromNBT(NBTTagCompound nbt)
    {
        this.startTime = nbt.getLong("starttime");
        this.finishTime = nbt.getLong("finishtime");
        this.isCheated = nbt.getBoolean("cheated");
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
        stat.setLong("finishtime", finishTime);
        stat.setBoolean("cheated", isCheated);

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
        return passedCheckPoints.contains((byte)no);
    }

    public int getCheckPointCount()
    {
        return passedCheckPoints.size();
    }

    public void setCheckPointPassed(int no)
    {
        Byte b = (byte)no;
        if (!passedCheckPoints.contains(b))
        {
            passedCheckPoints.add(b);
        }
    }

    public boolean isCleared()
    {
        return isCleared;
    }

    public void setCleared(boolean isCleared)
    {
        this.isCleared = isCleared;
    }

    public long getStartTime()
    {
        return this.startTime;
    }

    public long getFinishTime()
    {
        return this.finishTime;
    }

    public int getTimeElapsed(EntityPlayer player)
    {
        long timeTo = finishTime != -1 ? finishTime : player.worldObj.getTotalWorldTime();
        return (int)(timeTo - startTime);
    }

    public void setFinishTime(long tick)
    {
        this.finishTime = tick;
    }

    public boolean isCheated()
    {
        return this.isCheated;
    }
}
