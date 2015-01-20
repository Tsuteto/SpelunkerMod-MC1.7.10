package tsuteto.spelunker.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.item.SpelunkerItem;

import java.net.URL;

public class Utils
{
    public static final int[] vanillaColors = new int[]{
            0,
            170,
            43520,
            43690,
            11141120,
            11141290,
            16755200,
            11184810,
            5592405,
            5592575,
            5635925,
            5636095,
            16733525,
            16733695,
            16777045,
            16777215
    };

    public static int determineBlockOrientation6(World world, int x, int y, int z, EntityLivingBase entity)
    {
        // Following vanilla
        return BlockPistonBase.determineOrientation(world, x, y, z, entity);
    }

    public static AxisAlignedBB getTorchBlockBounds(World par1World, int par2, int par3, int par4)
    {
        int l = par1World.getBlockMetadata(par2, par3, par4) & 7;
        float f = 0.15F;
        AxisAlignedBB box;

        if (l == 1)
        {
            box = AxisAlignedBB.getBoundingBox(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
        }
        else if (l == 2)
        {
            box = AxisAlignedBB.getBoundingBox(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
        }
        else if (l == 3)
        {
            box = AxisAlignedBB.getBoundingBox(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
        }
        else if (l == 4)
        {
            box = AxisAlignedBB.getBoundingBox(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
        }
        else
        {
            f = 0.1F;
            box = AxisAlignedBB.getBoundingBox(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f);
        }

        return box.offset(par2, par3, par4);
    }

    public static AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, Block block, int par2, int par3, int par4)
    {
        return AxisAlignedBB.getBoundingBox(
                par2 + block.getBlockBoundsMinX(),
                par3 + block.getBlockBoundsMinY(),
                par4 + block.getBlockBoundsMinZ(),
                par2 + block.getBlockBoundsMaxX(),
                par3 + block.getBlockBoundsMaxY(),
                par4 + block.getBlockBoundsMaxZ());
    }

    public static Vec3 getHeadLook(EntityLivingBase entity)
    {
        float f = MathHelper.cos(-entity.rotationYawHead * 0.01745329F - (float) Math.PI);
        float f2 = MathHelper.sin(-entity.rotationYawHead * 0.01745329F - (float) Math.PI);
        float f4 = -MathHelper.cos(-entity.rotationPitch * 0.01745329F);
        float f6 = MathHelper.sin(-entity.rotationPitch * 0.01745329F);
        return Vec3.createVectorHelper(f2 * f4, f6, f * f4);
    }

    public static void giveGoldenSpelunker(EntityPlayer player, int numGs)
    {
        InventoryPlayer inventory = player.inventory;
        for (int i = 0; i < numGs; i++)
        {
            inventory.mainInventory[(i + 9) % inventory.mainInventory.length] = new ItemStack(SpelunkerItem.itemGoldenStatue);
        }
    }

    public static long generateRandomFromCoord(int x, int y, int z)
    {
        long i1 = (long) (x * 3129871) ^ (long) y * 116129781L ^ (long) z;
        i1 = i1 * i1 * 42317861L + i1 * 11L + i1;
        return i1 >> 16;
    }

    public static boolean soundFileExists(String filename)
    {
        for (String ext : new String[]{"ogg"})
        {
            String rsrcpath = String.format("/assets/spelunker/sounds/%s.%s", filename, ext);
            URL url = SpelunkerMod.class.getResource(rsrcpath);
            if (url != null)
            {
                return true;
            }
        }
        return false;
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
}
