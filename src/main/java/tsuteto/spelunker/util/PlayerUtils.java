package tsuteto.spelunker.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.init.SpelunkerItems;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class PlayerUtils
{
    public static void giveGoldenSpelunker(EntityPlayer player, int numGs)
    {
        InventoryPlayer inventory = player.inventory;
        for (int i = 0; i < numGs; i++)
        {
            inventory.mainInventory[(i + 9) % inventory.mainInventory.length] = new ItemStack(SpelunkerItems.itemGoldenStatue);
        }
    }

    public static void updatePlayerSpawnPoint(World world, int x, int y, int z, EntityPlayer player)
    {
        ChunkCoordinates coord = new ChunkCoordinates(x, y, z);
        int dimId = world.provider.dimensionId;

        if (!coord.equals(player.getBedLocation(dimId)))
        {
            player.setSpawnChunk(coord, true, dimId);
            ModLog.debug("Player's respawn location is updated to (%d, %d, %d)", x, y, z);
        }
    }

    /**
     * From World#getClosestVulnerablePlayerToEntity()
     */
    public static EntityPlayer getClosestVulnerableSpelunkerToEntity(Entity p_72856_1_, double p_72856_2_)
    {
        return getClosestVulnerableSpelunker(p_72856_1_.worldObj, p_72856_1_.posX, p_72856_1_.posY, p_72856_1_.posZ, p_72856_2_);
    }

    /**
     * From World#getClosestVulnerablePlayer()
     */
    public static EntityPlayer getClosestVulnerableSpelunker(World world, double p_72846_1_, double p_72846_3_, double p_72846_5_, double p_72846_7_)
    {
        double d4 = -1.0D;
        EntityPlayer entityplayer = null;

        for (int i = 0; i < world.playerEntities.size(); ++i)
        {
            EntityPlayer entityplayer1 = (EntityPlayer)world.playerEntities.get(i);
            SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(entityplayer1);

            if (!entityplayer1.capabilities.disableDamage && entityplayer1.isEntityAlive() && spelunker.isInCave())
            {
                double d5 = entityplayer1.getDistanceSq(p_72846_1_, p_72846_3_, p_72846_5_);
                double d6 = p_72846_7_;

                if (entityplayer1.isSneaking())
                {
                    d6 = p_72846_7_ * 0.800000011920929D;
                }

                if (entityplayer1.isInvisible())
                {
                    float f = entityplayer1.getArmorVisibility();

                    if (f < 0.1F)
                    {
                        f = 0.1F;
                    }

                    d6 *= (double)(0.7F * f);
                }

                if ((p_72846_7_ < 0.0D || d5 < d6 * d6) && (d4 == -1.0D || d5 < d4))
                {
                    d4 = d5;
                    entityplayer = entityplayer1;
                }
            }
        }

        return entityplayer;
    }
}
