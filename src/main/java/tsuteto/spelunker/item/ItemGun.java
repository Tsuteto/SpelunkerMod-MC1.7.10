package tsuteto.spelunker.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.*;
import net.minecraft.world.World;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.entity.EntityGunBullet;
import tsuteto.spelunker.player.ISpelunkerPlayer;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

import java.util.List;

public class ItemGun extends ItemBow
{
    private final EnumGunMaterial gunMaterial;

    public ItemGun(EnumGunMaterial gunMaterial)
    {
        super();
        this.gunMaterial = gunMaterial;
        maxStackSize = 1;
        if (!this.isUnlimited())
        {
            setMaxDamage(gunMaterial.getMaxUses());
        }
    }

    @Override
    public Item setUnlocalizedName(String par1Str)
    {
        this.setTextureName(par1Str);
        return super.setUnlocalizedName(par1Str);
    }

    public boolean isUnlimited()
    {
        return gunMaterial.getMaxUses() == -1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister)
    {
            this.itemIcon = par1IconRegister.registerIcon(this.getIconString());
    }

    public boolean canUseGun(EntityPlayer player, ISpelunkerPlayer spelunker)
    {
        return player.capabilities.isCreativeMode || spelunker.isUsingEnergy();
    }

    @Override
    public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
    {
        if (entity != null && entity instanceof EntityPlayerMP && gunMaterial.isAutoFire())
        {
            EntityPlayerMP player = ((EntityPlayerMP) entity);
            SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(player);
            if (spelunker == null) return;

            if (player.getCurrentEquippedItem() == itemstack && player.isUsingItem())
            {
                if (--spelunker.gunInUseCount % gunMaterial.getFireInterval() == 0)
                {
                    trigger(itemstack, world, spelunker, player);
                }

                // When getting out of the cave
                if (!this.canUseGun(player, spelunker)) player.stopUsingItem();
            }
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is
     * pressed. Args: itemStack, world, entityPlayer
     */
    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        ISpelunkerPlayer spelunker = SpelunkerMod.getSpelunkerPlayer(par3EntityPlayer);

        if (spelunker != null
                && par3EntityPlayer.getCurrentEquippedItem() == par1ItemStack
                && this.canUseGun(par3EntityPlayer, spelunker))
        {
            par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
            spelunker.setGunInUseCount(getMaxItemUseDuration(par1ItemStack));

            if (!par2World.isRemote)
            {
                spelunker.setGunDamageCount(0);
            }
        }

        return par1ItemStack;
    }

    /**
     * called when the player releases the use item button. Args: itemstack,
     * world, entityplayer, itemInUseCount
     */
    @Override
    public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer entityPlayer, int par4)
    {
        if (entityPlayer instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) entityPlayer;
            SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(player);

            if (spelunker != null && player.getCurrentEquippedItem() == itemstack)
            {
                if (!gunMaterial.isAutoFire() && par4 > gunMaterial.getFireInterval())
                {
                    trigger(itemstack, world, spelunker, player);
                }
                spelunker.gunInUseCount = 0;
                if (!world.isRemote)
                {
                    if (!this.isUnlimited())
                    {
                        itemstack.damageItem(spelunker.gunDamageCount, entityPlayer);
                    }
                    spelunker.gunDamageCount = 0;
                }
            }
        }
    }

    public void trigger(ItemStack itemstack, World world, SpelunkerPlayerMP spelunker, EntityPlayerMP entityPlayer)
    {
        if (itemstack != null && this.canUseGun(entityPlayer, spelunker))
        {
            if (fire(itemstack, world, spelunker, entityPlayer, itemRand.nextFloat() * -0.3F))
            {
                spelunker.decreaseEnergy(gunMaterial.getGunfireCost());
            }
            // Break the gun
            // System.out.println(String.format("gunDamageCount=%d, useLeft=%d",
            // spelunker.gunDamageCount, itemstack.getMaxDamage() -
            // itemstack.getItemDamage()));
            if (!world.isRemote && spelunker.gunDamageCount >= itemstack.getMaxDamage() - itemstack.getItemDamage())
            {
                entityPlayer.stopUsingItem();
            }
            if (itemstack.stackSize == 0)
            {
                entityPlayer.inventory.mainInventory[entityPlayer.inventory.currentItem] = null;
            }
        }
    }

    public boolean fire(ItemStack itemstack, World world, SpelunkerPlayerMP spelunker, EntityPlayer entityplayer,
            float rand)
    {
        // For enchantment
        if (!world.isRemote)
        {
            EntityGunBullet entityBullet = new EntityGunBullet(world, entityplayer, gunMaterial, rand);
            // Power
            int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemstack);
            if (k > 0)
            {
                entityBullet.setDamageMultiplier(entityBullet.getDamageMultiplier() + (double) k
                        * gunMaterial.getEnchantability());
            }
            // Punch
            int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, itemstack);
            if (l > 0)
            {
                entityBullet.setKnockBack(l);
            }
            // Fire
            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, itemstack) > 0)
            {
                entityBullet.setFire(100);
            }
            world.spawnEntityInWorld(entityBullet);
        }

        if (world.isRemote && !this.isUnlimited())
        {
            itemstack.damageItem(1, entityplayer);
        }
        if (!world.isRemote)
        {
            spelunker.gunDamageCount++;
        }
        world.playSoundAtEntity(entityplayer, "spelunker:gunshot", 1.0F, 1.0F);

        // For Infinity. Returns false if Infinity is enchanted
        return (EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, itemstack) <= 0)
                && !entityplayer.capabilities.isCreativeMode;
    }

    public ItemStack onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        return par1ItemStack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.bow;
    }

    /**
     * Return the enchantability factor of the item, most of the time is based
     * on material.
     */
    @Override
    public int getItemEnchantability()
    {
        return this.gunMaterial.getEnchantability();
    }

    @Override
    public EnumRarity getRarity(ItemStack p_77613_1_)
    {
        switch (this.gunMaterial)
        {
            case ORIGINAL:
                return EnumRarity.rare;
            default:
                return EnumRarity.common;
        }
    }

    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_)
    {
        switch (this.gunMaterial)
        {
            case ORIGINAL:
                p_77624_3_.add(I18n.format("item.spelunker:spelunkerGun.org.info"));
                break;
            case SPE_WORLD:
                p_77624_3_.add(I18n.format("item.spelunker:spelunkerGun.sw.info"));
                break;
        }
    }
}
