package tsuteto.spelunker.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.blockaspect.BlockAspectHC;
import tsuteto.spelunker.constants.SpelunkerGameMode;
import tsuteto.spelunker.constants.SpelunkerPacketType;
import tsuteto.spelunker.damage.SpelunkerDamageSource;
import tsuteto.spelunker.data.SpeLevelPlayerInfo;
import tsuteto.spelunker.data.SpeLevelRecordInfo;
import tsuteto.spelunker.network.SpelunkerPacketDispatcher;
import tsuteto.spelunker.player.SpelunkerPlayerMP;
import tsuteto.spelunker.potion.SpelunkerPotion;
import tsuteto.spelunker.util.ModLog;

/**
 * Handles network packets on server side
 *
 * @author Tsuteto
 *
 */
public class ServerPacketHandler
{

    public void onPacketData(ByteBuf data, EntityPlayer player)
    {
        EntityPlayerMP playermp = (EntityPlayerMP)player;
        SpelunkerPlayerMP spelunkerMp = SpelunkerMod.getSpelunkerPlayer(playermp);
        if (spelunkerMp == null) return;

        SpelunkerPacketType packetType = SpelunkerPacketType.values()[data.readByte()];

        switch (packetType)
        {
        case INIT:
            try
            {
                SpelunkerPacketDispatcher response = new SpelunkerPacketDispatcher(SpelunkerPacketType.INIT_SUCCEEDED);
                response.addByte((byte)spelunkerMp.gameMode.ordinal());
                response.addBool(SpelunkerMod.isHardcore());
                response.addInt(spelunkerMp.deaths);

                if (!SpelunkerMod.isSinglePlayer() && SpelunkerMod.settings().gameMode == SpelunkerGameMode.Arcade)
                {
                    response.addInt(SpelunkerMod.getTotalLivesLeft());
                }
                else
                {
                    response.addInt(spelunkerMp.livesLeft);
                }

                response.addInt(spelunkerMp.spelunkerScore.scoreActual);
                response.addInt(spelunkerMp.spelunkerScore.hiscore);
                response.addInt(spelunkerMp.getMaxEnergy());
                response.addInt(spelunkerMp.getSpawnTimeInv());
                if (spelunkerMp.isInSpelunkerWorld() && spelunkerMp.getWorldInfo().hasSpeLevelInfo())
                {
                    SpeLevelPlayerInfo levelInfo = spelunkerMp.getWorldInfo().getSpeLevelInfo();
                    response.addLong(levelInfo.getStartTime());
                    response.addLong(levelInfo.getFinishTime());
                    response.addBool(levelInfo.isCleared());
                    response.addBool(levelInfo.isCheated());
                    SpeLevelRecordInfo.Record record = spelunkerMp.getSpeLevelRecord();
                    response.addInt(record != null ? record.time : -1);
                }
                else
                {
                    response.addLong(-1);
                    response.addLong(-1);
                    response.addBool(false);
                    response.addBool(false);
                    response.addInt(-1);
                }
                response.sendPacketPlayer(player);

                if (spelunkerMp.isUsingEnergy())
                {
                    response = new SpelunkerPacketDispatcher(SpelunkerPacketType.USING_ENERGY);
                }
                else
                {
                    response = new SpelunkerPacketDispatcher(SpelunkerPacketType.NOT_USING_ENERGY);
                }
                response.sendPacketPlayer(player);

                if (spelunkerMp.isInCave())
                {
                    response = new SpelunkerPacketDispatcher(SpelunkerPacketType.GOT_IN_CAVE);
                }
                else
                {
                    response = new SpelunkerPacketDispatcher(SpelunkerPacketType.OUT_OF_CAVE);
                }
                response.sendPacketPlayer(player);

                if (spelunkerMp.is2xScore())
                {
                    response = new SpelunkerPacketDispatcher(SpelunkerPacketType.GOT_2x);
                    response.sendPacketPlayer(player);
                }

                if (spelunkerMp.isInvincible())
                {
                    response = new SpelunkerPacketDispatcher(SpelunkerPacketType.GOT_INVINCIBLE);
                    response.sendPacketPlayer(player);
                }

                if (spelunkerMp.isSpeedPotion())
                {
                    response = new SpelunkerPacketDispatcher(SpelunkerPacketType.GOT_POTION);
                    response.sendPacketPlayer(player);
                }

                if (SpelunkerMod.isHardcore())
                {
                    new SpelunkerPacketDispatcher(SpelunkerPacketType.ENERGY)
                            .addInt(spelunkerMp.getEnergy())
                            .sendPacketPlayer(player);
                }
            }
            catch (NullPointerException e)
            {
                ModLog.warn(e, "Failed to response for INIT");
                new SpelunkerPacketDispatcher(SpelunkerPacketType.INIT_FAILED).sendPacketPlayer(player);
            }

            break;

        case ENERGY_DEC:
            spelunkerMp.decreaseEnergy(data.readInt());
            break;

        case DMG_FIREWORKS:
            player.attackEntityFrom(DamageSource.onFire, 1.0F);
            break;

        case DMG_HARDBLOCK:
            player.attackEntityFrom(SpelunkerDamageSource.hardBlock, 1.0F);
            break;

        case DMG_BLOCKHITTING:
            int blockId = data.readInt();
            Block block = Block.getBlockById(blockId);
            if (block != null)
            {
                BlockAspectHC.applyDamage(player, block, BlockAspectHC.Aspect.Undiggable);
            }
            break;

        case CHOKED:
            player.addPotionEffect(new PotionEffect(SpelunkerPotion.choked.id, 600, 0));
            break;

        case ROPE_JUMP:
            spelunkerMp.isRopeJumping = data.readBoolean();
            break;

        case GAMEOVER:
            spelunkerMp.banByGameover();
            break;

            default:
            break;
        }
    }

}
