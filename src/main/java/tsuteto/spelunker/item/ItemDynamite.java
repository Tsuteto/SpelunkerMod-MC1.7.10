package tsuteto.spelunker.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tsuteto.spelunker.entity.EntityDynamitePrimed;

public class ItemDynamite extends Item
{
    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
            Block block = world.getBlock(x, y, z);
            x += Facing.offsetsXForSide[side];
            y += Facing.offsetsYForSide[side];
            z += Facing.offsetsZForSide[side];

            if (block.isSideSolid(world, x, y, z, ForgeDirection.DOWN))
            {
                Entity entity = new EntityDynamitePrimed(world, (double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, player);
                world.spawnEntityInWorld(entity);
                world.playSoundAtEntity(entity, "game.tnt.primed", 1.0F, 1.0F);

                if (!player.capabilities.isCreativeMode)
                {
                    --itemStack.stackSize;
                }
            }
            return true;
        }
    }
}
