package tsuteto.spelunker.player;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import tsuteto.spelunker.blockaspect.BlockAspectHC;
import tsuteto.spelunker.constants.SpelunkerPacketType;
import tsuteto.spelunker.network.SpelunkerPacketDispatcher;

import java.util.Random;

public class SpelunkerHardcoreSP extends SpelunkerNormalSP
{
    private int diggingHandDamageCount = 0;
    private int prevFoodlevel;
    private boolean prevIsEating;
    protected Random rand = new Random();

    public SpelunkerHardcoreSP(SpelunkerPlayerSP spelunker)
    {
        super(spelunker);
    }

    @Override
    public void beforeOnLivingUpdate()
    {
    }

    @Override
    public void afterOnLivingUpdate()
    {
        // Choked by food
        int foodlevel = player.getFoodStats().getFoodLevel();

        if (foodlevel > prevFoodlevel && prevIsEating && this.rand.nextInt(5) < 10)
        {
            new SpelunkerPacketDispatcher(SpelunkerPacketType.CHOKED).sendPacketToServer();
        }
        prevFoodlevel = foodlevel;
        prevIsEating = player.isEating();

    }

    @Override
    public boolean onParticleCollision(Entity entity)
    {
        return false;
    }

    @Override
    public void onPlayerDigging()
    {
        super.onPlayerDigging();

        Minecraft mc = Minecraft.getMinecraft();
        double d0 = mc.playerController.getBlockReachDistance();
        MovingObjectPosition mop = player.rayTrace(d0, 1.0f);
        if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            Block block = player.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ);
            //int meta = player.worldObj.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);
            //boolean isBreakable = block.canHarvestBlock(player, meta);
            ItemStack equipped = player.getCurrentEquippedItem();
            boolean isBreakable = equipped != null && equipped.func_150997_a(block) > 1.0; // getStrVsBlock
            float hardness = block.getBlockHardness(player.worldObj, mop.blockX, mop.blockY, mop.blockZ);

            if (BlockAspectHC.matches(player, block, BlockAspectHC.Aspect.Undiggable))
            {
                diggingHandDamageCount++;

                if (diggingHandDamageCount > 5)
                {
                    new SpelunkerPacketDispatcher(SpelunkerPacketType.DMG_BLOCKHITTING)
                            .addInt(Block.getIdFromBlock(block)).sendPacketToServer();
                    diggingHandDamageCount = 0;
                }
            }
            else if (!isBreakable && hardness > 1.0)
            {
                diggingHandDamageCount++;

                if (diggingHandDamageCount > 20)
                {
                    new SpelunkerPacketDispatcher(SpelunkerPacketType.DMG_HARDBLOCK).sendPacketToServer();
                    diggingHandDamageCount = 0;
                }
            }
        }
    }

    @Override
    public void onPlayerNotDigging()
    {
        diggingHandDamageCount = 0;
    }

}
