package tsuteto.spelunker.player;

import api.player.client.ClientPlayerAPI;
import api.player.client.ClientPlayerBase;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFireworkSparkFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Session;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.constants.SpelunkerGameMode;
import tsuteto.spelunker.constants.SpelunkerPacketType;
import tsuteto.spelunker.data.ScoreManager;
import tsuteto.spelunker.network.SpelunkerPacketDispatcher;
import tsuteto.spelunker.sound.ModSound;
import tsuteto.spelunker.sound.SpelunkerBgm;
import tsuteto.spelunker.util.ModLog;
import tsuteto.spelunker.util.WatchBool;
import tsuteto.spelunker.world.WorldProviderSpelunker;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Implements the Spelunker on client side by PlayerAPI
 *
 * @author Tsuteto
 *
 */
public class SpelunkerPlayerSP extends ClientPlayerBase implements ISpelunkerPlayer
{
    public SpelunkerGameMode gameMode;
    public boolean hardcore;
    public int energy;
    public int maxEnergy;
    public int deaths;
    public int livesLeft;

    public int gunInUseCount = 0;
    public int gunDamageCount = 0;
    public int energyAlertTime;
    public int particlesChecked = 0;

    public ScoreManager spelunkerScore = new ScoreManager();
    public ISpelunkerExtraSP spelunkerExtra = new SpelunkerExtraBlankSP();

    public int timeLvlupIndicator;
    public boolean isReady = false;
    public boolean isInitializing = false;
    private boolean hasStartedGameStable = false;
    private boolean isDigging = false;

    private boolean isUsingEnergy = false;
    private WatchBool statInCave = new WatchBool(false);

    public boolean is2xScore = false;
    private WatchBool stat2xScore = new WatchBool(false);

    public boolean isSpeedPotion = false;
    private WatchBool statSpeedPotion = new WatchBool(false);

    public boolean isInvincible = false;
    private WatchBool statInvincible = new WatchBool(false);

    public boolean isGhostComing = false;
    private WatchBool statGhostComing = new WatchBool(false);

    // For rope action
    public boolean isGrabbingRope = false;
    public int ropeJumpToggleTimer = 0;
    public boolean prevJumpping = false;
    public boolean isRopeJumpping = false;

    public static Field fldEffectRendererFxLayers = ReflectionHelper.findField(EffectRenderer.class, "field_78876_b", "fxLayers");
    public static Field fldIsHittingBlock = ReflectionHelper.findField(PlayerControllerMP.class, "field_78778_j", "isHittingBlock");

    public SpelunkerPlayerSP(ClientPlayerAPI var1)
    {
        super(var1);
    }

    public int getMaxEnergy()
    {
        return this.maxEnergy;
    }

    @Override
    public void afterLocalConstructing(Minecraft mc, World world, Session session, int var4)
    {
        ModSound.stopCurrentBgm();
    }

    @Override
    public void onLivingUpdate()
    {
        Minecraft mc = FMLClientHandler.instance().getClient();

        if (!isReady && !isInitializing)
        {
            new SpelunkerPacketDispatcher(SpelunkerPacketType.INIT).sendPacketToServer();
            isInitializing = true;
        }

        spelunkerExtra.beforeOnLivingUpdate();

        super.onLivingUpdate();

        try
        {
            this.isDigging = fldIsHittingBlock.getBoolean(mc.playerController);
        }
        catch (Exception e)
        {
            ModLog.debug("Reflection error: isHittingBlock");
        }

        if (!isReady || !player.isEntityAlive())
        {
            player.motionX = 0D;
            player.motionY = 0D;
            player.motionZ = 0D;
        }

        if (!isReady || player.ticksExisted <= 60)
        {
            mc.playerController.resetBlockRemoving();
        }

        // Hitting block
        if (isDigging) // isHittingBlock
        {
            this.spelunkerExtra.onPlayerDigging();
        }
        else
        {
            this.spelunkerExtra.onPlayerNotDigging();
        }

        // Score tick
        spelunkerScore.onTick();

        if (timeLvlupIndicator > -1)
            timeLvlupIndicator--;

        // Fireworks
        if (player.isEntityAlive())
        {
            try
            {
                List list = ((List[])fldEffectRendererFxLayers.get(mc.effectRenderer))[0];

                // Check particles up to 500 per tick
                for (int i = 0; i < 500; i++, particlesChecked++)
                {
                    if (particlesChecked >= list.size())
                    {
                        particlesChecked = 0;
                        break;
                    }

                    Entity entity = (Entity) list.get(particlesChecked);

                    if (entity.boundingBox.intersectsWith(player.boundingBox) && this.onParticleColision(entity))
                    {
                         break;
                    }

                }
            }
            catch (Exception e)
            {
                ModLog.debug("Reflection error: EffectRendererFxLayers");
            }
        }

        /*
         * BGM Control
         */
        if (player.isEntityAlive())
        {
            if (SpelunkerMod.isBgmMainAvailable)
            {
                if (statInCave.checkVal(isUsingEnergy()) && !isUsingEnergy())
                {
                    ModSound.stopBgm(SpelunkerBgm.bgmMain);
                }
                if (isUsingEnergy() && ModSound.getBgmNowPlaying() == null)
                {
                    ModSound.playBgm(SpelunkerBgm.bgmMain);
                }
            }

            if (SpelunkerMod.isBgm2xScoreAvailable)
            {
                if (stat2xScore.checkVal(is2xScore()))
                {
                    if (is2xScore())
                    {
                        ModSound.intrruptBgm(SpelunkerBgm.bgm2xScore);
                    }
                    else
                    {
                        ModSound.stopBgm(SpelunkerBgm.bgm2xScore);
                    }
                }
                else if (is2xScore()
                        && (ModSound.getBgmNowPlaying() == null || ModSound.getBgmNowPlaying() == SpelunkerBgm.bgmMain))
                {
                    ModSound.intrruptBgm(SpelunkerBgm.bgm2xScore);
                }
            }

            if (SpelunkerMod.isBgmInvincibleAvailable)
            {
                if (statInvincible.checkVal(isInvincible()))
                {
                    if (isInvincible())
                    {
                        ModSound.intrruptBgm(SpelunkerBgm.bgmInvincible);
                    }
                    else
                    {
                        ModSound.stopBgm(SpelunkerBgm.bgmInvincible);
                    }
                }
                else if (isInvincible()
                        && (ModSound.getBgmNowPlaying() == null || ModSound.getBgmNowPlaying() == SpelunkerBgm.bgmMain))
                {
                    ModSound.intrruptBgm(SpelunkerBgm.bgmInvincible);
                }
            }

            if (SpelunkerMod.isBgmSpeedPotionAvailable)
            {
                if (statSpeedPotion.checkVal(isSpeedPotion()))
                {
                    if (isSpeedPotion())
                    {
                        ModSound.intrruptBgm(SpelunkerBgm.bgmSpeedPotion);
                    }
                    else
                    {
                        ModSound.stopBgm(SpelunkerBgm.bgmSpeedPotion);
                    }
                }
                else if (isSpeedPotion()
                        && (ModSound.getBgmNowPlaying() == null || ModSound.getBgmNowPlaying() == SpelunkerBgm.bgmMain))
                {
                    ModSound.intrruptBgm(SpelunkerBgm.bgmSpeedPotion);
                }
            }

            if (SpelunkerMod.isBgmGhostComingAvailable)
            {
                if (statGhostComing.checkVal(isGhostComing()))
                {
                    if (isGhostComing())
                    {
                        ModSound.intrruptBgm(SpelunkerBgm.bgmGhostComing);
                    }
                    else
                    {
                        ModSound.stopBgm(SpelunkerBgm.bgmGhostComing);
                    }
                }
                else if (isGhostComing()
                        && (ModSound.getBgmNowPlaying() == null || ModSound.getBgmNowPlaying() == SpelunkerBgm.bgmMain))
                {
                    ModSound.intrruptBgm(SpelunkerBgm.bgmGhostComing);
                }
            }
        }

        // System.out.println("Underground: " + inUnderground() + ", Air: " +
        // getEnergy());

        /*
         * Elevator Control
         */
//        if (this.controlElevatorId != -1)
//        {
//            //ModLog.debug(player.movementInput.moveForward);
//            int direction;
//            if (player.isSneaking())
//            {
//                if (player.movementInput.moveForward > 0.1f)
//                {
//                    direction = 1;
//                } else if (player.movementInput.moveForward < -0.1f)
//                {
//                    direction = 2;
//                } else
//                {
//                    direction = 0;
//                }
//
//                player.setVelocity(0.0D, player.motionY, 0.0D);
//            }
//            else
//            {
//                direction = 0;
//            }
//
//            Entity entity = player.worldObj.getEntityByID(this.controlElevatorId);
//            if (entity instanceof EntityElevator)
//            {
//                EntityElevator elevator = (EntityElevator)entity;
//                if (direction == 0) elevator.setNeutral();
//                if (direction == 1) elevator.moveUp();
//                if (direction == 2) elevator.moveDown();
//                if (direction != this.prevElevatorDirection)
//                {
//                    PacketDispatcher.packet(new PacketElevatorControlSv(this.controlElevatorId, direction)).sendToServer();
//                    this.prevElevatorDirection = direction;
//                }
//            }
//        }

        // Rope Handling
        if (isGrabbingRope)
        {
            if (!isRopeJumpping)
            {
                if (player.movementInput.jump)
                {
                    player.motionY = 0.2D;
                } else if (player.isSneaking())
                {
                    player.motionY = -0.12D;
                } else
                {
                    player.motionY = 0.0D;
                }
                player.motionX *= 0.2D;
                player.motionZ *= 0.2D;
            }

            if (!prevJumpping && player.movementInput.jump)
            {
                if (ropeJumpToggleTimer == 0)
                {
                    ropeJumpToggleTimer = 7;
                }
                else
                {
                    float f = 0.3F;
                    player.motionX = (double)(-MathHelper.sin(player.rotationYaw / 180.0F * (float) Math.PI) * f);
                    player.motionZ = (double)(MathHelper.cos(player.rotationYaw / 180.0F * (float)Math.PI) * f);
                    player.motionY = 0.25D;
                    ropeJumpToggleTimer = 0;
                    isRopeJumpping = true;
                }
            }
            if (ropeJumpToggleTimer > 0)
            {
                ropeJumpToggleTimer--;
            }
            prevJumpping = player.movementInput.jump;
            isGrabbingRope = false;
        }
        else
        {
            isRopeJumpping = false;
        }


        spelunkerExtra.afterOnLivingUpdate();

        if (!hasStartedGameStable && player.onGround)
        {
            hasStartedGameStable = true;
        }
    }

    @Override
    public void afterJump()
    {
        player.playSound("spelunker:jump", getSeVolume(), 1.0f);
        decreaseEnergy(SpelunkerMod.energyCostJump);
    }

    public void hop()
    {
        player.motionY = 0.2;
        player.isAirBorne = true;
    }

    public boolean onParticleColision(Entity entity)
    {
        if (entity instanceof EntityFireworkSparkFX)
        {
            new SpelunkerPacketDispatcher(SpelunkerPacketType.DMG_FIREWORKS).sendPacketToServer();
            return true;
        }
        return spelunkerExtra.onParticleCollision(entity);
    }

    @Override
    public boolean attackEntityFrom(DamageSource dmgsrc, float amount)
    {
        // Avoid damage for 3 seconds since respawn
        if (player.ticksExisted <= 60 || isInvincible())
        {
            return false;
        }

        if (SpelunkerMod.settings().forceDeath)
        {
            return false;
        }

        // Thrown entities cause 1 damage at least
        if (dmgsrc.getDamageType().equals("thrown") && amount <= 1.0f)
        {
            amount = 1.0f;
        }

        return playerAPI.superAttackEntityFrom(dmgsrc, amount);
    }

    @Override
    public void killInstantly(DamageSource dmgsrc)
    {
    }

    @Override
    public void afterSetDead()
    {
        ModSound.stopCurrentBgm();
    }

    @Override
    public void beforeOnDeath(DamageSource damageSource)
    {
        if (isInSpelunkerWorld())
        {
            InventoryPlayer inventory = player.inventory;
            for (int i = 0; i < inventory.mainInventory.length; i++)
            {
                inventory.mainInventory[i] = null;
            }
            for (int i = 0; i < inventory.armorInventory.length; i++)
            {
                inventory.armorInventory[i] = null;
            }
        }
    }

    public void setDifficultyHardcore()
    {
        WorldInfo worldinfo = player.worldObj.getWorldInfo();
        try
        {
            ObfuscationReflectionHelper.setPrivateValue(WorldInfo.class, worldinfo, Boolean.TRUE, 20);
        }
        catch (Exception e)
        {
            ModLog.warn(e, "Failed to set the game hardcode");
        }
    }

    @Override
    public void decreaseEnergy(int i)
    {
        if (canDecreaseEnergy())
        {
            new SpelunkerPacketDispatcher(SpelunkerPacketType.ENERGY_DEC)
                    .addInt(i)
                    .sendPacketToServer();
        }
    }

    @Override
    public void addSpelunkerScore(int amount)
    {
    }

    public boolean canDecreaseEnergy()
    {
        return isUsingEnergy() && !player.capabilities.disableDamage;
    }

    @Override
    public boolean isUsingEnergy()
    {
        return (isHardcore() && isDigging) || isUsingEnergy;
    }

    public void setUsingEnergy(boolean isUsingEnergy)
    {
        this.isUsingEnergy = isUsingEnergy;
    }

    @Override
    public boolean is2xScore()
    {
        return is2xScore;
    }

    @Override
    public boolean isInvincible()
    {
        return isInvincible;
    }

    @Override
    public boolean isSpeedPotion()
    {
        return isSpeedPotion;
    }

    public boolean isGhostComing()
    {
        return isGhostComing;
    }

    private float getSoundPitch()
    {
        return 1.0F;
    }

    public float getSeVolume()
    {
        return ModSound.getBgmNowPlaying() != null ? 1.0f : 0.5f;
    }

    @Override
    public ScoreManager getSpelunkerScore()
    {
        return spelunkerScore;
    }

    @Override
    public int getGunInUseCount()
    {
        return 0;
    }

    @Override
    public void setGunInUseCount(int i)
    {
    }

    @Override
    public int getGunDamageCount()
    {
        return 0;
    }

    @Override
    public void setGunDamageCount(int i)
    {
    }

    @Override
    public void initSpelunker(GameProfile gameProfile)
    {
    }

    public EntityPlayerSP player()
    {
        return player;
    }

    /**
     * Obtains hardcore flag, client has in the player instance.
     * @return
     */
    @Override
    public boolean isHardcore()
    {
        return hardcore;
    }

    public boolean isInSpelunkerWorld()
    {
        return player.worldObj.provider instanceof WorldProviderSpelunker;
    }
}
