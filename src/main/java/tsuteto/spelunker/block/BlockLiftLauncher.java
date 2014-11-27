package tsuteto.spelunker.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tsuteto.spelunker.block.tileentity.TileEntityLiftLauncher;

public class BlockLiftLauncher extends BlockContainer
{
    // Upside down with ForgeDirection for inventory rendering
    public static final int DIR_UP = 0;
    public static final int DIR_DOWN = 1;

    private IIcon iconSide;

    public BlockLiftLauncher()
    {
        super(Material.iron);
    }

    @Override
    public IIcon getIcon(int side, int metadata)
    {
        if ((metadata & 1) == 1 && side == 0 || (metadata & 1) == 0 && side == 1)
        {
            return this.blockIcon;
        }
        else
        {
            return this.iconSide;
        }
    }

    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack par6ItemStack)
    {
        int var6 = determineOrientation(par1World, par2, par3, par4, par5EntityLiving);
        par1World.setBlockMetadataWithNotify(par2, par3, par4, var6, 2);
    }

    /**
     * gets the way this piston should face for that entity that placed it.
     */
    public static int determineOrientation(World par0World, int par1, int par2, int par3, EntityLivingBase par4EntityPlayer)
    {
        double var5 = par4EntityPlayer.posY + 1.82D - par4EntityPlayer.yOffset;

        if (var5 - par2 > 0.0D)
        {
            return DIR_UP;
        }
        else
        {
            return DIR_DOWN;
        }
    }



    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityLiftLauncher();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        super.registerBlockIcons(par1IconRegister);
        this.iconSide = par1IconRegister.registerIcon("spelunker:liftLauncher_side");
    }

}
