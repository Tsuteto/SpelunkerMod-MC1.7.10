package tsuteto.spelunker.block.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;

public class TileEntityRespawnPoint extends TileEntity
{
    public boolean isPointBlock;
    // For gate block
    public ChunkCoordinates respawnPoint = null;

    @Override
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        p_145841_1_.setBoolean("pointBlock", isPointBlock);
        if (respawnPoint != null)
        {
            p_145841_1_.setInteger("respawnX", respawnPoint.posX);
            p_145841_1_.setInteger("respawnY", respawnPoint.posY);
            p_145841_1_.setInteger("respawnZ", respawnPoint.posZ);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        this.isPointBlock = p_145839_1_.getBoolean("pointBlock");
        if (!this.isPointBlock)
        {
            this.respawnPoint = new ChunkCoordinates(
                    p_145839_1_.getInteger("respawnX"),
                    p_145839_1_.getInteger("respawnY"),
                    p_145839_1_.getInteger("respawnZ")
            );
        }
    }
}
