package tsuteto.spelunker;

import java.util.List;
import java.util.Random;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import tsuteto.spelunker.entity.EntityBatDroppings;

/**
 * Handles bat droppings behavior
 * @author Tsuteto
 *
 */
public class BatDroppingsHandler
{
    private static BatDroppingsHandler instance = new BatDroppingsHandler();

    private int[] droppingDelay = new int[]{0, 2, 4};
    private Random rand = new Random();

    private BatDroppingsHandler()
    {
    }

    public void onEntityUpdate(EntityBat bat)
    {
        if (!bat.worldObj.isRemote)
        {
            boolean isPlayerComing = false;

            AxisAlignedBB searchArea = bat.boundingBox.expand(20D, 20D, 20D).offset(0D, -10D, 0D);
            List<Entity> list = bat.worldObj.getEntitiesWithinAABBExcludingEntity(bat, searchArea, new IEntitySelector()
            {
                @Override
                public boolean isEntityApplicable(Entity entity)
                {
                    return entity instanceof EntityPlayer;
                }

            });

            isPlayerComing = list.size() > 0;

            int droppingTick = bat.ticksExisted & 63;
            boolean isSpawnTick = false;

            for (int delay : droppingDelay)
            {
                if (droppingTick == delay)
                {
                    isSpawnTick = true;
                    break;
                }
            }

            if (isPlayerComing && isSpawnTick)
            {
                EntityBatDroppings dropping = new EntityBatDroppings(bat.worldObj, bat);
                bat.worldObj.spawnEntityInWorld(dropping);
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
                droppingDelay[i] &= 63;
            }
            //ModLog.debug("Delay Updated: " + Arrays.toString(droppingDelay));
        }
    }

    public static BatDroppingsHandler getInstance()
    {
        return instance;
    }
}
