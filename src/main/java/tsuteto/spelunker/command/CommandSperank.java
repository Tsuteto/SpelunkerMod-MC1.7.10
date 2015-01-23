package tsuteto.spelunker.command;

import com.google.common.collect.Lists;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.ChatComponentText;
import org.apache.commons.lang3.StringUtils;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.data.SpelunkerWorldInfo;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Command /sperank
 *
 * @author Tsuteto
 *
 */
public class CommandSperank extends CommandSpelunkerBase
{
    enum Type
    {
        hisc("High Score", new Comparator<RankingRow>()
        {
            @Override
            public int compare(RankingRow a, RankingRow b)
            {
                Integer val1 = a.hisc;
                Integer val2 = b.hisc;
                return val2.compareTo(val1);
            }
        }),
        sc("Current Score", new Comparator<RankingRow>()
        {
            @Override
            public int compare(RankingRow a, RankingRow b)
            {
                Integer val1 = a.sc;
                Integer val2 = b.sc;
                return val2.compareTo(val1);
            }
        }),
        death("Number of Deaths", new Comparator<RankingRow>()
        {
            @Override
            public int compare(RankingRow a, RankingRow b)
            {
                Integer val1 = a.deaths;
                Integer val2 = b.deaths;
                return val2.compareTo(val1);
            }
        });

        public String title;
        public Comparator<RankingRow> comparator;

        Type(String title, Comparator<RankingRow> comparator)
        {
            this.comparator = comparator;
            this.title = title;
        }
    }

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
        return "/sperank sc|hisc|death [page 1-|all]";
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] args)
    {
        Type type;
        int page = 0;

        // Get arguments
        if (args.length == 0)
        {
            throw new WrongUsageException(getCommandUsage(commandSender));
        }
        else
        {
            try
            {
                type = Type.valueOf(args[0]);
            }
            catch (IllegalArgumentException e)
            {
                throw new WrongUsageException(getCommandUsage(commandSender));
            }

            if (args.length == 2)
            {
                if (args[1].equals("all"))
                {
                    page = -1;
                }
                else
                {
                    try
                    {
                        page = Integer.valueOf(args[1]) - 1;
                    }
                    catch (NumberFormatException e)
                    {
                        throw new WrongUsageException(getCommandUsage(commandSender));
                    }
                }
            }
        }

        // Obtain world info
        String[] entryList;
        if (MinecraftServer.getServer().isSinglePlayer())
        {
            entryList = new String[]{commandSender.getCommandSenderName()};
        }
        else
        {
            entryList = SpelunkerMod.getSaveHandler().getAvailableSpelunkerDat();
       }

        List<RankingRow> rankingRows = Lists.newArrayList();

        if (page <= (entryList.length - 1) / 10)
        {
            // Load spelunker data
            for (String entryName : entryList)
            {
                RankingRow rankingData = new RankingRow();

                EntityPlayer player = getOnlinePlayer(entryName);
                if (player != null)
                {
                    SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(player);
                    if (spelunker == null) continue;
                    rankingData.username = entryName;
                    rankingData.deaths = spelunker.getDeaths();
                    rankingData.sc = spelunker.getSpelunkerScore().scoreActual;
                    rankingData.hisc = spelunker.getSpelunkerScore().hiscore;
                }
                else
                {
                    SpelunkerWorldInfo worldInfo = getWorldInfo(entryName);
                    if (worldInfo == null) continue;
                    if (StringUtils.isNotEmpty(worldInfo.getPlayerName()))
                    {
                        rankingData.username = worldInfo.getPlayerName();
                    }
                    else
                    {
                        rankingData.username = entryName;
                    }
                    rankingData.deaths = worldInfo.getDeaths();
                    rankingData.sc = worldInfo.getScore();
                    rankingData.hisc = worldInfo.getHiscore();
                }
                rankingRows.add(rankingData);
            }

            // Sort
            Collections.sort(rankingRows, type.comparator);
        }

        // Display
        String pageGuide;
        if (page == -1)
        {
            pageGuide = "ALL";
        }
        else
        {
            pageGuide = String.format("%d/%d", page + 1, (entryList.length - 1) / 10 + 1);
        }

        if (commandSender instanceof EntityPlayer || commandSender instanceof TileEntityCommandBlock)
        {
            commandSender.addChatMessage(new ChatComponentText(
                    String.format("=== <Spelunker §6%s§r Ranking> %s ===", type.title, pageGuide)));
        }
        else
        {
            commandSender.addChatMessage(new ChatComponentText(
                    String.format("=== <Spelunker %s Ranking> %s ===", type.title, pageGuide)));
        }

        if (rankingRows.size() > 0)
        {
            int rank = 1;
            int prev = -1;
            for (int i = 0; i < rankingRows.size(); i++)
            {
                int val = 0;

                if (type == Type.hisc)
                {
                    val = rankingRows.get(i).hisc;
                }
                else if (type == Type.sc)
                {
                    val = rankingRows.get(i).sc;
                }
                else if (type == Type.death)
                {
                    val = rankingRows.get(i).deaths;
                }

                if (val != prev)
                {
                    rank = i + 1;
                    prev = val;
                }

                if (page == -1 || i >= page * 10 && i <= (page + 1) * 10 - 1)
                {
                    if (commandSender instanceof EntityPlayer || commandSender instanceof TileEntityCommandBlock)
                    {
                        commandSender.addChatMessage(new ChatComponentText(String.format("%d. §a%s§r - §e%d§r", rank, rankingRows.get(i).username, val)));
                    }
                    else
                    {
                        commandSender.addChatMessage(new ChatComponentText(String.format("%d. %s - %d", rank, rankingRows.get(i).username, val)));
                    }
                }
            }
        }
        else
        {
            commandSender.addChatMessage(new ChatComponentText("No records"));
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
