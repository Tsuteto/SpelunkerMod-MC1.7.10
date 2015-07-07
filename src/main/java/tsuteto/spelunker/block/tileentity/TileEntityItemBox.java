package tsuteto.spelunker.block.tileentity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.item.ItemEnergy;
import tsuteto.spelunker.item.SpelunkerItem;

public class TileEntityItemBox extends TileEntityRespawnPoint
{
    public ItemStack itemContained;
    public long respawnTime = -1;
    public boolean shouldSpawnItem = false;
    public boolean isHidden = false;

    public EntityItem itemEntity;

    public void setItemStack(ItemStack itemStack)
    {
        ItemStack prevItem = this.itemContained;
        this.itemContained = itemStack;

        if (prevItem == null || this.itemContained == null || !prevItem.isItemEqual(this.itemContained))
        {
            this.shouldSpawnItem = true;
        }
        this.respawnTime = 0;
    }

    public int getItemRespawnTime()
    {
        if (itemContained != null)
        {
            if (SpelunkerMod.isSinglePlayer())
            {
                return itemContained.getItem() instanceof SpelunkerItem ? SpelunkerMod.restorationTime : 200;
            }
            else
            {
                return itemContained.getItem() instanceof ItemEnergy ? 100
                        : itemContained.getItem() instanceof SpelunkerItem ? SpelunkerMod.restorationTime : 100;
            }
        }
        else
        {
            return 400;
        }
    }

    @Override
    public void updateEntity()
    {
        if (this.itemContained != null
                && this.respawnTime != -1
                && this.respawnTime <= this.worldObj.getTotalWorldTime())
        {
            this.isHidden = false;
            this.respawnTime = -1;
        }

        if (this.shouldSpawnItem)
        {
            this.spawnItem();
            this.shouldSpawnItem = false;
            if (!this.worldObj.isRemote)
            {
                this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
        }

        if (this.itemEntity != null) this.itemEntity.age++;
    }

    public void onItemTaken()
    {
        this.respawnTime = this.worldObj.getTotalWorldTime() + this.getItemRespawnTime();
        this.isHidden = true;
        this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    private void spawnItem()
    {
        if (this.itemContained != null)
        {
            this.itemEntity = new EntityItem(this.getWorldObj());
            this.itemEntity.setEntityItemStack(this.itemContained);
        }
    }

    public boolean isItemAvailable()
    {
        return this.itemContained != null && !this.isHidden;
    }

    @Override
    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);

        if (p_145839_1_.hasKey("RespawnTime"))
        {
            this.respawnTime = p_145839_1_.getLong("RespawnTime");
        }
        else
        {
            this.respawnTime = -1;
        }
        this.isHidden = p_145839_1_.getBoolean("IsHidden");
        if (p_145839_1_.hasKey("Item"))
        {
            NBTTagCompound nbtItem = p_145839_1_.getCompoundTag("Item");
            this.itemContained = ItemStack.loadItemStackFromNBT(nbtItem);
            if (this.itemContained != null && this.itemContained.stackSize == 0)
            {
                this.itemContained.stackSize = 1;
            }
        }
        else
        {
            this.itemContained = null;
        }
        this.shouldSpawnItem = true;
    }

    @Override
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        p_145841_1_.setLong("RespawnTime", this.respawnTime);
        p_145841_1_.setBoolean("IsHidden", this.isHidden);
        if (itemContained != null)
        {
            p_145841_1_.setTag("Item", itemContained.writeToNBT(new NBTTagCompound()));
        }
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
