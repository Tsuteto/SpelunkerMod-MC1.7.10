package tsuteto.spelunker.block.tileentity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.init.SpelunkerItems;

import java.util.Random;

public class TileEntityItemBoxHidden extends TileEntityItemBox
{
    private static final Item[] itemList = new Item[]{SpelunkerItems.item1up, SpelunkerItems.item2xScore, SpelunkerItems.itemSpeedPotion, SpelunkerItems.itemInvincible};
    private static Random rand = new Random();

    private long timeToHide = -1;

    public TileEntityItemBoxHidden()
    {
        this.isHidden = true;
    }

    public int getItemRespawnTime()
    {
        if (SpelunkerMod.isSinglePlayer())
        {
            return SpelunkerMod.restorationTime;
        }
        else
        {
            return 600;
        }
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.worldObj.isRemote)
        {
            if (this.timeToHide != -1 && this.timeToHide <= this.worldObj.getTotalWorldTime())
            {
                this.timeToHide = -1;
                this.setItemStack(null); // Item is not available until revealed next
            }
        }
    }

    public boolean revealItem()
    {
        if (this.itemContained == null && (this.respawnTime == -1 || this.respawnTime <= this.worldObj.getTotalWorldTime()))
        {
            this.isHidden = false;
            this.respawnTime = -1;
            this.timeToHide = this.worldObj.getTotalWorldTime() + SpelunkerMod.restorationTime;
            this.setItemStack(new ItemStack(itemList[rand.nextInt(itemList.length)]));
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void onItemTaken()
    {
        super.onItemTaken();
        this.setItemStack(null); // Item is not available until revealed next
    }

    @Override
    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        this.timeToHide = p_145839_1_.getLong("Hide");
    }

    @Override
    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        p_145841_1_.setLong("Hide", this.timeToHide);
    }
}
