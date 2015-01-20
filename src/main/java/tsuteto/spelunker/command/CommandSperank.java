package tsuteto.spelunker.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.ChatComponentText;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.data.SpelunkerWorldInfo;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Command /sperank
 *
 * @author Tsuteto
 *
 */
public class CommandSperank extends CommandSpelunkerBase
{
    private static final String TYPE_HISC = "hisc";
    private static final String TYPE_SC = "sc";
    private static final String TYPE_DEATH = "death";

    @Override
    public String getCommandName()
    {
        return "sperank";
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        if (SpelunkerMod.settings().playerStatusInPublic)
        {
            return true;
        }
        else
        {
            return super.canCommandSenderUseCommand(par1ICommandSender);
        }
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "/sperank sc|hisc|death";
    }

    @Override
    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length == 0)
        {
            throw new WrongUsageException(getCommandUsage(par1ICommandSender));
        }

        String[] playerList;
        if (MinecraftServer.getServer().isSinglePlayer())
        {
            playerList = new String[]{par1ICommandSender.getCommandSenderName()};
        }
        else
        {
            String[] datList = SpelunkerMod.getSaveHandler().getAvailableSpelunkerDat();
            playerList = new String[datList.length];
            for (int i = 0; i < datList.length; i++)
            {
                SpelunkerWorldInfo worldInfo = getWorldInfo(datList[i]);
                if (worldInfo.getPlayerName() != null)
                {
                    playerList[i] = worldInfo.getPlayerName();
                }
                else
                {
                    playerList[i] = datList[i];
                }
            }
        }

        RankingRow[] rankingRows = new RankingRow[playerList.length];

        for (int i = 0; i < playerList.length; i++)
        {
            RankingRow rankingData = new RankingRow();
            rankingData.username = playerList[i];

            EntityPlayer player = getOnlinePlayer(playerList[i]);
            if (player != null)
            {
                SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(player);
                if (spelunker == null) continue;
                rankingData.deaths = spelunker.getDeaths();
                rankingData.sc = spelunker.getSpelunkerScore().scoreActual;
                rankingData.hisc = spelunker.getSpelunkerScore().hiscore;
            }
            else
            {
                SpelunkerWorldInfo worldInfo = getWorldInfo(playerList[i]);
                if (worldInfo == null) continue;
                rankingData.deaths = worldInfo.getDeaths();
                rankingData.sc = worldInfo.getScore();
                rankingData.hisc = worldInfo.getHiscore();
            }
            rankingRows[i] = rankingData;
        }


        String type = par2ArrayOfStr[0];

        if (type.equals(TYPE_HISC))
        {
            par1ICommandSender.addChatMessage(new ChatComponentText("=== <Spelunker §6High Score§r Ranking> ==="));
            Arrays.sort(rankingRows, new Comparator<RankingRow>() {
                @Override
                public int compare(RankingRow a, RankingRow b)
                {
                    Integer val1 = a.hisc;
                    Integer val2 = b.hisc;
                    return val2.compareTo(val1);
                }
            });
        }
        else if (type.equals(TYPE_SC))
        {
            par1ICommandSender.addChatMessage(new ChatComponentText("=== <Spelunker §6Current Score§r Ranking> ==="));
            Arrays.sort(rankingRows, new Comparator<RankingRow>() {
                @Override
                public int compare(RankingRow a, RankingRow b)
                {
                    Integer val1 = a.sc;
                    Integer val2 = b.sc;
                    return val2.compareTo(val1);
                }
            });
        }
        else if (type.equals(TYPE_DEATH))
        {
            par1ICommandSender.addChatMessage(new ChatComponentText("=== <Spelunker §6Number of Deaths§r Ranking> ==="));
            Arrays.sort(rankingRows, new Comparator<RankingRow>() {
                @Override
                public int compare(RankingRow a, RankingRow b)
                {
                    Integer val1 = a.deaths;
                    Integer val2 = b.deaths;
                    return val2.compareTo(val1);
                }
            });
        }
        else
        {
            throw new WrongUsageException(getCommandUsage(par1ICommandSender));
        }

        if (rankingRows.length > 0)
        {
            int rank = 1;
            int prev = -1;
            for (int i = 0; i < rankingRows.length; i++)
            {
                int val = 0;

                if (type.equals(TYPE_HISC))
                {
                    val = rankingRows[i].hisc;
                }
                else if (type.equals(TYPE_SC))
                {
                    val = rankingRows[i].sc;
                }
                else if (type.equals(TYPE_DEATH))
                {
                    val = rankingRows[i].deaths;
                }

                if (val != prev)
                {
                    rank = i + 1;
                    prev = val;
                }

                if (par1ICommandSender instanceof EntityPlayer || par1ICommandSender instanceof TileEntityCommandBlock)
                {
                    par1ICommandSender.addChatMessage(new ChatComponentText(String.format("%d. §a%s§r - §e%d§r", rank, rankingRows[i].username, val)));
                }
                else
                {
                    par1ICommandSender.addChatMessage(new ChatComponentText(String.format("%d. %s - %d", rank, rankingRows[i].username, val)));
                }
            }
        }
        else
        {
            par1ICommandSender.addChatMessage(new ChatComponentText("No player in the server."));
        }
    }

    private class RankingRow
    {
        public String username;
        public int hisc;
        public int sc;
        public int deaths;
    }
}
