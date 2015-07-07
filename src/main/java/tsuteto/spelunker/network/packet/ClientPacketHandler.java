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
                spelunker.isSpeLevelCheated = data.readBoolean();
                spelunker.speLevelBestTime = data.readInt();
                spelunker.isReady = true;
                spelunker.isInitializing = false;
                spelunker.setSpelunkerExtra();
                TitleController.instance().clear();
                break;

            case INIT_FAILED:
                spelunker.isInitializing = false;
                 break;

            case DIM_CHANGE:
                spelunker.speLevelBestTime = data.readInt();
                spelunker.isBestTime = false;
                spelunker.isSpeLevelCleared = false;
                spelunker.isSpeLevelCheated = false;
                TitleController.instance().clear();
                break;

            case TOGGLE_HC:
                spelunker.hardcore = data.readBoolean();
                spelunker.setSpelunkerExtra();
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

            case GHOST_TARGETING:
                spelunker.ghosts.add(entityPlayer.worldObj.getEntityByID(data.readInt()));
                break;

            case SET_HARDCORE:
                spelunker.setDifficultyHardcore();
                break;

            case SPE_START:
                spelunker.speLevelStartTime = data.readLong();
                break;

            case SPE_CHECKPOINT:
                {
                    int mainBonus = data.readInt();
                    int energyBonus = data.readInt();
                    int split = data.readInt();
                    String dispSplit = spelunker.isSpeLevelCheated ?
                            I18n.format("Spelunker.cheated") : Utils.formatTickToTime(split, true);
                    if (mainBonus > 0)
                    {
                        TitleController.instance().setTitle(
                                I18n.format("Spelunker.checkpoint.title"),
                                I18n.format("Spelunker.bonus.main", mainBonus),
                                I18n.format("Spelunker.bonus.energy", energyBonus),
                                I18n.format("Spelunker.bonus.split", dispSplit)
                        );
                    }
                    else
                    {
                        TitleController.instance().setTitle(
                                I18n.format("Spelunker.checkpoint.title"),
                                I18n.format("Spelunker.bonus.split", dispSplit)
                        );
                    }
                }

                if (SpelunkerBgm.isBgmCheckPointAvailable)
                {
                    ModSound.interruptBgm(SpelunkerBgm.getCheckPoint());
                }
                break;

            case SPE_CLEARED:
                {
                    int mainBonus = data.readInt();
                    int energyBonus = data.readInt();
                    int splitTime = data.readInt();
                    long finishTime = data.readLong();
                    int totalTime = data.readInt();
                    boolean isBestTime = data.readBoolean();

                    String dispSplit = spelunker.isSpeLevelCheated ?
                            I18n.format("Spelunker.cheated") : Utils.formatTickToTime(splitTime, true);
                    String dispTotal = spelunker.isSpeLevelCheated ?
                            I18n.format("Spelunker.cheated") : Utils.formatTickToTime(totalTime, true);
                    if (mainBonus > 0)
                    {
                        TitleController.instance().setTitle(
                                I18n.format("Spelunker.speCleared.title"),
                                I18n.format("Spelunker.bonus.main", mainBonus),
                                I18n.format("Spelunker.bonus.energy", energyBonus),
                                I18n.format("Spelunker.bonus.split", dispSplit),
                                I18n.format("Spelunker.bonus.time", dispTotal)
                        );
                    }
                    else
                    {
                        TitleController.instance().setTitle(
                                I18n.format("Spelunker.speCleared.title"),
                                I18n.format("Spelunker.bonus.split", dispSplit),
                                I18n.format("Spelunker.bonus.time", dispTotal)
                        );
                    }

                    spelunker.speLevelFinishTime = finishTime;
                    spelunker.isSpeLevelCleared = true;
                    spelunker.isBestTime = isBestTime;
                    if (isBestTime)
                    {
                        spelunker.speLevelBestTime = totalTime;
                    }
                }

                if (SpelunkerBgm.isBgmAllCleardAvailable)
                {
                    ModSound.interruptBgm(SpelunkerBgm.getAllCleared());
                }
                break;

            case SPE_CHEATED:
                spelunker.isSpeLevelCheated = true;
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

                if (SpelunkerBgm.isBgmAllCleardAvailable)
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
