package tsuteto.spelunker.block.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.dimension.SpelunkerLevelInfo;
import tsuteto.spelunker.levelmap.SpelunkerMapInfo;
import tsuteto.spelunker.util.ModLog;

import java.util.UUID;

public class TileEntitySpelunkerPortalStage extends TileEntity implements IInventory
{
    public SpelunkerLevelInfo levelInfo = null;
    private int dimId;
    public String owner;
    public String ownerName;

    public void registerLevel(int dimId, SpelunkerMapInfo mapInfo)
    {
        this.levelInfo = new SpelunkerLevelInfo();
        this.levelInfo.dimId = dimId;
        this.levelInfo.mapFileName = mapInfo.fileName;
        this.levelInfo.levelName = mapInfo.mapName;
        SpelunkerMod.levelManager().register(levelInfo);

        this.markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        if (p_145839_1_.hasKey("Dim"))
        {
            this.dimId = p_145839_1_.getInteger("Dim");
        }
        if (p_145839_1_.hasKey("OwnerUUID"))
        {
            this.owner = p_145839_1_.getString("OwnerUUID");
        }
        if (p_145839_1_.hasKey("OwnerName"))
        {
            this.ownerName = p_145839_1_.getString("OwnerName");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        if (this.levelInfo != null)
        {
            p_145841_1_.setInteger("Dim", this.levelInfo.dimId);
        }
        if (this.owner != null)
        {
            p_145841_1_.setString("OwnerUUID", this.owner);
            if (this.ownerName != null && !this.ownerName.isEmpty())
            {
                p_145841_1_.setString("OwnerName", this.ownerName);
            }
        }
    }

    public EntityLivingBase getOwner()
    {
        try
        {
            return this.owner != null ? this.worldObj.func_152378_a(UUID.fromString(this.owner)) : null;
        }
        catch (IllegalArgumentException e)
        {
            ModLog.warn(e, "Invalid UUID for Spelunker portal owner! The ownership is removed.");
            this.owner = null;
            this.markDirty();
            return null;
        }
    }

    public boolean isOwner(Entity entity)
    {
        try
        {
            return this.owner == null || UUID.fromString(this.owner).equals(entity.getUniqueID());
        }
        catch (IllegalArgumentException e)
        {
            ModLog.warn(e, "Invalid UUID for Spelunker portal owner! The ownership is removed.");
            this.owner = null;
            this.markDirty();
            return true;
        }
    }

    public void setOwner(EntityPlayer player)
    {
        this.owner = player.getUniqueID().toString();
        this.ownerName = player.getCommandSenderName();

    }

    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        NBTTagCompound nbt = pkt.func_148857_g();
        if (nbt.hasKey(SpelunkerLevelInfo.NBT_ROOT))
        {
            this.levelInfo = new SpelunkerLevelInfo();
            this.levelInfo.readFromNBT(nbt);
        }
    }

    public Packet getDescriptionPacket()
    {
        if (levelInfo == null && dimId != 0)
        {
            this.levelInfo = SpelunkerMod.levelManager().getLevelInfo(dimId);
        }

        NBTTagCompound nbttagcompound = new NBTTagCompound();
        if (this.levelInfo != null)
        {
            levelInfo.writeToNBT(nbttagcompound);
        }
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 4, nbttagcompound);
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int p_70301_1_)
    {
        return null;
    }

    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_)
    {
        return null;
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int p_70304_1_)
    {
        return null;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_)
    {
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 0;
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName()
    {
        return null;
    }

    /**
     * Returns if the inventory is named
     */
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    /**
     * Returns the maximum stack size for a inventory slot.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_)
    {
        return true;
    }

    public void openInventory() {}

    public void closeInventory() {}

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_)
    {
        return true;
    }
}
