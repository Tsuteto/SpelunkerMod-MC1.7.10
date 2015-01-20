package tsuteto.spelunker.block;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.player.SpelunkerPlayerSP;

import java.util.List;

public class BlockRope extends Block
{
    public static int renderIdRope = RenderingRegistry.getNextAvailableRenderId();
    private IIcon iconTip;

    protected BlockRope()
    {
        super(Material.circuits);
        this.setStepSound(soundTypeCloth);
        float half = 0.0625F * 2;
        this.setBlockBounds(0.5F - half, 0.0F, 0.5F - half, 0.5F + half, 1.0F, 0.5F + half);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return p_149691_1_ == 1 ? this.iconTip : (p_149691_1_ == 0 ? this.iconTip : this.blockIcon);
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType()
    {
        return renderIdRope;
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_, Entity p_149670_5_)
    {
        List list = p_149670_1_.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBox(
                (double)p_149670_2_ + this.minX, (double)p_149670_3_ + this.minY, (double)p_149670_4_ + this.minZ,
                (double)p_149670_2_ + this.maxX, (double)p_149670_3_ + this.maxY, (double)p_149670_4_ + this.maxZ));

        if (!list.isEmpty())
        {
            for (Object entry : list)
            {
                Entity entity = (Entity) entry;
                if (!entity.onGround)
                {
                    this.handleMotion(entity);
                }
            }
        }
    }

    private void handleMotion(Entity entity)
    {
        if (entity instanceof EntityPlayer)
        {
            if (entity.worldObj.isRemote && entity instanceof EntityPlayerSP)
            {
                this.onClientControl((EntityPlayerSP)entity);
            }
        }
        else if (entity instanceof EntityLivingBase)
        {
            entity.motionY = 0.0D;
        }
        entity.fallDistance = 0.0F;
    }

    @SideOnly(Side.CLIENT)
    private void onClientControl(EntityPlayerSP player)
    {
        SpelunkerPlayerSP spelunker = SpelunkerMod.getSpelunkerPlayer(player);
        spelunker.isGrabbingRope = true;
    }


    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    /**
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack par6ItemStack)
    {
        int var6 = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
        par1World.setBlockMetadataWithNotify(par2, par3, par4, var6, 2);
    }

    @SideOnly(Side.CLIENT)

    /**
     * ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6)
    {
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        super.registerBlockIcons(par1IconRegister);
        this.iconTip = par1IconRegister.registerIcon(SpelunkerMod.resourceDomain + "rope_tip");
    }

}
