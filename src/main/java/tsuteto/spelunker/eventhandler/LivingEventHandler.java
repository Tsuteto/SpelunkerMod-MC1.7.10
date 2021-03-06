package tsuteto.spelunker.eventhandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.achievement.AchievementMgr;
import tsuteto.spelunker.damage.SpelunkerDamageSource;
import tsuteto.spelunker.entity.BatDroppingsHandler;
import tsuteto.spelunker.entity.EntityGhost;
import tsuteto.spelunker.entity.EntityGunBullet;
import tsuteto.spelunker.init.SpeAchievementList;
import tsuteto.spelunker.init.SpelunkerItems;
import tsuteto.spelunker.item.EnumGunMaterial;
import tsuteto.spelunker.item.ItemHelmet;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

/**
 * Handles events on EntityLiving
 *
 * @author Tsuteto
 *
 */
public class LivingEventHandler
{
    private BatDroppingsHandler batHandler = BatDroppingsHandler.forNormalBats();

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
    {
        EntityLivingBase entity = event.entityLiving;

        // Normal Bats
        if (entity instanceof EntityBat)
        {
            batHandler.onEntityUpdate(event.entityLiving);
        }

        // Helmet handling
        int helmetInterval = entity.worldObj.isRemote ?
                SpelunkerMod.settings().clientHelmetLightInterval
                : SpelunkerMod.settings().serverHelmetLightInterval;

        if (entity.ticksExisted % helmetInterval == 0)
        {
            boolean isHelmetLighting = false;

            if (entity instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer)event.entityLiving;
                InventoryPlayer var5 = player.inventory;
                ItemStack var6 = var5.armorItemInSlot(3);

                if (var6 != null && var6.getItem() == SpelunkerItems.itemHelmet)
                {
                    isHelmetLighting = true;
                }
            }
            else
            {
                ItemStack var6 = entity.getEquipmentInSlot(1);

                if (var6 != null && var6.getItem() == SpelunkerItems.itemHelmet)
                {
                    isHelmetLighting = true;
                }
            }

            if (isHelmetLighting)
            {
                ((ItemHelmet) SpelunkerItems.itemHelmet).lightTarget(entity.worldObj, entity);
            }
            else
            {
                ((ItemHelmet) SpelunkerItems.itemHelmet).removeLight(entity.worldObj, entity);
            }
        }
    }

    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event)
    {
        EntityLivingBase living = event.entityLiving;
        DamageSource source = event.source;

        // Ghost is forced to disappear
        if ("mob".equals(source.getDamageType()) && source.getEntity() instanceof EntityGhost)
        {
            source.getEntity().setDead();
        }
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event)
    {
        EntityLivingBase living = event.entityLiving;

        if (!living.worldObj.isRemote)
        {
            // Pet killed
            if (living instanceof IEntityOwnable)
            {
                IEntityOwnable tameable = (IEntityOwnable)living;
                Entity owner = tameable.getOwner();
                if (owner instanceof EntityPlayerMP)
                {
                    SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(((EntityPlayer)owner));
                    if (spelunker != null && spelunker.isHardcore())
                    {
                        spelunker.killInstantly(SpelunkerDamageSource.causeDamageOfPetKilled(living));
                    }
                }
            }

            if (living instanceof EntityGhost || living instanceof EntityGhast)
            {
                Entity shooter = event.source.getEntity();
                Entity bullet = event.source.getSourceOfDamage();
                if (bullet instanceof EntityGunBullet
                        && ((EntityGunBullet) bullet).getGunMaterial() == EnumGunMaterial.ORIGINAL)
                {
                    ItemStack helmet = ((EntityPlayer) shooter).getCurrentArmor(3);
                    if (helmet != null && helmet.getItem() == SpelunkerItems.itemHelmet)
                    {
                        AchievementMgr.achieve((EntityPlayer) shooter, SpeAchievementList.Key.mcSpelunker);
                    }
                }
            }
        }
    }
}
