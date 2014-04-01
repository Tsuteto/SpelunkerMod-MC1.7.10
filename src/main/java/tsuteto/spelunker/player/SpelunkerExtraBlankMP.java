package tsuteto.spelunker.player;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

public class SpelunkerExtraBlankMP implements ISpelunkerExtraMP
{
    @Override
    public void beforeOnLivingUpdate() {}
    @Override
    public void afterOnLivingUpdate() {}
    @Override
    public void drainEnergy() {}
    @Override
    public boolean isUsingEnergy() {
        return false;
    }
    @Override
    public void resetSunHeatCount() {}
    @Override
    public void afterReadEntityFromNBT(NBTTagCompound nbttagcompound) {}
    @Override
    public void afterWriteEntityToNBT(NBTTagCompound nbttagcompound) {}
    @Override
    public void onPlayerOutOfCave() {}
    @Override
    public boolean isUntouchableEntity(Entity entity)
    {
        return false;
    }
    @Override
    public void onRespawn(EntityPlayer deadPlayer) {}
    @Override
    public void onItemPickup(Item item) {}
    @Override
    public void onBlockDestroyed(int x, int y, int z, Block block) {}
    @Override
    public boolean attackTargetEntityWithCurrentItem(Entity entity)
    {
        return true;
    }
    @Override
    public void afterOnKillEntity(EntityLivingBase entityliving, int exp) {}
}
