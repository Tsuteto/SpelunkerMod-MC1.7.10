package tsuteto.spelunker.block.tileentity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import tsuteto.spelunker.item.SpelunkerItem;

public class TileEntityItemBox extends TileEntityRespawnPoint
{
    public ItemStack itemContained;
    public int respawnTimer = 0;
    public boolean shouldUpdateItem = false;

    public EntityItem itemEntity;

    public void setItemStack(ItemStack itemStack)
    {
        ItemStack prevItem = this.itemContained;
        this.itemContained = itemStack;

        if (prevItem == null || this.itemContained == null || !prevItem.isItemEqual(this.itemContained))
        {
            this.shouldUpdateItem = true;
        }
        this.respawnTimer = 0;
    }

    public int getItemRespawnTime()
    {
        if (itemContained != null)
        {
            if (MinecraftServer.getServer().isSinglePlayer())
            {
                return itemContained.getItem() == SpelunkerItem.itemGateKey || itemContained.getItem() instanceof SpelunkerItem ? 12000 : 200;
            }
            else
            {
                return itemContained.getItem() == SpelunkerItem.itemGateKey ? 12000
                        : itemContained.getItem() instanceof SpelunkerItem ? 600 : 100;
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
        if (this.respawnTimer > 0)
        {
            respawnTimer--;
        }
        else if (this.itemContained != null && this.shouldUpdateItem)
        {
            if (!this.worldObj.isRemote)
            {
                this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
            this.spawnItem();
            this.shouldUpdateItem = false;
        }

        if (this.itemEntity != null) this.itemEntity.age++;
    }

    public void onItemTaken()
    {
        this.respawnTimer = this.getItemRespawnTime();
        this.itemEntity = null;
        this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void spawnItem()
    {
        if (this.itemContained != null)
        {
            this.itemEntity = new EntityItem(this.getWorldObj());
            this.itemEntity.setEntityItemStack(this.itemContained);
        }
    }

    public boolean isItemAvailable()
    {
        return this.itemContained != null && this.respawnTimer == 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);

        this.respawnTimer = p_145839_1_.getShort("Respawn");
        NBTTagCompound nbtItem = p_145839_1_.getCompoundTag("Item");
        this.itemContained = ItemStack.loadItemStackFromNBT(nbtItem);
        if (this.itemContained.stackSize == 0) this.itemContained.stackSize = 1;
        this.shouldUpdateItem = true;
    }

    @Override
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        p_145841_1_.setShort("Respawn", (short)this.respawnTimer);
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
