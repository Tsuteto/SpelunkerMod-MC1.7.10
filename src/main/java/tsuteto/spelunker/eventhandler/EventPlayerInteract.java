package tsuteto.spelunker.eventhandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.blockaspect.BlockAspectHC;
import tsuteto.spelunker.damage.SpelunkerDamageSource;
import tsuteto.spelunker.player.ISpelunkerPlayer;
import tsuteto.spelunker.player.SpelunkerHardcoreMP;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class EventPlayerInteract
{
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        ISpelunkerPlayer spelunker = SpelunkerMod.getSpelunkerPlayer(event.entityPlayer);
        if (spelunker != null && spelunker.isHardcore())
        {
            EntityPlayer player = event.entityPlayer;
            ItemStack equipped = player.getCurrentEquippedItem();
            if (!player.worldObj.isRemote && ((SpelunkerPlayerMP)spelunker).isWithinSpawnTimeInv())
            {
                event.setCanceled(true);
            }

            if (event.action == Action.RIGHT_CLICK_BLOCK)
            {
                Block block = player.worldObj.getBlock(event.x, event.y, event.z);

                // Doors
                if (block == Blocks.wooden_door)
                {
                    BlockDoor door = (BlockDoor)block;
                    if (door.func_150015_f(player.worldObj, event.x, event.y, event.z)) // isDoorOpen
                    {
                        player.attackEntityFrom(SpelunkerDamageSource.door, 1.0F);
                    }
                }

                // Trapdoors
                if (block == Blocks.trapdoor)
                {
                    int meta = player.worldObj.getBlockMetadata(event.x, event.y, event.z);
                    if (BlockTrapDoor.func_150118_d(meta)) // isTrapdoorOpen
                    {
                        player.attackEntityFrom(SpelunkerDamageSource.door, 1.0F);
                    }
                }

                // Fence gates
                if (block == Blocks.fence_gate)
                {
                    int meta = player.worldObj.getBlockMetadata(event.x, event.y, event.z);
                    if (BlockFenceGate.isFenceGateOpen(meta))
                    {
                        player.attackEntityFrom(SpelunkerDamageSource.door, 1.0F);
                    }
                }
            }

            // BlockAspect
            Block block = player.worldObj.getBlock(event.x, event.y, event.z);
            if (block != null)
            {
                if (event.action == Action.LEFT_CLICK_BLOCK)
                {
                    BlockAspectHC.applyDamage(player, block, BlockAspectHC.Aspect.UnclickableLeft);
                }
                if (event.action == Action.RIGHT_CLICK_BLOCK)
                {
                    BlockAspectHC.applyDamage(player, block, BlockAspectHC.Aspect.UnclickableRight);
                }
            }

            // Fire
            if (event.action == Action.LEFT_CLICK_BLOCK
                    && isTouchingFire(event.entityPlayer.worldObj, event.x, event.y, event.z, event.face))
            {
                player.attackEntityFrom(DamageSource.onFire, 2.0F);
            }

            if (!player.worldObj.isRemote)
            {
                ((SpelunkerHardcoreMP)((SpelunkerPlayerMP)spelunker).getSpelunkerExtra()).shouldCheckGS = true;
            }
        }
    }

    @SubscribeEvent
    public void onPlayerEntityInteract(EntityInteractEvent event)
    {
        ISpelunkerPlayer spelunker = SpelunkerMod.getSpelunkerPlayer(event.entityPlayer);
        if (spelunker != null && spelunker.isHardcore())
        {
            EntityPlayer player = event.entityPlayer;
            if (!player.worldObj.isRemote)
            {
                ((SpelunkerHardcoreMP)((SpelunkerPlayerMP)spelunker).getSpelunkerExtra()).shouldCheckGS = true;
            }
        }
    }

    private boolean isTouchingFire(World par1, int par2, int par3, int par4, int par5)
    {
        if (par5 == 0)
        {
            --par3;
        }

        if (par5 == 1)
        {
            ++par3;
        }

        if (par5 == 2)
        {
            --par4;
        }

        if (par5 == 3)
        {
            ++par4;
        }

        if (par5 == 4)
        {
            --par2;
        }

        if (par5 == 5)
        {
            ++par2;
        }

        return par1.getBlock(par2, par3, par4) == Blocks.fire;
    }

    protected MovingObjectPosition getMovingObjectPositionFromPlayer(World par1World, EntityPlayer par2EntityPlayer, boolean par3)
    {
        float var4 = 1.0F;
        float var5 = par2EntityPlayer.prevRotationPitch + (par2EntityPlayer.rotationPitch - par2EntityPlayer.prevRotationPitch) * var4;
        float var6 = par2EntityPlayer.prevRotationYaw + (par2EntityPlayer.rotationYaw - par2EntityPlayer.prevRotationYaw) * var4;
        double var7 = par2EntityPlayer.prevPosX + (par2EntityPlayer.posX - par2EntityPlayer.prevPosX) * var4;
        double var9 = par2EntityPlayer.prevPosY + (par2EntityPlayer.posY - par2EntityPlayer.prevPosY) * var4 + 1.62D - par2EntityPlayer.yOffset;
        double var11 = par2EntityPlayer.prevPosZ + (par2EntityPlayer.posZ - par2EntityPlayer.prevPosZ) * var4;
        Vec3 var13 = Vec3.createVectorHelper(var7, var9, var11);
        float var14 = MathHelper.cos(-var6 * 0.017453292F - (float)Math.PI);
        float var15 = MathHelper.sin(-var6 * 0.017453292F - (float)Math.PI);
        float var16 = -MathHelper.cos(-var5 * 0.017453292F);
        float var17 = MathHelper.sin(-var5 * 0.017453292F);
        float var18 = var15 * var16;
        float var20 = var14 * var16;
        double var21 = 5.0D;
        if (par2EntityPlayer instanceof EntityPlayerMP)
        {
            var21 = ((EntityPlayerMP)par2EntityPlayer).theItemInWorldManager.getBlockReachDistance();
        }
        Vec3 var23 = var13.addVector(var18 * var21, var17 * var21, var20 * var21);
        return par1World.func_147447_a(var13, var23, par3, !par3, false);
    }


}
