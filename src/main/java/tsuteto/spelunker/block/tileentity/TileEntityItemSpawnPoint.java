package tsuteto.spelunker.block.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityItemSpawnPoint extends TileEntity
{
    public int targetX;
    public int targetY;
    public int targetZ;

    public void setTarget(int x, int y, int z)
    {
        this.targetX = x;
        this.targetY = y;
        this.targetZ = z;
    }

    @Override
    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);

        this.targetX = p_145839_1_.getInteger("TargetX");
        this.targetY = p_145839_1_.getInteger("TargetY");
        this.targetZ = p_145839_1_.getInteger("TargetZ");
    }

    @Override
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);

        p_145841_1_.setInteger("TargetX", this.targetX);
        p_145841_1_.setInteger("TargetY", this.targetY);
        p_145841_1_.setInteger("TargetZ", this.targetZ);
    }


}
