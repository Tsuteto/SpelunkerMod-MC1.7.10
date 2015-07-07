package tsuteto.spelunker.player;

import api.player.server.IServerPlayer;
import api.player.server.ServerPlayerAPI;
import api.player.server.ServerPlayerBase;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.BanEntry;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.WorldInfo;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.achievement.AchievementMgr;
import tsuteto.spelunker.constants.SpelunkerDifficulty;
import tsuteto.spelunker.constants.SpelunkerGameMode;
import tsuteto.spelunker.constants.SpelunkerPacketType;
import tsuteto.spelunker.damage.SpelunkerDamageSource;
import tsuteto.spelunker.data.*;
import tsuteto.spelunker.dimension.SpelunkerLevelInfo;
import tsuteto.spelunker.entity.EntityGhost;
import tsuteto.spelunker.entity.EntitySpelunkerItem;
import tsuteto.spelunker.entity.EntityStillBat;
import tsuteto.spelunker.eventhandler.GhostSpawnHandler;
import tsuteto.spelunker.eventhandler.ItemSpawnHandler;
import tsuteto.spelunker.init.SpeAchievementList;
import tsuteto.spelunker.init.SpelunkerItems;
import tsuteto.spelunker.network.SpelunkerPacketDispatcher;
import tsuteto.spelunker.util.ModLog;
import tsuteto.spelunker.util.WatchBool;
import tsuteto.spelunker.world.WorldProviderSpelunker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Implements the Spelunker on server side by PlayerAPI
 *
 * @author Tsuteto
 *
 */
public class SpelunkerPlayerMP extends ServerPlayerBase implements ISpelunkerPlayer
{
    public static final Field fldIsDestroyingBlock = ReflectionHelper.findField(ItemInWorldManager.class, "field_73088_d", "isDestroyingBlock");

    public static final Method mtdGetHurtSound = ReflectionHelper.findMethod(EntityPlayer.class, null, new String[]{"func_70621_aR", "getHurtSound"});
    public static final Method mtdGetSoundVolume = ReflectionHelper.findMethod(EntityLivingBase.class, null, new String[]{"func_70599_aP", "getSoundVolume"});
    public static final Method mtdGetSoundPitch = ReflectionHelper.findMethod(EntityLivingBase.class, null, new String[]{"func_70647_i", "getSoundPitch"});
    public static final Method mtdGetExperiencePoints = ReflectionHelper.findMethod(EntityLivingBase.class, null, new String[]{"func_70693_a", "getExperiencePoints"}, EntityPlayer.class);

    public ScoreManager spelunkerScore = new ScoreManager();
    public WatchBool statUnderground;
    public WatchBool statUsingEnergy;
    public Entity onceRiding;
    public int timeEnergyUpdate = 5;
    public boolean shouldDieOfCursedWords = false;

    public SpelunkerGameMode gameMode;
    public int energy;
    public int deaths;
    public int livesLeft;

    public int gunInUseCount = 0;
    public int gunDamageCount = 0;
    public int energyAlertTime;
    public boolean isRopeJumping = false;

    private SpelunkerPlayerInfo worldInfo;
    private SpelunkerSaveHandler saveHandler;

    private int time2xScore;
    private int timeInvincible;
    private int timeSpeedPotion;
    private int timeSpawnInv = 0;

    protected Random rand = new Random();
    private int prevExpLevel = -1;
    protected boolean aroundSurface = false;
    protected GhostSpawnHandler ghostSpawnHandler = new GhostSpawnHandler(this, this.rand);

    private ItemStack[] mainInventoryToCarryover;
    private ItemStack[] armorInventoryToCarryover;

    private ISpelunkerExtraMP spelunkerExtra = new SpelunkerExtraBlankMP();

    public SpelunkerPlayerMP(ServerPlayerAPI api)
    {
        super(api);

        this.updateFlgAroundSurface();

        int py = MathHelper.floor_double(player.posY);
        statUnderground = new WatchBool(this.aroundSurface && py < 64);
        statUsingEnergy = new WatchBool(statUnderground.getVal());

        energy = getMaxEnergy();

        if (!SpelunkerMod.settings().fullHP)
        {
            player.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(0.1D);
            player.setHealth(0.1f);
        }
        else
        {
            player.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
        }
    }

    @Override
    public void afterLocalConstructing(MinecraftServer server, WorldServer world, GameProfile gameProfile, ItemInWorldManager itemInWorldManager)
    {
        this.initSpelunker(world, gameProfile);
    }

    /**
     * Load Spelunker info for the world
     */
    public void initSpelunker(WorldServer world, GameProfile gameProfile)
    {
        UUID uuid = gameProfile.getId();
        try
        {
            boolean isFirstLogin;
            saveHandler = SpelunkerMod.getSaveHandler();

            // Load world info
            saveHandler.migrateSaveData(gameProfile.getName(), gameProfile.getId().toString());
            worldInfo = saveHandler.loadSpelunker(uuid.toString());

            if (worldInfo == null)
            {
                // Initialize world info
                worldInfo = new SpelunkerPlayerInfo(gameProfile);
                saveHandler.saveSpelunker(this);
                worldInfo = saveHandler.loadSpelunker(uuid.toString());

                isFirstLogin = true;
                this.setSpawnTimeInv(100);
            }
            else
            {
                isFirstLogin = false;
            }

            // Update user name
            worldInfo.setPlayerName(gameProfile.getName());

            // Load params
            this.gameMode = worldInfo.getMode();
            this.deaths = worldInfo.getDeaths();
            this.livesLeft = worldInfo.getLives();
            this.spelunkerScore.load(worldInfo);

            this.setSpelunkerExtra(isFirstLogin);
        }
        catch (Exception e)
        {
            ModLog.warn(e, "Caught an error in Spelunker initialization");
        }
    }

    public void initGhostSpawnHandler()
    {
        this.ghostSpawnHandler.initController(this, this.rand);
    }

    public int getMaxEnergy()
    {
        int lvlBonus = 0;
        if (this.gameMode == SpelunkerGameMode.Adventure)
        {
            lvlBonus = Math.min(player.experienceLevel * 400, 8400);
        }
        return 3600 + lvlBonus;
    }

    @Override
    public void onLivingUpdate()
    {
        this.spelunkerExtra.beforeOnLivingUpdate();

        this.checkSpelunkerLevelStarting();

        // Give an instant death on fall
        //ModLog.debug("fallDistance: %.2f", player.fallDistance);
        if (player.fallDistance > 1.5f)
        {
            this.killInstantly(DamageSource.fall);
        }

        this.updateFlgAroundSurface();

        if (player.isInsideOfMaterial(Material.water))
        {
            player.setAir(-19);
        }

        if (!isInCave())
        {
            chkStatUndergroundTrue();
        }
        else
        {
            chkStatUndergroundFalse();
        }

        if (statUsingEnergy.checkVal(isUsingEnergy()))
        {
            if (statUsingEnergy.getVal())
            {
                new SpelunkerPacketDispatcher(SpelunkerPacketType.IN_CAVE_TRUE).sendPacketPlayer(player);
            }
            else
            {
                this.onPlayerOutOfCave();
                new SpelunkerPacketDispatcher(SpelunkerPacketType.IN_CAVE_FALSE).sendPacketPlayer(player);
            }
        }
        //ModLog.debug("aroundSurface=%s, isInCave=%s, statDrainEnergy=%s%n", aroundSurface, isInCave(), statDrainEnergy.getVal());

        if (statUsingEnergy.getVal() && player.isEntityAlive())
        {
            this.drainEnergy();

            if (energy < 0)
            {
                player.attackEntityFrom(SpelunkerDamageSource.drained, 2.0f);
                this.setEnergy(40);
            }

            // Energy alert
            if (energy < 600 && this.energyAlertTime <= 0)
            {
                this.playSound("spelunker:energyalert", 0.8f, 1.0f);
                this.energyAlertTime = 8;
            }
            else
            {
                this.energyAlertTime--;
            }

        }

        if (time2xScore == 1)
        {
            new SpelunkerPacketDispatcher(SpelunkerPacketType.OUT_OF_2x).sendPacketPlayer(player);
        }

        if (timeInvincible == 1)
        {
            new SpelunkerPacketDispatcher(SpelunkerPacketType.OUT_OF_INVINCIBLE).sendPacketPlayer(player);
        }

        if (timeSpeedPotion == 1)
        {
            new SpelunkerPacketDispatcher(SpelunkerPacketType.OUT_OF_POTION).sendPacketPlayer(player);
        }

        this.ghostSpawnHandler.updateGhostStat();

        super.onLivingUpdate();

        if (this.is2xScore())     time2xScore--;
        if (this.isInvincible())  timeInvincible--;
        if (this.isSpeedPotion()) timeSpeedPotion--;
        if (timeEnergyUpdate > 0) timeEnergyUpdate--;

        if (player.ridingEntity != null)
        {
            onceRiding = player.ridingEntity;
        }

        // Pick up spelunker drop items
        if (player.isEntityAlive())
        {
            List var3 = player.worldObj.getEntitiesWithinAABBExcludingEntity(player,
                    player.boundingBox.expand(1.0D, 0.0D, 1.0D));

            if (var3 != null)
            {
                for (int var4 = 0; var4 < var3.size(); ++var4)
                {
                    Entity var5 = (Entity) var3.get(var4);

                    if (!var5.isDead && var5 instanceof EntitySpelunkerItem)
                    {
                        var5.onCollideWithPlayer(player);
                    }
                }
            }
        }

        // Collision with entities, referring to EntityLiving
        if (player.isEntityAlive())
        {
            List list = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.expand(0.2D, 0.5D, 0.2D));
            boolean isTouchingRiding = false;

            if (list != null)
            {
                for (int i = 0; i < list.size(); i++)
                {
                    Entity entity = (Entity) list.get(i);

                    if (entity.equals(onceRiding))
                    {
                        isTouchingRiding = true;
                        continue;
                    }

                    if (entity instanceof EntityLiving)
                    {
                        EntityLivingBase living = (EntityLiving) entity;
                        if (living.isEntityAlive() && spelunkerExtra.isUntouchableEntity(living))
                        {
                            // Mob attack causes NO DAMAGE on Peaceful difficulty as of 1.3.2
                            player.attackEntityFrom(DamageSource.causeMobDamage((EntityLiving) entity), 2);
                        }
                    }
                    else if (entity instanceof EntityItem && entity.motionY < -0.40D)
                    {
                        player.attackEntityFrom(SpelunkerDamageSource.causeFallingItemDamage(entity), 1);
                    }
                    else if (entity instanceof EntityFireworkRocket)
                    {
                        player.attackEntityFrom(SpelunkerDamageSource.onFire, 1);
                    }
                }
            }

            if (onceRiding != null && !isTouchingRiding)
            {
                onceRiding = null;
            }
        }

        // Level
        if (prevExpLevel == -1)
        {
            prevExpLevel = player.experienceLevel;
        }
        else if (player.experienceLevel > prevExpLevel)
        {
            this.onLevelIncreasing();
            prevExpLevel = player.experienceLevel;
        }

        // Cursed words (HC)
        if (shouldDieOfCursedWords)
        {
            player.attackEntityFrom(SpelunkerDamageSource.cursedWords, 4.0F);
            shouldDieOfCursedWords = false;
        }

        // Spawn Spelunker items
        ItemSpawnHandler.onPlayerTick(this);

        // System.out.println("Underground: " + inUnderground() + ", Air: " + energy);

        // Spawn ghost
        this.ghostSpawnHandler.spawnCheck();

        this.spelunkerExtra.afterOnLivingUpdate();

        if (timeSpawnInv > 0)
        {
            timeSpawnInv--;
        }

        // Creative mode check
        if (player.capabilities.isCreativeMode && this.isInSpelunkerWorld())
        {
            setSpelunkerLevelCheated();
        }
    }

    public void setSpelunkerLevelCheated()
    {
        if (this.getWorldInfo().hasSpeLevelInfo())
        {
            SpeLevelPlayerInfo info = this.getWorldInfo().getSpeLevelInfo();
            if (!info.isCheated())
            {
                info.setCheated();
                SpelunkerPacketDispatcher.of(SpelunkerPacketType.SPE_CHEATED).sendPacketPlayer(player);
            }
        }
    }

    @Override
    public void attackTargetEntityWithCurrentItem(Entity var1)
    {
        if (spelunkerExtra.attackTargetEntityWithCurrentItem(var1))
        {
            super.attackTargetEntityWithCurrentItem(var1);
        }
    }

    protected void drainEnergy()
    {
        this.spelunkerExtra.drainEnergy();
    }

    protected void onPlayerOutOfCave()
    {
        this.spelunkerExtra.onPlayerOutOfCave();
    }

    @Override
    public boolean attackEntityFrom(DamageSource dmgsrc, float amount)
    {
        // Avoid damage for 3 seconds since respawn
        if (shouldAvoidDamage(dmgsrc) || isInvincible())
        {
            return false;
        }

        SpelunkerDifficulty difficulty = SpelunkerDifficulty.getSettings(player.worldObj);
        if (difficulty.allowProjectileBlocking && dmgsrc.isProjectile() && player.isBlocking())
        {
            if (difficulty.debuffOnProjectileBlocking != null)
            {
                player.addPotionEffect(difficulty.debuffOnProjectileBlocking);
            }
            return false;
        }

        if (SpelunkerMod.settings().forceDeath)
        {
            try
            {
                player.playSound((String)mtdGetHurtSound.invoke(player), (Float)mtdGetSoundVolume.invoke(player), (Float)mtdGetSoundPitch.invoke(player));
            }
            catch (Exception e) {}

            this.killInstantly(dmgsrc);
            return false;
        }

        // Projectile causes 1 damage at least
        if (dmgsrc.isProjectile() && amount < 1.0F)
        {
            amount = 1.0F;
        }

        return super.attackEntityFrom(dmgsrc, amount);
    }

    public boolean shouldAvoidDamage(DamageSource dmgsrc)
    {
        if (isWithinSpawnTimeInv() && !isInSpelunkerWorld())
        {

            if (dmgsrc instanceof SpelunkerDamageSource)
            {
                SpelunkerDamageSource spedmg = (SpelunkerDamageSource)dmgsrc;
                return spedmg.isTerrainDamage();
            }
            else
            {
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public boolean isWithinSpawnTimeInv()
    {
        return timeSpawnInv > 0;
    }

    private void checkSpelunkerLevelStarting()
    {
        // Start the Spelunker level
        if (this.isInSpelunkerWorld() && this.getWorldInfo().hasSpeLevelInfo())
        {
            SpeLevelPlayerInfo info = this.getWorldInfo().getSpeLevelInfo();
            if (!info.isStarted())
            {
                long startTime = player.worldObj.getTotalWorldTime();
                info.setStartTime(startTime);
                this.saveSpelunker();
                SpelunkerPacketDispatcher.of(SpelunkerPacketType.SPE_START)
                        .addLong(startTime)
                        .sendPacketPlayer(player);
            }
        }
    }

    @Override
    public void afterAttackTargetEntityWithCurrentItem(Entity entity)
    {
        this.decreaseEnergy(SpelunkerMod.energyCostAttack);
    }

    @Override
    public void afterOnKillEntity(EntityLivingBase entityliving)
    {
        int exp = 0;

        try
        {
            exp = (Integer)mtdGetExperiencePoints.invoke(entityliving, player);
        }
        catch (Exception ignored) {}

        if (entityliving instanceof EntityDragon)
        {
            if (player.dimension == 1 && !worldInfo.isDragonDefeated())
            {
                // Give 500000 pts. to each spelunker

                for (Object playerList : MinecraftServer.getServer().getConfigurationManager().playerEntityList)
                {
                    EntityPlayerMP player = (EntityPlayerMP) playerList;
                    SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(player);
                    if (spelunker != null)
                    {
                        SpelunkerPlayerInfo winfo = spelunker.worldInfo;
                        if (!winfo.isDragonDefeated())
                        {
                            spelunker.addSpelunkerScore(SpelunkerMod.bonusAllCleared, true);
                            winfo.setDragonDefeated();
                            spelunker.saveSpelunker();
                        }
                    }
                    AchievementMgr.achieve(player, SpeAchievementList.Key.defeatDragon);
                }
                new SpelunkerPacketDispatcher(SpelunkerPacketType.ALL_CLEARED)
                        .addInt(this.calcActualScore(SpelunkerMod.bonusAllCleared, true))
                        .sendPacketAll();
            }
            else
            {
                this.addSpelunkerScore(10000);
            }
        }
        else if (entityliving instanceof EntityStillBat)
        {
            this.addSpelunkerScore(500);
        }
        else if (entityliving instanceof EntityGhost)
        {
            this.addSpelunkerScore(2000);
        }
        else if (entityliving instanceof IMob)
        {
            if (entityliving instanceof EntityCaveSpider)
            {
                this.addSpelunkerScore(1000);
            }
            else if (entityliving instanceof EntityMagmaCube)
            {
                this.addSpelunkerScore(500);
            }
            else if (entityliving instanceof EntitySlime)
            {
                this.addSpelunkerScore(200);
            }
            else if (entityliving instanceof EntityEnderman)
            {
                this.addSpelunkerScore(700);
            }
            else if (entityliving instanceof EntitySkeleton)
            {
                int type = ((EntitySkeleton)entityliving).getSkeletonType();
                if (type == 1)
                {
                    this.addSpelunkerScore(1500);
                }
                else
                {
                    this.addSpelunkerScore(1000);
                }
            }
            else if (entityliving instanceof EntityGhast)
            {
                this.addSpelunkerScore(700);
            }
            else if (entityliving instanceof EntityWitch)
            {
                this.addSpelunkerScore(5000);
            }
            else if (entityliving instanceof EntityWither)
            {
                this.addSpelunkerScore(100000);
                AchievementMgr.achieve(player, SpeAchievementList.Key.defeatWither);
            }
            else
            {
                this.addSpelunkerScore(500);
                // Blaze: plus 500 pts. at exp bonus
            }

            this.addSpelunkerScore(Math.min(1000, Math.max(0, exp - 5) * 100));
        }
        else if (entityliving instanceof EntityBat)
        {
            this.addSpelunkerScore(500);
        }
        else if (entityliving instanceof EntityIronGolem)
        {
            this.addSpelunkerScore(5000);
        }
        else if (entityliving instanceof EntityAnimal)
        {
            addSpelunkerScore(100 + Math.max(0, (exp - 3) * 50));
        }

        spelunkerExtra.afterOnKillEntity(entityliving, exp);
    }

    @Override
    public void afterJump()
    {
        this.decreaseEnergy(SpelunkerMod.energyCostJump);
    }

    /**
     * Called when picking up spelunker items
     *
     * @param item
     * @return isSpelunkerItem
     */
    public void onSpelunkerItemPickup(EntitySpelunkerItem item)
    {
        if (!player.isDead && !player.worldObj.isRemote)
        {
            EntityTracker entitytracker = ((WorldServer)player.worldObj).getEntityTracker();
            entitytracker.func_151247_a(item, new S0DPacketCollectItem(item.getEntityId(), player.getEntityId()));
        }
    }

    public void onBlockDestroyed(int x, int y, int z, Block block)
    {
        if (block == Blocks.diamond_ore)
        {
            this.addSpelunkerScore(10000);
            this.playSound("spelunker:diamond", 1.0f, 1.0f);
        }
        else if (block == Blocks.emerald_ore)
        {
            this.addSpelunkerScore(8000);
        }
        else if (block == Blocks.lapis_ore)
        {
            this.addSpelunkerScore(5000);
        }
        else if (block == Blocks.redstone_ore || block == Blocks.lit_redstone_ore)
        {
            this.addSpelunkerScore(1500);
        }
        spelunkerExtra.onBlockDestroyed(x, y, z , block);
    }

    /**
     * Kills the Spelunker instantly
     *
     * @param dmgsrc
     */
    @Override
    public void killInstantly(DamageSource dmgsrc)
    {
        if (player.capabilities.disableDamage || shouldAvoidDamage(dmgsrc))
        {
            return;
        }

        if (player.isEntityAlive())
        {
            player.func_110142_aN().func_94547_a(dmgsrc, player.getHealth(), player.getHealth());
            player.setHealth(0);
            player.onDeath(dmgsrc);
            player.setDead();
            if (player.isPlayerSleeping() && !player.worldObj.isRemote)
            {
                // To avoid a game glitch
                player.wakeUpPlayer(true, true, false);
            }
        }
    }

    @Override
    public void beforeOnDeath(DamageSource damagesoure)
    {
        InventoryPlayer inventory = player.inventory;
        for (int i = 0; i < inventory.mainInventory.length; i++)
        {
            if (inventory.mainInventory[i] != null && inventory.mainInventory[i].getItem() == SpelunkerItems.itemGoldenStatue)
            {
                inventory.mainInventory[i] = null;
            }
        }

        // No item dropped in Spelunker worlds. Temporarily keep inventory to restore later
        if (isInSpelunkerWorld())
        {
            mainInventoryToCarryover = new ItemStack[inventory.mainInventory.length];
            armorInventoryToCarryover = new ItemStack[inventory.armorInventory.length];
            for (int i = 0; i < inventory.mainInventory.length; i++)
            {
                mainInventoryToCarryover[i] = inventory.mainInventory[i];
                inventory.mainInventory[i] = null;
            }
            for (int i = 0; i < inventory.armorInventory.length; i++)
            {
                armorInventoryToCarryover[i] = inventory.armorInventory[i];
                inventory.armorInventory[i] = null;
            }
        }
    }

    @Override
    public void afterOnDeath(DamageSource damagesource)
    {
        boolean isSinglePlayer = SpelunkerMod.isSinglePlayer();

        if (player.ticksExisted > 1)
        {
            deaths += 1;
            worldInfo.setDeaths(deaths);

            AchievementMgr.achieveDeaths(deaths, this.player());

            if (this.gameMode == SpelunkerGameMode.Adventure)
            {
                if (player.worldObj.difficultySetting.getDifficultyId() >= 2)
                {
                    worldInfo.setScore(Math.max(0, spelunkerScore.scoreActual - 1000));
                }
            }
            else
            {
                if (isSinglePlayer)
                {
                    livesLeft -= 1;
                    worldInfo.setLives(livesLeft);
                }
                else if (SpelunkerMod.getTotalLivesLeft() > 0)
                {
                    SpelunkerMod.decreaseTotalLivesLeft();
                }
                else
                {
                    // Game over
                    SpelunkerMod.setGameToBeFinished(true);
                    SpelunkerPacketDispatcher.of(SpelunkerPacketType.LIVES).addInt(-1).sendPacketPlayer(player);
                }
            }

            if (!isSinglePlayer && SpelunkerMod.getGamemodeMulti() == SpelunkerGameMode.Arcade)
            {
                if (SpelunkerMod.getTotalLivesLeft() > 0)
                {
                    new SpelunkerPacketDispatcher(SpelunkerPacketType.LIVES)
                            .addInt(SpelunkerMod.getTotalLivesLeft())
                            .sendPacketAll();
                }
            }
            else
            {
                new SpelunkerPacketDispatcher(SpelunkerPacketType.LIVES)
                        .addInt(livesLeft)
                        .sendPacketPlayer(player);
            }

            new SpelunkerPacketDispatcher(SpelunkerPacketType.DEATHS)
                    .addInt(deaths)
                    .sendPacketPlayer(player);

            this.saveSpelunker();
        }

        // Keep the dead player into storage
        SpelunkerMod.deadPlayerStorage.put(player.getUniqueID(), player);

        // Restore inventory
        if (isInSpelunkerWorld())
        {
            InventoryPlayer inventory = player.inventory;
            for (int i = 0; i < inventory.mainInventory.length; i++)
            {
                inventory.mainInventory[i] = mainInventoryToCarryover[i];
            }
            for (int i = 0; i < inventory.armorInventory.length; i++)
            {
                inventory.armorInventory[i] = armorInventoryToCarryover[i];
            }
            mainInventoryToCarryover = null;
            armorInventoryToCarryover = null;
        }

        // Play sound
        if (this.gameMode == SpelunkerGameMode.Arcade && livesLeft < 0)
        {
            this.playSound("spelunker:gameover", 1.0F, 1.0F);
        }
        else
        {
            this.playSound("spelunker:death", 1.0F, 1.0F);
        }
    }

    public void banByGameover()
    {
        MinecraftServer server = MinecraftServer.getServer();
        if (server.isSinglePlayer() && player.getCommandSenderName().equals(server.getServerOwner()))
        {
            player.playerNetServerHandler.kickPlayerFromServer("You missed too much. Game over, man, it\'s game over!");
            MinecraftServer.getServer().deleteWorldAndStopServer();
        }
        else
        {
            BanEntry banentry = new UserListBansEntry(player.getGameProfile(), null, SpelunkerMod.modId, null, "Out of lives of spelunker");
            MinecraftServer.getServer().getConfigurationManager().func_152608_h().func_152687_a(banentry);
            player.playerNetServerHandler.kickPlayerFromServer("You missed too much. Game over, man, it\'s game over!");
        }
    }

    public void setDifficultyHardcore()
    {
        WorldInfo worldinfo = player.worldObj.getWorldInfo();
        try
        {
            ObfuscationReflectionHelper.setPrivateValue(WorldInfo.class, worldinfo, true, 20);
        }
        catch (Exception e2)
        {
            player.addChatMessage(new ChatComponentTranslation("Spelunker.failedToKillWorld"));
        }
    }

    public boolean isInCave()
    {
        return statUnderground.getVal();
    }

    public int getEnergy()
    {
        return energy;
    }

    public void setEnergy(int amount)
    {
        int max = getMaxEnergy();
        energy = (amount > max) ? max : amount;

        new SpelunkerPacketDispatcher(SpelunkerPacketType.ENERGY)
                .addInt(energy)
                .sendPacketPlayer(player);
    }

    @Override
    public void decreaseEnergy(int amount)
    {
        if (isUsingEnergy())
        {
            energy -= amount;

            if (timeEnergyUpdate <= 0)
            {
                new SpelunkerPacketDispatcher(SpelunkerPacketType.ENERGY)
                        .addInt(energy)
                        .sendPacketPlayer(player);
                timeEnergyUpdate = 5;
            }
        }
    }

    @Override
    public boolean isUsingEnergy()
    {
        return spelunkerExtra.isUsingEnergy();
    }

    public void addEnergy(int i)
    {
        setEnergy(getEnergy() + i);
    }

    public int getDeaths()
    {
        return deaths;
    }

    @Override
    public void addSpelunkerScore(int amount)
    {
        this.addSpelunkerScore(amount, false);
    }

    public void addSpelunkerScore(int amount, boolean isBonus)
    {
        amount = this.calcActualScore(amount, isBonus);
        spelunkerScore.addScore(amount);

        AchievementMgr.achieveScore(spelunkerScore.scoreActual, this.player());

        if (amount > 0)
        {
            new SpelunkerPacketDispatcher(SpelunkerPacketType.SCORE)
                    .addInt(spelunkerScore.scoreActual)
                    .sendPacketPlayer(player);
        }
    }

    public int calcActualScore(int amount, boolean isBonus)
    {
        amount = (int)(amount * this.getScoreMultiplier() / 10) * 10;

        if (!isBonus && is2xScore())
        {
            amount *= 2;
        }
        return amount;
    }

    public float getScoreMultiplier()
    {
        if (player.capabilities.isCreativeMode || player.worldObj.difficultySetting == EnumDifficulty.PEACEFUL)
        {
            return 0.0f;
        }

        if (player.worldObj.difficultySetting == EnumDifficulty.EASY)
        {
            return 0.25f;
        }
        else
        {
            return 1.0f;
        }
    }

    /**
     * Referenced to EntityPlayer
     */
    public void onLevelIncreasing()
    {
        if (this.gameMode == SpelunkerGameMode.Adventure)
        {
            setEnergy(this.getMaxEnergy());

            if (player.experienceLevel <= 21)
            {
                new SpelunkerPacketDispatcher(SpelunkerPacketType.ENERGY_UP)
                        .addInt(this.getMaxEnergy())
                        .sendPacketPlayer(player);
            }
        }
    }

    public void increaseLife(int i)
    {
        if (!SpelunkerMod.isSinglePlayer() && SpelunkerMod.getGamemodeMulti() == SpelunkerGameMode.Arcade)
        {
            SpelunkerMod.increaseTotalLivesLeft(i);

            new SpelunkerPacketDispatcher(SpelunkerPacketType.LIVES)
                    .addInt(SpelunkerMod.getTotalLivesLeft())
                    .sendPacketAll();
        }
        else
        {
            livesLeft += 1;

            new SpelunkerPacketDispatcher(SpelunkerPacketType.LIVES)
                    .addInt(this.livesLeft)
                    .sendPacketPlayer(player);
        }
    }

    /**
     * Clean up items in the inventory
     */
    public void cleanupItem(Item item)
    {
        ItemStack[] mainInventory = player.inventory.mainInventory;

        for (int i = 0; i < mainInventory.length; i++)
        {
            if (mainInventory[i] != null && item == mainInventory[i].getItem())
            {
                mainInventory[i] = null;
            }
        }
    }

    public boolean chkStatUndergroundTrue()
    {
        int py = MathHelper.floor_double(player.posY);
        return statUnderground.checkVal(!this.aroundSurface && py < 63 || player.worldObj.provider.hasNoSky);
    }

    public boolean chkStatUndergroundFalse()
    {
        if (player.worldObj.provider.hasNoSky)
        {
            return statUnderground.checkVal(true);
        }

        int px = MathHelper.floor_double(player.posX);
        int py = MathHelper.floor_double(player.posY);
        int pz = MathHelper.floor_double(player.posZ);
        Chunk chunk = player.worldObj.getChunkFromBlockCoords(px, pz);
        int mapHeight = chunk.getHeightValue(px & 15, pz & 15);
        return statUnderground.checkVal(mapHeight > py);
    }

    @Override
    public void afterAddMovementStat(double d, double d1, double d2)
    {
        if (player.onGround)
        {
            int k = Math.round(MathHelper.sqrt_double(d * d + d2 * d2) * 100F);
            if (k > 0 && player.isSprinting())
            {
                this.decreaseEnergy(MathHelper.floor_float(SpelunkerMod.energyCostSprint * k));
            }
        }
    }

    public void updateFlgAroundSurface()
    {
        int px = MathHelper.floor_double(player.posX);
        int py = MathHelper.floor_double(player.posY);
        int pz = MathHelper.floor_double(player.posZ);
        Chunk chunk = player.worldObj.getChunkFromBlockCoords(px, pz);
        int mapHeight = chunk.getHeightValue(px & 15, pz & 15);
        this.aroundSurface = mapHeight - py < 15;
    }

    public void onRespawn(EntityPlayer deadPlayer)
    {
        // Inherit food status
        playerAPI.setFoodStatsField(deadPlayer.getFoodStats());
        if (player.getFoodStats().getFoodLevel() <= 0)
        {
            if (isHardcore())
            {
                player.getFoodStats().func_151686_a((ItemFood) Items.cookie, new ItemStack(Items.cookie));
            }
            else if (player.worldObj.difficultySetting == EnumDifficulty.HARD)
            {
                player.getFoodStats().func_151686_a((ItemFood) Items.bread, new ItemStack(Items.bread));
            }
        }

        // Inherit potion effect
        PotionEffect potion = deadPlayer.getActivePotionEffect(Potion.moveSpeed);
        if (potion != null && potion.getAmplifier() == 3)
        {
            player.addPotionEffect(potion);
        }

        // Restore inventory
        if (isInSpelunkerWorld())
        {
            InventoryPlayer deadInventory = deadPlayer.inventory;
            InventoryPlayer inventory = player.inventory;
            for (int i = 0; i < inventory.mainInventory.length; i++)
            {
                inventory.mainInventory[i] = deadInventory.mainInventory[i];
            }
            for (int i = 0; i < inventory.armorInventory.length; i++)
            {
                inventory.armorInventory[i] = deadInventory.armorInventory[i];
            }
        }

        this.setSpawnTimeInv(60);

        // Additional
        this.spelunkerExtra.onRespawn(deadPlayer);
    }

    @Override
    public boolean is2xScore()
    {
        return time2xScore > 0;
    }

    @Override
    public boolean isInvincible()
    {
        return timeInvincible > 0;
    }

    @Override
    public boolean isSpeedPotion()
    {
        return timeSpeedPotion > 0;
    }

    @Override
    public boolean isGhostComing()
    {
        return ghostSpawnHandler.isGhostComing();
    }

    public void set2xScore(int i)
    {
        time2xScore = i;
    }

    public void setInvincible(int i)
    {
        timeInvincible = i;
    }

    public void setSpeedPotion(int i)
    {
        timeSpeedPotion = i;
    }

    public void eliminateGhosts()
    {
        ghostSpawnHandler.eliminate();
    }

    public void saveSpelunker()
    {
        saveHandler.saveSpelunker(this);
    }

    @Override
    public void afterReadEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        setEnergy(nbttagcompound.getShort("Spelunker_CaveAir"));
        statUnderground = new WatchBool(nbttagcompound.getBoolean("Spelunker_InUnderground"));
        time2xScore = nbttagcompound.getShort("Spelunker_2x");
        timeInvincible = nbttagcompound.getShort("Spelunker_Inv");
        timeSpeedPotion = nbttagcompound.getShort("Spelunker_Speed");
        timeSpawnInv = nbttagcompound.getShort("Spelunker_Spawning");
        ghostSpawnHandler.readFromNBT(nbttagcompound);

        // if dead, keep into storage
        if (!player.isEntityAlive())
        {
            SpelunkerMod.deadPlayerStorage.put(player.getUniqueID(), player);
        }

        spelunkerExtra.afterReadEntityFromNBT(nbttagcompound);
    }

    @Override
    public void afterWriteEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setShort("Spelunker_CaveAir", (short) energy);
        nbttagcompound.setBoolean("Spelunker_InUnderground", isInCave());
        nbttagcompound.setShort("Spelunker_2x", (short) time2xScore);
        nbttagcompound.setShort("Spelunker_Inv", (short) timeInvincible);
        nbttagcompound.setShort("Spelunker_Speed", (short) timeSpeedPotion);
        nbttagcompound.setShort("Spelunker_Spawning", (short) timeSpawnInv);
        ghostSpawnHandler.writeToNBT(nbttagcompound);
        spelunkerExtra.afterWriteEntityToNBT(nbttagcompound);

        spelunkerScore.store(worldInfo);
        saveSpelunker();
    }

    public void playSound(String name, float volume, float pitch)
    {
        player.worldObj.playSoundAtEntity(player, name, volume, pitch);
    }

    /*
     * Getter methods
     */

    public EntityPlayerMP player()
    {
        return this.player;
    }

    public IServerPlayer playerAPI()
    {
        return this.playerAPI;
    }

    public SpelunkerPlayerInfo getWorldInfo()
    {
        return worldInfo;
    }

    public void setSpawnTimeInv(int ticks)
    {
        this.timeSpawnInv = ticks;
        SpelunkerPacketDispatcher.of(SpelunkerPacketType.SPAWN_INV)
                .addInt(ticks)
                .sendPacketPlayer(player);
    }

    public int getSpawnTimeInv()
    {
        return this.timeSpawnInv;
    }

    @Override
    public ScoreManager getSpelunkerScore()
    {
        return this.spelunkerScore;
    }

    public void setEnergyAlertTime(int i)
    {
        this.energyAlertTime = i;
    }

    @Override
    public int getGunInUseCount()
    {
        return this.gunInUseCount;
    }

    @Override
    public void setGunInUseCount(int i)
    {
        this.gunInUseCount = i;
    }

    @Override
    public int getGunDamageCount()
    {
        return this.gunDamageCount;
    }

    @Override
    public void setGunDamageCount(int i)
    {
        this.gunDamageCount = i;
    }

    public void addGoldenSpelunker()
    {
        this.worldInfo.addGoldenSpelunker();
        this.saveSpelunker();
        ModLog.info("%s obtained Golden Spelunker", player.getCommandSenderName());
    }

    public void resetGoldenSpelunker()
    {
        this.worldInfo.setGoldenSpelunkers(SpelunkerMod.settings().goldenSpelunkers);
        this.saveSpelunker();
        ModLog.info("%s reset Golden Spelunker", player.getCommandSenderName());

        ((SpelunkerHardcoreMP)spelunkerExtra).giveGoldenSpelunkers();
    }

    public SpeLevelRecordInfo.Record getSpeLevelRecord()
    {
        return this.getSpeLevelRecord(player.dimension);
    }

    public SpeLevelRecordInfo.Record getSpeLevelRecord(int dimId)
    {
        SpelunkerLevelInfo levelInfo = SpelunkerMod.levelManager().getLevelInfo(dimId);
        return levelInfo != null ? SpelunkerMod.recordManager().getRecord(player, levelInfo) : null;
    }

    public void registerSpeLevelRecord(int totalTime)
    {
        SpelunkerLevelInfo levelInfo = SpelunkerMod.levelManager().getLevelInfo(player.dimension);
        SpelunkerMod.recordManager().registerRecord(levelInfo, player, totalTime);
    }

    public <T extends ISpelunkerExtraMP> T getSpelunkerExtra()
    {
        return (T)this.spelunkerExtra;
    }

    public void setSpelunkerExtra(boolean isInit)
    {
        if (isHardcore())
        {
            this.spelunkerExtra = new SpelunkerHardcoreMP(this, isInit);
        }
        else
        {
            this.spelunkerExtra = new SpelunkerNormalMP(this);
        }
    }

    @Override
    public boolean isHardcore()
    {
        return SpelunkerMod.isHardcore();
    }

    public boolean isInSpelunkerWorld()
    {
        return player.worldObj.provider instanceof WorldProviderSpelunker;
    }
}
