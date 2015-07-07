package tsuteto.spelunker.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.constants.SpelunkerGameMode;
import tsuteto.spelunker.constants.SpelunkerPacketType;
import tsuteto.spelunker.gui.TitleController;
import tsuteto.spelunker.player.SpelunkerHardcoreSP;
import tsuteto.spelunker.player.SpelunkerNormalSP;
import tsuteto.spelunker.player.SpelunkerPlayerSP;
import tsuteto.spelunker.potion.SpelunkerPotion;
import tsuteto.spelunker.sound.ModSound;
import tsuteto.spelunker.sound.SpelunkerBgm;
import tsuteto.spelunker.util.PlayerUtils;
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
                spelunker.timeSpawnInv = data.readInt();
                spelunker.speLevelStartTime = data.readLong();
                spelunker.speLevelFinishTime = data.readLong();
                spelunker.isSpeLevelCleared = data.readBoolean();
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
                TitleController.instance().clear();
                break;

            case INIT_FAILED:
                spelunker.isInitializing = false;
                break;

            case DIM_CHANGE:
                spelunker.speLevelStartTime = data.readLong();
                spelunker.isSpeLevelCleared = false;
                TitleController.instance().clear();
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

            case SPAWN_INV:
                spelunker.timeSpawnInv = data.readInt();
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

            case SPE_CHECKPOINT:
                {
                    int mainBonus = data.readInt();
                    int energyBonus = data.readInt();
                    int time = data.readInt();
                    if (mainBonus > 0)
                    {
                        TitleController.instance().setTitle(
                                I18n.format("Spelunker.checkpoint.title"),
                                I18n.format("Spelunker.bonus.main", mainBonus),
                                I18n.format("Spelunker.bonus.energy", energyBonus),
                                I18n.format("Spelunker.bonus.split", Utils.formatTickToTime(time, true))
                        );
                    }
                    else
                    {
                        TitleController.instance().setTitle(
                                I18n.format("Spelunker.checkpoint.title"),
                                I18n.format("Spelunker.bonus.split", Utils.formatTickToTime(time, true))
                        );
                    }
                }

                if (SpelunkerMod.isBgmCheckPointAvailable)
                {
                    ModSound.interruptBgm(SpelunkerBgm.getCheckPoint());
                }
                break;

            case SPE_CLEARED:
                {
                    int mainBonus = data.readInt();
                    int energyBonus = data.readInt();
                    int time = data.readInt();
                    if (mainBonus > 0)
                    {
                        TitleController.instance().setTitle(
                                I18n.format("Spelunker.speCleared.title"),
                                I18n.format("Spelunker.bonus.main", mainBonus),
                                I18n.format("Spelunker.bonus.energy", energyBonus),
                                I18n.format("Spelunker.bonus.time", Utils.formatTickToTime(time, true))
                        );
                    }
                    else
                    {
                        TitleController.instance().setTitle(
                                I18n.format("Spelunker.speCleared.title"),
                                I18n.format("Spelunker.bonus.time", Utils.formatTickToTime(time, true))
                        );
                    }
                }

                spelunker.speLevelFinishTime = entityPlayer.worldObj.getTotalWorldTime();
                spelunker.isSpeLevelCleared = true;

                if (SpelunkerMod.isBgmAllCleardAvailable)
                {
                    ModSound.interruptBgm(SpelunkerBgm.getAllCleared());
                }
                break;

            case ALL_CLEARED:
                int mainBonus = data.readInt();
                if (mainBonus > 0)
                {
                    TitleController.instance().setTitle(
                            I18n.format("Spelunker.allCleared.title"),
                            I18n.format("Spelunker.bonus.main", mainBonus)
                    );
                }
                else
                {
                    TitleController.instance().setTitle(
                            I18n.format("Spelunker.allCleared.title"));
                }

                if (SpelunkerMod.isBgmAllCleardAvailable)
                {
                    ModSound.interruptBgm(SpelunkerBgm.getAllCleared());
                }
                break;

            case HOP:
                spelunker.hop();
                break;

            case RESET_GS:
                int numGs = data.readInt();
                PlayerUtils.giveGoldenSpelunker(entityPlayer, numGs);
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
