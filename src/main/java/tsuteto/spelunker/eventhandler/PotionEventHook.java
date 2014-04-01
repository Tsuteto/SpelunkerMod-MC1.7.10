package tsuteto.spelunker.eventhandler;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.damage.SpelunkerDamageSource;
import tsuteto.spelunker.player.SpelunkerPlayerMP;
import tsuteto.spelunker.potion.SpelunkerPotion;
import tsuteto.spelunker.util.ModLog;

public class PotionEventHook
{
    public static void onEaten(ItemStack itemstack, World world, EntityPlayer player)
    {
        if (!world.isRemote)
        {
            SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(player);

            if (spelunker != null && itemstack.getItemDamage() == 0)
            {
                player.removePotionEffect(SpelunkerPotion.choked.id);
                player.removePotionEffect(SpelunkerPotion.heatStroke.id);
                spelunker.getSpelunkerExtra().resetSunHeatCount();
            }

            if (spelunker != null && spelunker.isHardcore())
            {
                List<PotionEffect> list = Items.potionitem.getEffects(itemstack);

                applySideEffect(list, 1.0D, player, world);
            }
        }
    }

    public static void onSplashPotionImpact(EntityPotion splashPotion, List<PotionEffect> potionEffects, EntityLivingBase entity, double factor)
    {
        World world = entity.worldObj;

        if (!world.isRemote && entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)entity;
            SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(player);

            if (spelunker != null && spelunker.isHardcore())
            {
                applySideEffect(potionEffects, factor, player, world);
            }
        }
    }

    private static void applySideEffect(List<PotionEffect> potionEffects, double healFactor, EntityPlayer player, World world)
    {
        if (potionEffects != null)
        {
            Random rand = world.rand;
            int effectStrength = 0;
            for (PotionEffect e : potionEffects)
            {
                effectStrength += e.getAmplifier() + 1;
                effectStrength += e.getDuration() > 1200 ? 1 : 0;

                if (Potion.potionTypes[e.getPotionID()].isInstant() && healFactor < 0.5D) effectStrength /= 2;
            }
            effectStrength *= effectStrength;
            ModLog.debug("effectStrength: %d", effectStrength);

            if (effectStrength > 2 && rand.nextInt(10) < effectStrength - 2)
            {
                player.attackEntityFrom(SpelunkerDamageSource.sideEffect, (float)Math.min(Float.MAX_VALUE, rand.nextDouble() * effectStrength + 1));
            }
        }
    }
}
