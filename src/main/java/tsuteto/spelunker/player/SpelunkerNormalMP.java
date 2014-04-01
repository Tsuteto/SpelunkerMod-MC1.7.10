package tsuteto.spelunker.player;

import api.player.server.IServerPlayer;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

public class SpelunkerNormalMP implements ISpelunkerExtraMP
{
    protected EntityPlayerMP player;
    protected IServerPlayer playerAPI;
    protected SpelunkerPlayerMP spelunker;

    public SpelunkerNormalMP(SpelunkerPlayerMP spelunker)
    {
        this.spelunker = spelunker;
        this.player = spelunker.player();
        this.playerAPI = spelunker.playerAPI();
    }

    @Override
    public void beforeOnLivingUpdate() {}

    @Override
    public void afterOnLivingUpdate() {}

    @Override
    public void drainEnergy()
    {
        int foodlevel = player.getFoodStats().getFoodLevel();
        if (foodlevel <= 0)
        {
            spelunker.decreaseEnergy(4);
        }
        else if (foodlevel <= 6)
        {
            spelunker.decreaseEnergy(3);
        }
        else if (foodlevel <= 12)
        {
            spelunker.decreaseEnergy(2);
        }
        else
        {
            spelunker.decreaseEnergy(1);
        }
    }

    @Override
    public boolean isUsingEnergy()
    {
        return spelunker.isInCave() && !player.capabilities.disableDamage;
    }

    @Override
    public void resetSunHeatCount() {}

    @Override
    public void afterReadEntityFromNBT(NBTTagCompound nbttagcompound) {}

    @Override
    public void afterWriteEntityToNBT(NBTTagCompound nbttagcompound) {}

    @Override
    public void onPlayerOutOfCave()
    {
        spelunker.setEnergy(spelunker.getMaxEnergy());
    }

    @Override
    public boolean isUntouchableEntity(Entity entity)
    {
        return entity instanceof EntityAnimal || entity instanceof EntityAmbientCreature || entity instanceof IMob
                || entity instanceof EntityWaterMob || entity instanceof EntityGolem;
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
