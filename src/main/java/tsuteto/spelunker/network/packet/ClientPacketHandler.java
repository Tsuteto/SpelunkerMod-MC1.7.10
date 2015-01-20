package tsuteto.spelunker.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.constants.SpelunkerGameMode;
import tsuteto.spelunker.constants.SpelunkerPacketType;
import tsuteto.spelunker.player.SpelunkerHardcoreSP;
import tsuteto.spelunker.player.SpelunkerNormalSP;
import tsuteto.spelunker.player.SpelunkerPlayerSP;
import tsuteto.spelunker.potion.SpelunkerPotion;
import tsuteto.spelunker.sound.ModSound;
import tsuteto.spelunker.sound.SpelunkerBgm;
import tsuteto.spelunker.util.Utils;

/**
 * Handles network packets on client side
 *
 * @author Tsuteto
 *
 */
public class ClientPacketHandler extends CommonClientPacketHandler
{
    public void onPacketData(ByteBuf data, EntityPlayer player)
    {
        //System.out.println("received: " + packet.packetType);
        EntityPlayerSP entityPlayer = (EntityPlayerSP)player;
        SpelunkerPlayerSP spelunker = SpelunkerMod.getSpelunkerPlayer(entityPlayer);
        if (spelunker == null) return;

        SpelunkerPacketType packetType = SpelunkerPacketType.values()[data.readByte()];

        switch (packetType) {
            case INIT_SUCCEEDED:
                spelunker.gameMode = SpelunkerGameMode.values()[data.readByte()];
                spelunker.hardcore = data.readBoolean();
                spelunker.deaths = data.readInt();
                spelunker.livesLeft = data.readInt();
                spelunker.spelunkerScore.initScore(data.readInt());
                spelunker.spelunkerScore.hiscore = data.readInt();
                spelunker.maxEnergy = data.readInt();
                spelunker.isReady = true;
                spelunker.isInitializing = false;
                if (spelunker.hardcore)
                {
                    spelunker.spelunkerExtra = new SpelunkerHardcoreSP(spelunker);
                }
                else
                {
                    spelunker.spelunkerExtra = new SpelunkerNormalSP(spelunker);
                }
                break;

            case INIT_FAILED:
                spelunker.isInitializing = false;
                break;

            case ENERGY:
                spelunker.energy = data.readInt();
                //System.out.println("setEnergy: " + spelunker.getEnergy());
                break;

            case LIVES:
                spelunker.livesLeft = data.readInt();
                break;

            case DEATHS:
                spelunker.deaths = data.readInt();
                break;

            case SCORE:
                spelunker.spelunkerScore.setScore(data.readInt());
                break;

            case ENERGY_UP:
                spelunker.maxEnergy = data.readInt();
                spelunker.timeLvlupIndicator = 80;
                break;

            case IN_CAVE_TRUE:
                spelunker.setUsingEnergy(true);
                break;

            case IN_CAVE_FALSE:
                spelunker.setUsingEnergy(false);
                break;

            case GOT_2x:
                spelunker.is2xScore = true;
                break;

            case OUT_OF_2x:
                spelunker.is2xScore = false;
                break;

            case GOT_INVINCIBLE:
                spelunker.isInvincible = true;
                break;

            case OUT_OF_INVINCIBLE:
                spelunker.isInvincible = false;
                break;

            case GOT_POTION:
                spelunker.isSpeedPotion = true;
                break;

            case OUT_OF_POTION:
                spelunker.isSpeedPotion = false;
                break;

            case GHOST_COMING:
                spelunker.isGhostComing = true;
                break;

            case GHOST_GONE:
                spelunker.isGhostComing = false;
                break;

            case GAMEOVER:
                spelunker.setDifficultyHardcore();
                break;

            case ALL_CLEARED:
                ModSound.playBgm(SpelunkerBgm.bgmAllCleared);
                break;

            case HOP:
                spelunker.hop();
                break;

            case RESET_GS:
                int numGs = data.readInt();
                Utils.giveGoldenSpelunker(entityPlayer, numGs);
                break;

            case CHECK_POTION_ID:
                int choked = data.readInt();
                int sunstroke = data.readInt();
                if (choked != SpelunkerPotion.choked.id || sunstroke != SpelunkerPotion.heatStroke.id)
                {
                    IChatComponent chat;
                    chat = new ChatComponentTranslation("Spelunker.wrongPotionId1");
                    entityPlayer.addChatMessage(chat);
                    chat = new ChatComponentTranslation("Spelunker.wrongPotionId2");
                    entityPlayer.addChatMessage(chat);
                    chat = new ChatComponentTranslation("Spelunker.wrongPotionId3", choked, sunstroke);
                    entityPlayer.addChatMessage(chat);
                }
                break;

            default:
                break;
        }
    }

}
