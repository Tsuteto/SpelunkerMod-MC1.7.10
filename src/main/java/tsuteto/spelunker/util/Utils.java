package tsuteto.spelunker.util;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.item.SpelunkerItem;
import tsuteto.spelunker.sound.SoundHandler;

import java.net.URL;

public class Utils
{
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
        return AxisAlignedBB.getAABBPool().getAABB(
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

    public static boolean soundFileExists(String filename)
    {
        for (String ext : new String[]{"ogg", "wav"})
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
}
