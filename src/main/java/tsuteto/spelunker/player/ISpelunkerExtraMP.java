package tsuteto.spelunker.player;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

public interface ISpelunkerExtraMP
{
    void beforeOnLivingUpdate();
    void afterOnLivingUpdate();
    void drainEnergy();
    boolean isUsingEnergy();
    void onPlayerOutOfCave();
    void resetSunHeatCount();
    void afterReadEntityFromNBT(NBTTagCompound nbttagcompound);
    void afterWriteEntityToNBT(NBTTagCompound nbttagcompound);
    boolean isUntouchableEntity(Entity entity);
    void onRespawn(EntityPlayer deadPlayer);
    void onItemPickup(Item item);
    void onBlockDestroyed(int x, int y, int z, Block block);
    boolean attackTargetEntityWithCurrentItem(Entity entity);
    void afterOnKillEntity(EntityLivingBase entityliving, int exp);
}
