package tsuteto.spelunker.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.achievement.AchievementMgr;
import tsuteto.spelunker.block.tileentity.TileEntityCheckpoint;
import tsuteto.spelunker.constants.SpelunkerPacketType;
import tsuteto.spelunker.data.SpeLevelPlayerInfo;
import tsuteto.spelunker.data.SpeLevelRecordInfo;
import tsuteto.spelunker.init.SpeAchievementList;
import tsuteto.spelunker.network.SpelunkerPacketDispatcher;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class BlockCheckpoint extends BlockRespawnPoint
{
    public BlockCheckpoint(Material p_i45394_1_)
    {
        super(p_i45394_1_);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        super.onEntityCollidedWithBlock(world, x, y, z, entity);

        if (entity instanceof EntityPlayer)
        {
            if (!world.isRemote)
            {
                EntityPlayer player = (EntityPlayer) entity;
                SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(player);
                if (spelunker.getWorldInfo().hasSpeLevelInfo())
                {
                    SpeLevelPlayerInfo playerLevelInfo = spelunker.getWorldInfo().getSpeLevelInfo();
                    if (isLevelGoal(world, x, y, z))
                    {
                        if (!player.capabilities.isCreativeMode && !playerLevelInfo.isCleared())
                        {
                            onGoalPassed(world, player, playerLevelInfo);
                        }
                    }
                    else
                    {
                        int cpNo = getCheckpointNo(world, x, y, z);
                        if (!player.capabilities.isCreativeMode && cpNo != -1 && !playerLevelInfo.isCheckPointPassed(cpNo))
                        {
                            this.onCheckpointPassed(world, player, playerLevelInfo, cpNo);
                        }
                    }

                    spelunker.setEnergy(spelunker.getMaxEnergy());
                    player.getFoodStats().func_151686_a((ItemFood) Items.cooked_beef, new ItemStack(Items.cooked_beef));
                }
            }
        }
    }

    private void onCheckpointPassed(World world, EntityPlayer player, SpeLevelPlayerInfo playerLevelInfo, int cpNo)
    {
        SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(player);

        int energyBonus = SpelunkerMod.bonusEnergyMax * spelunker.getEnergy() / spelunker.getMaxEnergy() / 10 * 10;
        spelunker.addSpelunkerScore(SpelunkerMod.bonusSpeCheckPoint + energyBonus, true);
        spelunker.increaseLife(1);
        playerLevelInfo.setCheckPointPassed(cpNo);
        int splitTime = playerLevelInfo.markSplitTime(player);
        spelunker.saveSpelunker();
        spelunker.eliminateGhosts();

        SpelunkerPacketDispatcher.of(SpelunkerPacketType.SPE_CHECKPOINT)
                .addInt(spelunker.calcActualScore(SpelunkerMod.bonusSpeCheckPoint, true))
                .addInt(spelunker.calcActualScore(energyBonus, true))
                .addInt(splitTime)
                .sendPacketPlayer(player);
    }

    private void onGoalPassed(World world, EntityPlayer player, SpeLevelPlayerInfo playerLevelInfo)
    {
        SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(player);

        long finishTime = world.getTotalWorldTime();
        int finishBonus = SpelunkerMod.bonusSpeLvlClearedBase + SpelunkerMod.bonusSpeLvlClearedChkPt * playerLevelInfo.getCheckPointCount();
        int energyBonus = SpelunkerMod.bonusEnergyMax * spelunker.getEnergy() / spelunker.getMaxEnergy() / 10 * 10;
        spelunker.addSpelunkerScore(finishBonus + energyBonus, true);
        spelunker.increaseLife(1);
        playerLevelInfo.setCleared(true);
        playerLevelInfo.setFinishTime(finishTime);
        int splitTime = playerLevelInfo.markSplitTime(player);

        boolean isBestTime = false;
        int totalTime = playerLevelInfo.getTotalTime(player);
        SpeLevelRecordInfo.Record record = spelunker.getSpeLevelRecord();
        if (SpelunkerMod.isSinglePlayer()
                && !playerLevelInfo.isCheated()
                && (record == null || totalTime < record.time))
        {
            spelunker.registerSpeLevelRecord(totalTime);
            isBestTime = true;
        }

        spelunker.saveSpelunker();
        spelunker.eliminateGhosts();

        AchievementMgr.achieve(player, SpeAchievementList.Key.speWorldCleared);

        SpelunkerPacketDispatcher.of(SpelunkerPacketType.SPE_CLEARED)
                .addInt(spelunker.calcActualScore(finishBonus, true))
                .addInt(spelunker.calcActualScore(energyBonus, true))
                .addInt(splitTime)
                .addLong(finishTime)
                .addInt(totalTime)
                .addBool(isBestTime)
                .sendPacketPlayer(player);
    }

    public static boolean isLevelGoal(World world, int x, int y, int z)
    {
        return world.getBlockMetadata(x, y, z) == 1;
    }

    public static int getCheckpointNo(World world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileEntityCheckpoint)
        {
            return ((TileEntityCheckpoint)te).no;
        }
        else
        {
            return -1;
        }
    }

    public static void setCheckpointNo(World world, int x, int y, int z, int no)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileEntityCheckpoint)
        {
            ((TileEntityCheckpoint)te).setCheckPointNo(no);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityCheckpoint();
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
    public int getRenderType()
    {
        return -1;
    }
}
