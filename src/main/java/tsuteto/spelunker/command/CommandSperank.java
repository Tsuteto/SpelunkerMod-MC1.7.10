package tsuteto.spelunker.command;

import com.google.common.collect.Lists;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.StringUtils;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.data.SpelunkerPlayerInfo;
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
        hisc(new Comparator<RankingRow>()
        {
            @Override
            public int compare(RankingRow a, RankingRow b)
            {
                Integer val1 = a.hisc;
                Integer val2 = b.hisc;
                return val2.compareTo(val1);
            }
        }),
        sc(new Comparator<RankingRow>()
        {
            @Override
            public int compare(RankingRow a, RankingRow b)
            {
                Integer val1 = a.sc;
                Integer val2 = b.sc;
                return val2.compareTo(val1);
            }
        }),
        death(new Comparator<RankingRow>()
        {
            @Override
            public int compare(RankingRow a, RankingRow b)
            {
                Integer val1 = a.deaths;
                Integer val2 = b.deaths;
                return val2.compareTo(val1);
            }
        });

        public Comparator<RankingRow> comparator;

        Type(Comparator<RankingRow> comparator)
        {
            this.comparator = comparator;
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
        return "commands.sperank.usage";
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

            if (args.length >= 2)
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
        if (SpelunkerMod.isSinglePlayer())
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
                    if (spelunker != null)
                    {
                        rankingData.username = entryName;
                        rankingData.deaths = spelunker.getDeaths();
                        rankingData.sc = spelunker.getSpelunkerScore().scoreActual;
                        rankingData.hisc = spelunker.getSpelunkerScore().hiscore;
                    }
                }
                else
                {
                    SpelunkerPlayerInfo worldInfo = getWorldInfo(entryName);
                    if (worldInfo != null)
                    {
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
                }
                rankingRows.add(rankingData);
            }

            // Sort
            Collections.sort(rankingRows, type.comparator);
        }

        // Display
        ChatComponentTranslation pageGuide;
        if (page == -1)
        {
            pageGuide = new ChatComponentTranslation("commands.sperank.all");
        }
        else
        {
            pageGuide = new ChatComponentTranslation("commands.sperank.pageGuide", page + 1, (entryList.length - 1) / 10 + 1);
        }

        commandSender.addChatMessage(new ChatComponentTranslation("commands.sperank.header",
                new ChatComponentTranslation("commands.sperank." + type.name()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)), pageGuide));

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
            commandSender.addChatMessage(new ChatComponentTranslation("commands.sperank.noRecords"));
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_)
    {
        switch (p_71516_2_.length)
        {
            case 1:
                return getListOfStringsMatchingLastWord(p_71516_2_,
                        Type.death.toString(), Type.sc.toString(), Type.hisc.toString());
            default:
                return null;
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
