package tsuteto.spelunker.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Handles bat droppings behavior
 * @author Tsuteto
 *
 */
public class BatDroppingsHandler
{
    private static BatDroppingsHandler normalBats = new BatDroppingsHandler(63);
    private static BatDroppingsHandler stillBats = new BatDroppingsHandler(15);

    private int cycleDuration;
    private int[] droppingDelay = new int[]{0, 2, 4};
    private Random rand = new Random();

    private BatDroppingsHandler(int cycleDuration)
    {
        this.cycleDuration = cycleDuration;
    }

    public void onEntityUpdate(EntityLivingBase bat)
    {
        if (!bat.worldObj.isRemote && bat.isEntityAlive())
        {
            int droppingTick = bat.ticksExisted & cycleDuration;
            boolean isSpawnTick = false;

            for (int delay : droppingDelay)
            {
                if (droppingTick == delay)
                {
                    isSpawnTick = true;
                    break;
                }
            }

            if (isSpawnTick)
            {
                boolean isPlayerComing = bat.worldObj.getClosestPlayer(bat.posX, bat.posY - 10.0D, bat.posZ, 32.0D) != null;
                if (isPlayerComing)
                {
                    EntityBatDroppings dropping = new EntityBatDroppings(bat.worldObj, bat);
                    bat.worldObj.spawnEntityInWorld(dropping);
                }
            }
        }
    }

    public void onServerTick(World world)
    {
        if (rand.nextInt(200) == 0)
        {
            for (int i = 0; i < droppingDelay.length; i++)
            {
                droppingDelay[i] += rand.nextInt(2 + i * 2);
                droppingDelay[i] &= cycleDuration;
            }
            //ModLog.debug("Delay Updated: " + Arrays.toString(droppingDelay));
        }
    }

    public static BatDroppingsHandler forNormalBats()
    {
        return normalBats;
    }
    public static BatDroppingsHandler forStillBats()
    {
        return stillBats;
    }
}
