package tsuteto.spelunker.block.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import tsuteto.spelunker.block.SpelunkerBlocks;

import java.util.List;

public class TileEntityBlockBumpy extends TileEntity
{
    public int progress;

    @Override
    public void updateEntity()
    {
        this.progress = this.progress + 1 & 31;
        if (worldObj.isRemote)
        {
            this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }

        this.liftUpEntity(progress < 16 ? 0 : 1);
    }

    private void liftUpEntity(int s)
    {
        SpelunkerBlocks.blockBumpy.setStepCollisionBox(worldObj, xCoord, yCoord, zCoord, progress, s);

        AxisAlignedBB bb = SpelunkerBlocks.blockBumpy.getCollisionBoundingBoxFromPool(worldObj, xCoord, yCoord, zCoord);
        bb.maxY += 0.0625D;
        List entities = worldObj.getEntitiesWithinAABBExcludingEntity(null, bb);
        for (Object entry : entities)
        {
            Entity entity = (Entity) entry;
            entity.motionY = Math.max(entity.motionY, 0.0625D);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        this.progress = nbtTagCompound.getByte("prog");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setByte("prog", (byte) this.progress);
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 4, nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        this.readFromNBT(pkt.func_148857_g());
    }
}
