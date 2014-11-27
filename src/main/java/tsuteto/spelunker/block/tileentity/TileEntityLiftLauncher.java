package tsuteto.spelunker.block.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import tsuteto.spelunker.block.BlockLiftLauncher;
import tsuteto.spelunker.entity.EntityLift;

public class TileEntityLiftLauncher extends TileEntity
{
    private static final int interval = 60;

    private int ticksSinceLastDispatch = 0;

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!worldObj.isRemote)
        {
            int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);

            if (ticksSinceLastDispatch >= interval)
            {
                EntityLift newLift = new EntityLift(this.worldObj);

                if (meta == BlockLiftLauncher.DIR_UP)
                {
                    newLift.setPosition(this.xCoord, this.yCoord + 1, this.zCoord + newLift.width / 2 - 0.5D);
                    newLift.moveUp();
                }
                else
                {
                    newLift.setPosition(this.xCoord, this.yCoord - newLift.height, this.zCoord + newLift.width / 2 - 0.5D);
                    newLift.moveDown();
                }
                this.worldObj.spawnEntityInWorld(newLift);
                ticksSinceLastDispatch = 0;
            }
            else
            {
                ticksSinceLastDispatch++;
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);

        p_145839_1_.setShort("tick", (short)ticksSinceLastDispatch);
    }

    @Override
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);

        ticksSinceLastDispatch = p_145841_1_.getShort("tick");
    }
}
