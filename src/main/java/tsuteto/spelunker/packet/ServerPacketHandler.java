package tsuteto.spelunker.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.blockaspect.BlockAspectHC;
import tsuteto.spelunker.constants.SpelunkerGameMode;
import tsuteto.spelunker.constants.SpelunkerPacketType;
import tsuteto.spelunker.damage.SpelunkerDamageSource;
import tsuteto.spelunker.player.SpelunkerPlayerMP;
import tsuteto.spelunker.potion.SpelunkerPotion;

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
        SpelunkerPlayerMP spelunkerMp = SpelunkerMod.getSpelunkerPlayer((EntityPlayerMP)player);
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

                if (!MinecraftServer.getServer().isSinglePlayer() && SpelunkerMod.settings().gameMode == SpelunkerGameMode.Arcade)
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
                response.sendPacketPlayer(player);

                if (spelunkerMp.isUsingEnergy()) {
                    response = new SpelunkerPacketDispatcher(SpelunkerPacketType.IN_CAVE_TRUE);
                }
                else
                {
                    response = new SpelunkerPacketDispatcher(SpelunkerPacketType.IN_CAVE_FALSE);
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
                new SpelunkerPacketDispatcher(SpelunkerPacketType.INIT_FAILED).sendPacketPlayer(player);
            }

            break;

        case ENERGY_DEC:
            spelunkerMp.decreaseEnergy(data.readInt());
            break;

        case DMG_FIREWORKS:
            ((EntityPlayerMP)player).attackEntityFrom(DamageSource.onFire, 1.0F);
            break;

        case DMG_HARDBLOCK:
            ((EntityPlayerMP)player).attackEntityFrom(SpelunkerDamageSource.hardBlock, 1.0F);
            break;

        case DMG_BLOCKHITTING:
            int blockId = data.readInt();
            Block block = Block.getBlockById(blockId);
            if (block != null)
            {
                BlockAspectHC.applyDamage((EntityPlayerMP)player, block, BlockAspectHC.Aspect.Undiggable);
            }
            break;

        case CHOKED:
            ((EntityPlayerMP)player).addPotionEffect(new PotionEffect(SpelunkerPotion.choked.id, 600, 0));
            break;

        default:
            break;
        }
    }

}
