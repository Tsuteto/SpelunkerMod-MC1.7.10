package tsuteto.spelunker.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tsuteto.spelunker.block.tileentity.TileEntityRespawnPoint;
import tsuteto.spelunker.util.ModLog;
import tsuteto.spelunker.util.Utils;

public class BlockRespawnPoint extends BlockContainer
{
    public BlockRespawnPoint(Material p_i45394_1_)
    {
        super(p_i45394_1_);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        if (!world.isRemote)
        {
            if (entity instanceof EntityPlayer)
            {
                this.setPlayerRespawnPoint(world, x, y, z, (EntityPlayer) entity);
            }
        }
    }

    public void setPlayerRespawnPoint(World world, int x, int y, int z, EntityPlayer player)
    {
        Block block = world.getBlock(x, y, z);
        if (this.isRespawnPoint(block))
        {
            Utils.updatePlayerSpawnPoint(world, x, y, z, player);
        }
        else if (block == SpelunkerBlocks.blockRespawnGate)
        {
            TileEntityRespawnPoint gate = (TileEntityRespawnPoint) world.getTileEntity(x, y, z);
            if (gate.respawnPoint != null)
            {
                Utils.updatePlayerSpawnPoint(world, gate.respawnPoint.posX, gate.respawnPoint.posY, gate.respawnPoint.posZ, player);
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
    {
        if (!p_149689_1_.isRemote)
        {
            boolean isValid = initTileEntity(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_);
            if (p_149689_5_ instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer) p_149689_5_;
                if (isValid)
                {
                    player.addChatMessage(new ChatComponentText("Respawn gate located respawn point"));
                }
                else
                {
                    player.addChatMessage(new ChatComponentText("Respawn gate FAILED to locate respawn point"));
                }
            }
        }
    }

    public boolean initTileEntity(World world, int x, int y, int z)
    {
        TileEntityRespawnPoint thisGate = (TileEntityRespawnPoint) world.getTileEntity(x, y, z);

        if (this == SpelunkerBlocks.blockRespawnGate)
        {
            thisGate.isPointBlock = false;

            for (ForgeDirection next : ForgeDirection.VALID_DIRECTIONS)
            {
                int i = x + next.offsetX;
                int j = y + next.offsetY;
                int k = z + next.offsetZ;
                Block anotherBlock = world.getBlock(i, j, k);
                if (anotherBlock == SpelunkerBlocks.blockRespawnGate)
                {
                    TileEntity anotherGate = world.getTileEntity(i, j, k);
                    if (anotherGate instanceof TileEntityRespawnPoint)
                    {
                        if (((TileEntityRespawnPoint) anotherGate).respawnPoint != null)
                        {
                            thisGate.respawnPoint = new ChunkCoordinates(((TileEntityRespawnPoint) anotherGate).respawnPoint);
                            ModLog.debug("Respawn point located at %s", thisGate.respawnPoint);
                            return true;
                        }
                    }
                }
                else if (this.isRespawnPoint(anotherBlock))
                {
                    thisGate.respawnPoint = new ChunkCoordinates(i, j, k);
                    ModLog.debug("Respawn point located at %s", thisGate.respawnPoint);
                    return true;
                }
            }
            ModLog.debug("Failed to locate respawn point");
            return false;
        }
        else if (this.isRespawnPoint(this))
        {
            thisGate.isPointBlock = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isRespawnPoint(Block block)
    {
        return block == SpelunkerBlocks.blockItemBox || block == SpelunkerBlocks.blockStartPoint;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityRespawnPoint();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World p_149633_1_, int p_149633_2_, int p_149633_3_, int p_149633_4_)
    {
        Block block = p_149633_1_.getBlock(p_149633_2_, p_149633_3_, p_149633_4_);
        if (block != SpelunkerBlocks.blockItemBox)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        }
        return super.getSelectedBoundingBoxFromPool(p_149633_1_, p_149633_2_, p_149633_3_, p_149633_4_);
    }
}
