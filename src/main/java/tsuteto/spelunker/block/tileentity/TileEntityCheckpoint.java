package tsuteto.spelunker.block.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCheckpoint extends TileEntity
{
    public int no;

    public void setCheckPointNo(int number)
    {
        this.no = number;
    }

    @Override
    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        this.no = p_145839_1_.getShort("CP");
    }

    @Override
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        p_145841_1_.setShort("CP", (short)this.no);
    }
}
