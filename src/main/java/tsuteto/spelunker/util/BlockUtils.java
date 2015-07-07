package tsuteto.spelunker.util;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockUtils
{
    @SidedProxy(
            serverSide = "tsuteto.spelunker.util.BlockUtils$CommonProxy",
            clientSide = "tsuteto.spelunker.util.BlockUtils$ClientProxy")
    private static CommonProxy proxy;


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

    public static void setInvisibleBlockBounds(Block block)
    {
        proxy.setBlockBounds(block);
    }

    public static class CommonProxy
    {
        public void setBlockBounds(Block block) {}
    }

    public static class ClientProxy extends CommonProxy
    {
        public void setBlockBounds(Block block)
        {
            if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            {
                EntityPlayer player = cpw.mods.fml.client.FMLClientHandler.instance().getClientPlayerEntity();
                if (!player.capabilities.isCreativeMode)
                {
                    block.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
                }
                else
                {
                    block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                }
            }
        }
    }
}
