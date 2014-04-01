package tsuteto.spelunker.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.player.ISpelunkerPlayer;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class ItemGoldenSpelunker extends Item
{
    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.bow;
    }

    /**
     * How long it takes to use or consume an item
     */
    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 64;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        ISpelunkerPlayer spelunker = SpelunkerMod.getSpelunkerPlayer(par3EntityPlayer);
        if (spelunker != null && spelunker.isHardcore())
        {
            par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
        }
        return par1ItemStack;
    }

    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        for (int i = 0; i < par3EntityPlayer.inventory.mainInventory.length; i++)
        {
            ItemStack is = par3EntityPlayer.inventory.mainInventory[i];
            if (is != null && is.getItem() == this)
            {
                par3EntityPlayer.inventory.mainInventory[i] = null;
            }
        }

        if (!par2World.isRemote)
        {
            SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(par3EntityPlayer);
            if (spelunker != null)
            {
                spelunker.resetGoldenSpelunker();
            }
        }

        par3EntityPlayer.inventory.markDirty();

        for (int j = 0; j < 8; ++j)
        {
            Vec3 vec3 = par2World.getWorldVec3Pool().getVecFromPool((this.itemRand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
            vec3.rotateAroundX(-par3EntityPlayer.rotationPitch * (float)Math.PI / 180.0F);
            vec3.rotateAroundY(-par3EntityPlayer.rotationYaw * (float)Math.PI / 180.0F);
            Vec3 vec31 = par2World.getWorldVec3Pool().getVecFromPool((this.itemRand.nextFloat() - 0.5D) * 0.3D, (-this.itemRand.nextFloat()) * 0.6D - 0.3D, 0.6D);
            vec31.rotateAroundX(-par3EntityPlayer.rotationPitch * (float)Math.PI / 180.0F);
            vec31.rotateAroundY(-par3EntityPlayer.rotationYaw * (float)Math.PI / 180.0F);
            vec31 = vec31.addVector(par3EntityPlayer.posX, par3EntityPlayer.posY + par3EntityPlayer.getEyeHeight(), par3EntityPlayer.posZ);
            par2World.spawnParticle("explode", vec31.xCoord, vec31.yCoord, vec31.zCoord, vec3.xCoord, vec3.yCoord + 0.05D, vec3.zCoord);
        }

        par1ItemStack.stackSize = 0;
        return par1ItemStack;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        ISpelunkerPlayer spelunker = SpelunkerMod.getSpelunkerPlayer(par2EntityPlayer);
        if (spelunker != null)
        {
            if (spelunker.isHardcore())
            {
                par3List.add(StatCollector.translateToLocal("item.spelunker:goldenStatueF.desc.hc1"));
                par3List.add(StatCollector.translateToLocal("item.spelunker:goldenStatueF.desc.hc2"));
                par3List.add(StatCollector.translateToLocal("item.spelunker:goldenStatueF.desc.hc3"));
                par3List.add(StatCollector.translateToLocal("item.spelunker:goldenStatueF.desc.hc4"));
                par3List.add(StatCollector.translateToLocal("item.spelunker:goldenStatueF.desc.hc5"));
                par3List.add(StatCollector.translateToLocal("item.spelunker:goldenStatueF.desc.hc6"));
            }
            else
            {
                par3List.add(StatCollector.translateToLocal("item.spelunker:goldenStatueF.desc"));
            }
        }
    }
}
