package tsuteto.spelunker.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockRailBase;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockSpeRail extends BlockRailBase
{
    @SideOnly(Side.CLIENT)
    private IIcon iconCorner;

    public BlockSpeRail(boolean p_i45389_1_)
    {
        super(p_i45389_1_);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return p_149691_2_ >= 6 ? this.iconCorner : this.blockIcon;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        super.registerBlockIcons(p_149651_1_);
        this.iconCorner = p_149651_1_.registerIcon(this.getTextureName() + "_turned");
    }

//    protected void func_150048_a(World p_150048_1_, int p_150048_2_, int p_150048_3_, int p_150048_4_, int p_150048_5_, int p_150048_6_, Block p_150048_7_)
//    {
//        if (p_150048_7_.canProvidePower() && (new BlockRailBase.Rail(p_150048_1_, p_150048_2_, p_150048_3_, p_150048_4_)).func_150650_a() == 3)
//        {
//            this.func_150052_a(p_150048_1_, p_150048_2_, p_150048_3_, p_150048_4_, false);
//        }
//    }

    @Override
    public void onMinecartPass(World world, EntityMinecart cart, int y, int x, int z)
    {
        if (cart.riddenByEntity != null && cart.riddenByEntity instanceof EntityLivingBase)
        {
            double d7 = (double)((EntityLivingBase)cart.riddenByEntity).moveForward;

            if (Math.abs(d7) > 0.0D)
            {
                int f = d7 > 0.0D ? 1 : -1;
                double d8 = -Math.sin((double)(cart.riddenByEntity.rotationYaw * (float)Math.PI / 180.0F)) * f;
                double d9 = Math.cos((double)(cart.riddenByEntity.rotationYaw * (float)Math.PI / 180.0F)) * f;

                cart.motionX = Math.abs(d8) > 0.5D ? d8 * 0.25D : 0.0D;
                cart.motionZ = Math.abs(d9) > 0.5D ? d9 * 0.25D : 0.0D;
            }
            else
            {
                cart.motionX = 0.0D;
                cart.motionZ = 0.0D;
            }
        }
        else
        {
            cart.motionX = 0.0D;
            cart.motionZ = 0.0D;
        }
    }


}
