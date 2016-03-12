package tsuteto.spelunker.player;

import api.player.client.ClientPlayerAPI;
import api.player.client.ClientPlayerBase;
import com.google.common.collect.Lists;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiWinGame;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFireworkSparkFX;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Session;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.constants.SpelunkerGameMode;
import tsuteto.spelunker.constants.SpelunkerPacketType;
import tsuteto.spelunker.data.ScoreManager;
import tsuteto.spelunker.gui.TitleController;
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

    public int particlesChecked = 0;
    public int timeSpawnInv = 0;

    public ScoreManager spelunkerScore = new ScoreManager();
    public ISpelunkerExtraSP spelunkerExtra = new SpelunkerExtraBlankSP();

    public int timeLvlupIndicator;
    public boolean isReady = false;
    public boolean isInitializing = false;
    private boolean hasStartedGameStable = false;
    private boolean isDigging = false;

    public long speLevelStartTime = -1;
    public long speLevelFinishTime = -1;
    public int speLevelBestTime = -1;
    public boolean isBestTime = false;
    public boolean isSpeLevelCheated = false;
    public boolean isSpeLevelCleared = false;

    private boolean isInCave = false;
    private WatchBool statInCave = new WatchBool(false);

    private boolean isUsingEnergy = false;
    private WatchBool statUsingEnergy = new WatchBool(false);

    public boolean is2xScore = false;
    private WatchBool stat2xScore = new WatchBool(false);

    public boolean isSpeedPotion = false;
    private WatchBool statSpeedPotion = new WatchBool(false);

    public boolean isInvincible = false;
    private WatchBool statInvincible = new WatchBool(false);

    public List<Entity> ghosts = Lists.newArrayList();
    public boolean isGhostComing = false;
    private WatchBool statGhostComing = new WatchBool(false);

    // For rope action
    public boolean isGrabbingRope = false;
    private boolean prevGrabbingRope = false;
    public int ropeJumpToggleTimer = 0;
    public boolean prevJumpping = false;
    public boolean isRopeJumping = false;

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

        if (!isReady || timeSpawnInv > 0)
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

        if (timeSpawnInv > 0)
        {
            timeSpawnInv--;
        }

        /*
         * BGM Control
         */
        if (player.isEntityAlive())
        {
            if (SpelunkerBgm.isBgm2xScoreAvailable)
            {
                if (stat2xScore.checkVal(is2xScore()))
                {
                    if (is2xScore())
                    {
                        ModSound.interruptBgm(SpelunkerBgm.get2xScore());
                    }
                    else
                    {
                        ModSound.stopBgm(SpelunkerBgm.resLoc2xScore);
                    }
                }
                else if (is2xScore()
                        && (ModSound.getBgmNowPlaying() == null || ModSound.bgmPlayingEquals(SpelunkerBgm.resLocMain)))
                {
                    ModSound.interruptBgm(SpelunkerBgm.get2xScore());
                }
            }

            if (SpelunkerBgm.isBgmInvincibleAvailable)
            {
                if (statInvincible.checkVal(isInvincible()))
                {
                    if (isInvincible())
                    {
                        ModSound.interruptBgm(SpelunkerBgm.getInvincible());
                    }
                    else
                    {
                        ModSound.stopBgm(SpelunkerBgm.resLocInvincible);
                    }
                }
                else if (isInvincible()
                        && (ModSound.getBgmNowPlaying() == null || ModSound.bgmPlayingEquals(SpelunkerBgm.resLocMain)))
                {
                    ModSound.interruptBgm(SpelunkerBgm.getInvincible());
                }
            }

            if (SpelunkerBgm.isBgmSpeedPotionAvailable)
            {
                if (statSpeedPotion.checkVal(isSpeedPotion()))
                {
                    if (isSpeedPotion())
                    {
                        ModSound.interruptBgm(SpelunkerBgm.getSpeedPotion());
                    }
                    else
                    {
                        ModSound.stopBgm(SpelunkerBgm.resLocSpeedPotion);
                    }
                }
                else if (isSpeedPotion()
                        && (ModSound.getBgmNowPlaying() == null || ModSound.bgmPlayingEquals(SpelunkerBgm.resLocMain)))
                {
                    ModSound.interruptBgm(SpelunkerBgm.getSpeedPotion());
                }
            }

            if (SpelunkerBgm.isBgmGhostComingAvailable)
            {
                if (statGhostComing.checkVal(isGhostComing())
                        && (ModSound.getBgmNowPlaying() == null || !ModSound.bgmPlayingEquals(SpelunkerBgm.resLocCheckPoint)))
                {
                    if (isGhostComing())
                    {
                        ModSound.interruptBgm(SpelunkerBgm.getGhostComing());
                    }
                    else
                    {
                        ModSound.stopBgm(SpelunkerBgm.resLocGhostComing);
                    }
                }
                else if (isGhostComing()
                        && (ModSound.getBgmNowPlaying() == null || ModSound.bgmPlayingEquals(SpelunkerBgm.resLocMain)))
                {
                    ModSound.interruptBgm(SpelunkerBgm.getGhostComing());
                }
            }

            if (ModSound.getBgmNowPlaying() != null  && mc.currentScreen instanceof GuiWinGame)
            {
                ModSound.stopCurrentBgm();
            }
            else
            {
                if (SpelunkerBgm.isBgmMainAvailable)
                {
                    if (statInCave.checkVal(isInCave()) && !isInCave())
                    {
                        ModSound.stopBgm(SpelunkerBgm.resLocMain);
                    }
                    if (this.isInCave && ModSound.getBgmNowPlaying() == null)
                    {
                        ModSound bgm = SpelunkerBgm.getMain();
                        ModSound.playBgm(bgm);
                    }
                }
            }
        }
        else
        {
            ModSound.stopCurrentBgm();
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
            if (!isRopeJumping)
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
                player.motionX *= 0.85D;
                player.motionZ *= 0.85D;
                player.fallDistance = 0.0F;
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
                    isRopeJumping = true;
                    SpelunkerPacketDispatcher.of(SpelunkerPacketType.ROPE_JUMP)
                            .addBool(true)
                            .sendPacketToServer();
                }
            }
            if (ropeJumpToggleTimer > 0)
            {
                ropeJumpToggleTimer--;
            }
            prevJumpping = player.movementInput.jump;

            if (!prevGrabbingRope)
            {
                GameSettings gamesettings = mc.gameSettings;
                String sneakKey = GameSettings.getKeyDisplayString(gamesettings.keyBindSneak.getKeyCode());
                String jumpKey = GameSettings.getKeyDisplayString(gamesettings.keyBindJump.getKeyCode());
                TitleController.instance().setInstruction(I18n.format("Spelunker.ropeHelp", jumpKey, sneakKey));
            }
            prevGrabbingRope = true;
            isGrabbingRope = false;
        }
        else
        {
            if (isRopeJumping)
            {
                SpelunkerPacketDispatcher.of(SpelunkerPacketType.ROPE_JUMP)
                        .addBool(false)
                        .sendPacketToServer();
            }
            isRopeJumping = false;
            prevGrabbingRope = false;
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
    public void afterOnDeath(DamageSource damageSource)
    {
        ModSound.stopCurrentBgm();
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

    public boolean isInCave()
    {
        return isInCave;
    }

    public void setInCave(boolean isInCave)
    {
        this.isInCave = isInCave;
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

    public boolean isGameOver()
    {
        return livesLeft < 0 && this.gameMode == SpelunkerGameMode.Arcade;
    }

    public void setSpelunkerExtra()
    {
        if (this.hardcore)
        {
            this.spelunkerExtra = new SpelunkerHardcoreSP(this);
        }
        else
        {
            this.spelunkerExtra = new SpelunkerNormalSP(this);
        }
    }
}
