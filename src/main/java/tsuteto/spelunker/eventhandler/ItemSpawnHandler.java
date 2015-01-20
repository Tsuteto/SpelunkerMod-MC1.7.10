package tsuteto.spelunker.eventhandler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import tsuteto.spelunker.constants.SpelunkerDifficulty;
import tsuteto.spelunker.entity.EntitySpelunkerItem;
import tsuteto.spelunker.item.SpelunkerItem;
import tsuteto.spelunker.player.SpelunkerPlayerMP;
import tsuteto.spelunker.player.SpelunkerPlayerSP;
import tsuteto.spelunker.util.ModLog;

import java.util.List;
import java.util.Random;

/**
 * Handles spawning Spelunker drop items
 *
 * @author Tsuteto
 *
 */
public class ItemSpawnHandler
{
    private static Random rand = new Random();

    public static void onClientTick(Minecraft mc, SpelunkerPlayerSP spelunker, long worldtime)
    {
        // Item animation
        if (worldtime % 2 == 0)
        {
            ((SpelunkerItem)SpelunkerItem.itemEnergy).animate();
            ((SpelunkerItem)SpelunkerItem.itemInvincible).animate();
            ((SpelunkerItem)SpelunkerItem.itemMiracle).animate();
        }
    }

    public static void onPlayerTick(SpelunkerPlayerMP spelunker)
    {
        if (!spelunker.isInSpelunkerWorld() && spelunker.isUsingEnergy() && !spelunker.player().capabilities.disableDamage)
        {
            World world = spelunker.player().worldObj;
            long worldtime = world.getTotalWorldTime();
            int interval = (spelunker.player().dimension == 0) ? 100 : 160;
            if (worldtime % interval == 0)
            {
                SpelunkerDifficulty difficulty = SpelunkerDifficulty.getSettings(world.difficultySetting);
                int testCnt = difficulty.trials;
                while (testCnt-- > 0)
                    spawnSpelunkerItems(spelunker, world, difficulty);
            }
        }
    }

    public static void spawnSpelunkerItems(SpelunkerPlayerMP spelunker, World world, SpelunkerDifficulty difficulty)
    {
        EntityPlayerMP player = spelunker.player();
        double x = (rand.nextDouble() - rand.nextDouble()) * 30.0D + player.posX;
        double y = (rand.nextDouble() - rand.nextDouble()) * 6.0D - 3.0D + player.posY;
        double z = (rand.nextDouble() - rand.nextDouble()) * 30.0D + player.posZ;
        if (y < 5.5D)
            y = 5.5D;
        int tileX = MathHelper.floor_double(x);
        int tileY = MathHelper.floor_double(y);
        int tileZ = MathHelper.floor_double(z);

        EntitySpelunkerItem spawnItem = new EntitySpelunkerItem(world);
        spawnItem.setPosition(x, y, z);
        spawnItem.rotationYaw = (float)(Math.random() * 360.0D);
        spawnItem.motionX = ((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D));
        spawnItem.motionY = 0.20000000298023224D;
        spawnItem.motionZ = ((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D));
        spawnItem.lifespan = 1200;
        spawnItem.delayBeforeCanPickup = 10;

        boolean onGround = world.isAirBlock(tileX, tileY, tileZ)
                && (!world.isAirBlock(tileX, tileY - 1, tileZ) || !world.isAirBlock(tileX, tileY - 2, tileZ));
        boolean isOutOfSight = !difficulty.spawnInSight || difficulty.spawnInSight && !player.canEntityBeSeen(spawnItem);
        // System.out.println(String.format("Light: %2d, onGround: %5s, Seen: %5s, Sky: %5s, Dim: %2d, Near: %5s",
        // world.getBlockLightValue(blockX, blockY, blockZ),
        // onGround,
        // spelunker.canEntityBeSeen(spawnItem),
        // world.canBlockSeeTheSky(blockX, blockY, blockZ),
        // spelunker.dimension,
        // isItemExistsNearby));
        if (onGround && (player.dimension == -1 || !world.canBlockSeeTheSky(tileX, tileY, tileZ))
                && isOutOfSight && !itemExistsNearby(spawnItem, world, difficulty.spawnDensity))
        {
            int lightLvl = difficulty.lightLevel;
            boolean isDarkPlace = world.getBlockLightValue(tileX, tileY, tileZ) < rand.nextInt(lightLvl / 2) + lightLvl;
            ItemStack item = null;

            if (rand.nextDouble() < difficulty.chanceOfEnergy && isDarkPlace)
            {
                item = new ItemStack(SpelunkerItem.itemEnergy);
                // System.out.println("Spawned Energy");
            }
            else
            {

	            int rarityTotal = 0;
	            for (SpelunkerItem dropItem : SpelunkerItem.dropItemRegistry)
	            {
	                if (dropItem.spawnCheck(spelunker, difficulty, isDarkPlace, y))
	                {
	                    rarityTotal += dropItem.getRarity(difficulty);
	                }
	            }

	            int itemrand = rand.nextInt(rarityTotal + difficulty.rarityNoItem);
	            int thresh = 0;

	            for (SpelunkerItem dropItem : SpelunkerItem.dropItemRegistry)
	            {
	                if (dropItem.spawnCheck(spelunker, difficulty, isDarkPlace, y))
	                {
	                    thresh += dropItem.getRarity(difficulty);
	                    if (itemrand < thresh)
	                    {
	                        item = new ItemStack(dropItem);
	                        break;
	                    }
	                }
	            }
            }

            if (item != null)
            {
            	spawnItem.setEntityItemStack(item);
                world.spawnEntityInWorld(spawnItem);
                ModLog.debug("Item spawned: " + item);
            }
        }
    }

    private static boolean itemExistsNearby(EntitySpelunkerItem spawnItem, World world, double density)
    {
        List<Entity> entitiesNearby = world.getEntitiesWithinAABBExcludingEntity(
                spawnItem, spawnItem.boundingBox.expand(density, 2.0D, density));
        for (Entity e : entitiesNearby)
        {
            if (e instanceof EntitySpelunkerItem)
            {
                return true;
            }
        }
        return false;
    }
}
