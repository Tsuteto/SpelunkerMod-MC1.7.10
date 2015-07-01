package tsuteto.spelunker.player;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.blockaspect.BlockAspectHC;
import tsuteto.spelunker.constants.SpelunkerPacketType;
import tsuteto.spelunker.damage.SpelunkerDamageSource;
import tsuteto.spelunker.init.SpelunkerItems;
import tsuteto.spelunker.network.SpelunkerPacketDispatcher;
import tsuteto.spelunker.potion.SpelunkerPotion;
import tsuteto.spelunker.util.BlockUtils;
import tsuteto.spelunker.util.ModLog;
import tsuteto.spelunker.util.PlayerUtils;
import tsuteto.spelunker.util.Utils;

import java.lang.reflect.Field;
import java.util.List;

public class SpelunkerHardcoreMP extends SpelunkerNormalMP
{
    private static int AWAKE_TIME_LIMIT = 18000;
    public static final Field fldSleepTimer = ReflectionHelper.findField(EntityPlayer.class, null, "field_71076_b", "sleepTimer");

    private double prevMotionY = 0;
    public boolean isRainHit;
    public boolean isSnowHit;
    public boolean isInColdPlace;
    public boolean isInHighPlace;
    public boolean isDestroyingBlock = false;
    public int lavaHeat = 0;
    public int nextLavaHeat = 0;

    public int ticksInDarkPlace = 0;
    public int ticksOnBed = 0;
    public int ticksOverload = 0;
    public int ticksHeatStroke = 0;
    public int ticksSprint = 0;
    public int ticksStarve = 0;
    public int ticksTileDamage = 0;
    public int ticksAwake = 0;

    public int hopCount = 0;
    public boolean isHopping = false;
    public boolean isSleepy = false;
    public boolean shouldCheckGS = false;

    public SpelunkerHardcoreMP(SpelunkerPlayerMP spelunker, boolean isFirstLogin)
    {
        super(spelunker);

        if (isFirstLogin)
        {
            // Give golden statues
            this.giveGoldenSpelunkers();
        }
    }

    @Override
    public void beforeOnLivingUpdate()
    {
        int px = MathHelper.floor_double(player.posX);
        int py = MathHelper.floor_double(player.posY);
        int pz = MathHelper.floor_double(player.posZ);

        // Head hitting
        int floorHeight = (int)(player.posY + player.getEyeHeight()) + 1;
        boolean isSolidBlock = player.worldObj.getBlock(px, floorHeight, pz).isNormalCube();

        if (isSolidBlock && floorHeight - player.boundingBox.maxY == 0.0D && prevMotionY == player.motionY)
        {
            if (player.inventory.getStackInSlot(36) != null && spelunker.rand.nextInt(10) == 0)
            {
                player.attackEntityFrom(SpelunkerDamageSource.neckTwisting, 2.0f);
            }
            else
            {
                player.attackEntityFrom(SpelunkerDamageSource.headHitting, 1.0f);
            }
        }
        this.prevMotionY = player.motionY;

        // Water handling
        if (player.isInWater() && !player.isRiding())
        {
            player.attackEntityFrom(SpelunkerDamageSource.water, 2.0F);
        }
    }

    @Override
    public void afterOnLivingUpdate()
    {
        int px = MathHelper.floor_double(player.posX);
        int py = MathHelper.floor_double(player.posY);
        int pz = MathHelper.floor_double(player.posZ);
        BiomeGenBase biome = player.worldObj.getBiomeGenForCoords(px, pz);

        isRainHit = player.worldObj.canLightningStrikeAt(px, py + 1, pz);
        isSnowHit = this.isSnowHit(px, py, pz);
        isInColdPlace = biome.getEnableSnow();
        isInHighPlace = player.posY + player.getEyeHeight() > 85;
        try
        {
            isDestroyingBlock = SpelunkerPlayerMP.fldIsDestroyingBlock.getBoolean(player.theItemInWorldManager);
        }
        catch (Exception e)
        {
            ModLog.debug("Reflection error: isDestroyingBlock");
        }

        // Dark place
        if (player.worldObj.getBlockLightValue(px, (int)(player.posY + player.getEyeHeight()), pz) == 0)
        {
            ticksInDarkPlace++;
            if (ticksInDarkPlace > 600)
            {
                player.attackEntityFrom(SpelunkerDamageSource.darkness, 1.0f);
                ticksInDarkPlace -= 100;
            }
        }
        else
        {
            ticksInDarkPlace = 0;
        }

        // Starving
        int difficulty = player.worldObj.difficultySetting.getDifficultyId();
        if (player.getFoodStats().getFoodLevel() <= 0
                && (player.getHealth() <= 10.0F && difficulty <= 1 || player.getHealth() <= 1.0F && difficulty == 2))
        {
            ticksStarve++;
            if (ticksStarve >= 80)
            {
                player.attackEntityFrom(DamageSource.starve, 1.0F);
                ticksStarve = 0;
            }
        }

        // Fall down by sprinting
        if (player.isSprinting())
        {
            if (spelunker.rand.nextInt(100) <= ticksSprint - 60)
            {
                isHopping = true;
            }

            if (isHopping && player.onGround)
            {
                player.motionY = 0.2;
                player.isAirBorne = true;
                new SpelunkerPacketDispatcher(SpelunkerPacketType.HOP).sendPacketPlayer(player);
                hopCount++;
            }

            if (hopCount > 3)
            {
                player.attackEntityFrom(SpelunkerDamageSource.falldown, 2.0f);
                player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 15 * 20, 2));
                hopCount = 0;
                ticksSprint = 0;
                isHopping = false;
                player.setSprinting(false);
            }
            else
            {
                ticksSprint++;
            }
        }
        else
        {
            hopCount = 0;
            ticksSprint = 0;
            isHopping = false;
        }

        // Web
        if (playerAPI.getIsInWebField())
        {
            player.attackEntityFrom(SpelunkerDamageSource.web, 0.5f);
            ticksTileDamage = 20;
        }

        int sleepTimer = 0;
        try
        {
            sleepTimer = fldSleepTimer.getInt(player);
        }
        catch (Exception e)
        {
            ModLog.warn(e, "Reflection Failure: sleepTimer");
        }

        // Hard bed
        if (sleepTimer >= 99)
        {
            try
            {
                fldSleepTimer.setInt(player, 99);
            } catch (IllegalAccessException e)
            {
                ModLog.warn(e, "Reflection Failure: sleepTimer");
            }
            //spelunker.killInstantly(SpelunkerDamageSource.hardBed);
        }

        // Sleepy
        if (!player.capabilities.disableDamage && player.isEntityAlive())
        {
            if (sleepTimer > 0)
            {
                ticksAwake = 0;
            }
            else if (!player.worldObj.isDaytime() || ticksAwake > AWAKE_TIME_LIMIT)
            {
                ticksAwake++;
            }

            if (ticksAwake > AWAKE_TIME_LIMIT && ticksAwake % 100 == 0)
            {
                player.attackEntityFrom(SpelunkerDamageSource.sleepy, 0.5F);
            }
            //ModLog.debug("ticks awaking: %d", ticksAwake);
        }

        // Overload
        if (!player.capabilities.disableDamage && (player.inventory.inventoryChanged || ticksOverload > 0 && player.ticksExisted % 20 == 0))
        {
            if (player.inventory.getFirstEmptyStack() == -1)
            {
                if (ticksOverload % 15 == 0)
                {
                    player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 15 * 20, 1));
                }
                ticksOverload++;
            }
            else
            {
                ticksOverload = 0;
            }

            if (ticksOverload > 15)
            {
                player.attackEntityFrom(SpelunkerDamageSource.overload, 1.0f);
            }
        }

        // Golden Spelunker
        if (player.inventory.inventoryChanged || shouldCheckGS)
        {
            int currGs = 0;
            for (int i = 0; i < player.inventory.mainInventory.length; ++i)
            {
                ItemStack slot = player.inventory.mainInventory[i];
                if (slot != null && slot.getItem() == SpelunkerItems.itemGoldenStatue)
                {
                    currGs++;
                }
            }
            ItemStack itemGrabbed = player.inventory.getItemStack();
            if (itemGrabbed != null && itemGrabbed.getItem() == SpelunkerItems.itemGoldenStatue)
            {
                currGs++;
            }

            if (currGs < spelunker.getWorldInfo().getGoldenSpelunkers())
            {
                spelunker.killInstantly(SpelunkerDamageSource.worthyItemDropped);
            }
            if (currGs > spelunker.getWorldInfo().getGoldenSpelunkers())
            {
                spelunker.getWorldInfo().setGoldenSpelunkers(currGs);
                spelunker.saveSpelunker();
                ModLog.info("%s obtained Golden Spelunker", player.getCommandSenderName());
            }
            shouldCheckGS = false;
        }

        // Heat Stroke
        if (!player.capabilities.disableDamage && player.ticksExisted % 20 == 1)
        {
            if (!player.isPotionActive(SpelunkerPotion.heatStroke.id)
                    && biome.getFloatTemperature(px, py, pz) > 0.9f
                    && (player.worldObj.canBlockSeeTheSky(px, py, pz) || player.worldObj.provider.hasNoSky))
            {
                int tolerability;
                if (player.getCurrentArmor(1) != null && player.getCurrentArmor(2) != null)
                {
                    tolerability = 3;
                }
                else if (player.getCurrentArmor(3) != null)
                {
                    tolerability = 300;
                }
                else
                {
                    tolerability = 30;
                }

                if (spelunker.rand.nextInt(90) <= ticksHeatStroke - tolerability)
                {
                    player.addPotionEffect(new PotionEffect(SpelunkerPotion.heatStroke.id, 1200, 0));
                }
                else
                {
                    ticksHeatStroke++;
                }
            }
            else
            {
                ticksHeatStroke = 0;
            }
        }

        // Lava heat
        if (!player.capabilities.disableDamage)
        {
            int step = (player.ticksExisted % 9) + 1;
            if (step == 1) nextLavaHeat = 0;
            for (int i = -4; i <= 4; i++)
            {
                for (int j = -4; j <= 4; j++)
                {
                    for (int k = -4; k <= 4; k++)
                    {
                        if (step != Math.abs(i) + Math.abs(j) + Math.abs(k)) continue;

                        Block block = player.worldObj.getBlock(px + i, py + j, pz + k);
                        if (block != null && block.getMaterial() == Material.lava && this.canBlockBeSeen(px + i, py + j, pz + k))
                        {
                            int d = 5 - (int)MathHelper.sqrt_float(i * i + j * j + k * k);
                            if (d > nextLavaHeat)
                            {
                                nextLavaHeat = d;
                            }
                        }
                    }
                }
            }

            if (step == 9) lavaHeat = nextLavaHeat;
        }

        // Tile collision damage
        if (ticksTileDamage == 0)
        {
            Block block = player.worldObj.getBlock(px, py, pz);
            if ((block == Blocks.tallgrass || block instanceof IPlantable) && !player.isSneaking())
            {
                // Plants
                player.attackEntityFrom(SpelunkerDamageSource.grass, 1.0f);
                ticksTileDamage = 20;
            }
            else if (player.worldObj.getBlock(px, py - 1, pz) == Blocks.soul_sand && player.onGround)
            {
                // Soul sand
                player.attackEntityFrom(SpelunkerDamageSource.soulSand, 1.0f);
                ticksTileDamage = 20;
            }
            else
            {
                this.checkBlockCollision();
            }
        }
        else if (ticksTileDamage > 0)
        {
            ticksTileDamage--;
        }

        player.inventory.inventoryChanged = false;

        // Lightning bolt
        for (Object entity : player.worldObj.weatherEffects)
        {
            if (entity instanceof EntityLightningBolt)
            {
                EntityLightningBolt lightning = (EntityLightningBolt)entity;
                float distance = spelunker.isInCave() ? 32.0F : player.worldObj.isDaytime() ? 64.0F : 80.0F;
                if (player.getDistanceToEntity(lightning) < distance)
                {
                    player.attackEntityFrom(SpelunkerDamageSource.thunderclap, 0.5f);
                }
            }
        }

        // Villager
        List<EntityVillager> villagersNearby = player.worldObj.getEntitiesWithinAABB(EntityVillager.class, player.boundingBox.expand(32.0D, 3.0D, 32.0D));
        for (EntityVillager villager : villagersNearby)
        {
            // Only EntityVillager hurts Spelunker, not ones which extend it
            if (villager.getClass() == EntityVillager.class && isEntityLookingAtPlayer(villager))
            {
                player.attackEntityFrom(SpelunkerDamageSource.villager, 0.5F);
            }
        }
    }

    private boolean isEntityLookingAtPlayer(EntityLivingBase entity)
    {
        Vec3 vec3 = Utils.getHeadLook(entity).normalize();
        Vec3 vec31 = Vec3.createVectorHelper(player.posX - entity.posX, player.boundingBox.minY + (player.height / 2.0F) - (entity.posY + entity.getEyeHeight()), player.posZ - entity.posZ);
        double d0 = vec31.lengthVector();
        vec31 = vec31.normalize();
        double d1 = vec3.dotProduct(vec31);
        return d1 > 1.0D - 0.2D / d0 && entity.canEntityBeSeen(player);
    }

    private void checkBlockCollision()
    {
        AxisAlignedBB par1AxisAlignedBB = player.boundingBox.expand(0.1D, -0.0D, 0.1D).offset(0.0F, 0.0F, 0.0F);
        World world = player.worldObj;

        int i = MathHelper.floor_double(par1AxisAlignedBB.minX);
        int j = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0D);
        int k = MathHelper.floor_double(par1AxisAlignedBB.minY);
        int l = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);
        int i1 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        int j1 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0D);

        for (int k1 = i; k1 < j; ++k1)
        {
            for (int l1 = i1; l1 < j1; ++l1)
            {
                if (world.blockExists(k1, 64, l1))
                {
                    for (int i2 = k - 1; i2 < l; ++i2)
                    {
                        Block block = world.getBlock(k1, i2, l1);
                        // Torches
                        if (block == Blocks.torch)
                        {
                            AxisAlignedBB torch = BlockUtils.getTorchBlockBounds(world, k1, i2, l1);
                            if (par1AxisAlignedBB.intersectsWith(torch))
                            {
                                player.attackEntityFrom(DamageSource.onFire, 1.0f);
                                ticksTileDamage = 20;
                                return;
                            }
                        }

                        // Vine
                        if (block == Blocks.vine)
                        {
                            block.setBlockBoundsBasedOnState(world, k1, i2, l1);
                            AxisAlignedBB bounds = BlockUtils.getCollisionBoundingBoxFromPool(world, block, k1, i2, l1);
                            if (par1AxisAlignedBB.intersectsWith(bounds))
                            {
                                player.attackEntityFrom(SpelunkerDamageSource.grass, 1.0f);
                                ticksTileDamage = 20;
                                return;
                            }
                        }

                        // Trip wire
                        if (block == Blocks.tripwire)
                        {
                            block.setBlockBoundsBasedOnState(world, k1, i2, l1);
                            AxisAlignedBB bounds = BlockUtils.getCollisionBoundingBoxFromPool(world, block, k1, i2, l1);
                            if (par1AxisAlignedBB.intersectsWith(bounds))
                            {
                                player.attackEntityFrom(SpelunkerDamageSource.falldown, 1.0f);
                                ticksTileDamage = 20;
                                return;
                            }
                        }

                        if (BlockAspectHC.applyDamage(player, block, BlockAspectHC.Aspect.Untouchable))
                        {
                            ticksTileDamage = 20;
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean attackTargetEntityWithCurrentItem(Entity entity) {
        float f = (float)player.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
        if (f <= 1.0f)
        {
            if (MinecraftForge.EVENT_BUS.post(new AttackEntityEvent(player, entity)))
            {
                return false;
            }
            ItemStack equipped = player.getCurrentEquippedItem();
            if (equipped != null) equipped.getItem().onLeftClickEntity(equipped, player, entity);
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void afterOnKillEntity(EntityLivingBase entityliving, int exp)
    {
        if (entityliving.getClass() == EntityVillager.class)
        {
            spelunker.addSpelunkerScore(1500);
        }
    }

    @Override
    public void onBlockDestroyed(int x, int y, int z, Block block) {
        BlockAspectHC.applyDamage(player, block, BlockAspectHC.Aspect.Unbreakable);
    }

    @Override
    public void drainEnergy()
    {
        int amount;
        int foodlevel = player.getFoodStats().getFoodLevel();

        if (!spelunker.isInCave() && isInColdPlace)
        {
            ItemStack boots = player.getCurrentArmor(0);
            ItemStack plate = player.getCurrentArmor(2);
            boolean isWaring = plate != null && plate.getItem() == Items.leather_chestplate
                    && boots != null && boots.getItem() == Items.leather_boots;

            if (isWaring)
            {
                amount = 1 + lavaHeat;
                return;
            }

        }

        if (foodlevel <= 0)
        {
            amount = 4;
        }
        else if (foodlevel <= 6)
        {
            amount = 3;
        }
        else if (foodlevel <= 12)
        {
            amount = 2;
        }
        else
        {
            amount = 1;
        }

        if (spelunker.aroundSurface && isSnowHit)
        {
            if (lavaHeat == 0)
            {
                amount *= 2;
            }
            else if (lavaHeat > 2)
            {
                amount += lavaHeat - 2;
            }
        }
        else
        {
            amount += lavaHeat;
        }

        spelunker.decreaseEnergy(amount);
    }

    @Override
    public boolean isUsingEnergy()
    {
        // isInColdPlace wraps isSnowHitting
        return (isDestroyingBlock || isRainHit || isInHighPlace || isInColdPlace || lavaHeat > 0 || spelunker.isInCave()) && !player.capabilities.disableDamage;
    }

    public boolean isSnowHit(int par1, int par2, int par3)
    {
        if (!player.worldObj.isRaining())
        {
            return false;
        }
        else if (!player.worldObj.canBlockSeeTheSky(par1, par2, par3))
        {
            return false;
        }
        else if (player.worldObj.getPrecipitationHeight(par1, par3) > par2)
        {
            return false;
        }
        else
        {
            BiomeGenBase biomegenbase = player.worldObj.getBiomeGenForCoords(par1, par3);
            return biomegenbase.getEnableSnow();
        }
    }

    @Override
    public void onPlayerOutOfCave()
    {
    }

    @Override
    public void onRespawn(EntityPlayer deadPlayer)
    {
        // Give golden statues
        this.giveGoldenSpelunkers();

        // Inherit awaking time
        SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(deadPlayer);
        SpelunkerHardcoreMP hardcore = spelunker.getSpelunkerExtra();
        if (hardcore.ticksAwake >= AWAKE_TIME_LIMIT)
        {
            ticksAwake = AWAKE_TIME_LIMIT - 3000;
        }
        else
        {
            ticksAwake = hardcore.ticksAwake;
        }
    }

    public void giveGoldenSpelunkers()
    {
        int goldenSpelunkers = spelunker.getWorldInfo().getGoldenSpelunkers();
        PlayerUtils.giveGoldenSpelunker(player, goldenSpelunkers);
        new SpelunkerPacketDispatcher(SpelunkerPacketType.RESET_GS)
                .addInt(goldenSpelunkers).sendPacketPlayer(player);
    }

    @Override
    public void resetSunHeatCount()
    {
        ticksHeatStroke = 0;
    }

    @Override
    public boolean isUntouchableEntity(Entity entity)
    {
        return !(entity instanceof EntityPlayer);
    }

    @Override
    public void afterReadEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        ticksInDarkPlace = nbttagcompound.getShort("Spelunker_Dark");
        ticksOnBed = nbttagcompound.getShort("Spelunker_Bed");
        ticksOverload = nbttagcompound.getShort("Spelunker_Ol");
        ticksHeatStroke = nbttagcompound.getShort("Spelunker_SHeat");
        ticksSprint = nbttagcompound.getShort("Spelunker_Sprint");
        ticksStarve = nbttagcompound.getShort("Spelunker_Starve");
        ticksAwake = nbttagcompound.getInteger("Spelunker_Awake");
    }

    @Override
    public void afterWriteEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setShort("Spelunker_Dark", (short) ticksInDarkPlace);
        nbttagcompound.setShort("Spelunker_Bed", (short) ticksOnBed);
        nbttagcompound.setShort("Spelunker_Ol", (short) ticksOverload);
        nbttagcompound.setShort("Spelunker_SHeat", (short) ticksHeatStroke);
        nbttagcompound.setShort("Spelunker_Sprint", (short) ticksSprint);
        nbttagcompound.setShort("Spelunker_Starve", (short) ticksStarve);
        nbttagcompound.setInteger("Spelunker_Awake", ticksAwake);

    }

    private boolean canBlockBeSeen(int x, int y, int z)
    {
        return player.worldObj.rayTraceBlocks(
                Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ),
                Vec3.createVectorHelper(x + 0.5D, y + 0.5D, z + 0.5D)) == null;
    }
}
