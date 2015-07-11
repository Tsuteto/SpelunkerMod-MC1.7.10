package tsuteto.spelunker.command;

import com.google.common.collect.Lists;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.StringUtils;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.constants.SpelunkerPacketType;
import tsuteto.spelunker.data.SpeLevelRecordInfo;
import tsuteto.spelunker.levelmap.SpelunkerMapInfo;
import tsuteto.spelunker.network.SpelunkerPacketDispatcher;
import tsuteto.spelunker.util.Utils;

import java.util.Arrays;
import java.util.List;

public class CommandSpeLevelRecord extends CommandSpelunkerBase
{
    enum Subcommand
    {
        show, delete
    }

    @Override
    public String getCommandName()
    {
        return "spelevelrecord";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length < 3)
        {
            throw new WrongUsageException(getCommandUsage(sender));
        }

        Subcommand sub;
        try
        {
            sub = Subcommand.valueOf(args[0]);
        }
        catch (IllegalArgumentException e)
        {
            throw new WrongUsageException(getCommandUsage(sender));
        }

        EntityPlayerMP player = getPlayer(sender, args[1]);

        String mapName = StringUtils.join(Arrays.copyOfRange(args, 2, args.length), " ");
        SpelunkerMapInfo mapInfo = SpelunkerMod.mapManager().getInfo(mapName + ".png");
        if (mapInfo == null)
        {
            throw new WrongUsageException("commands.spelevelrecord.mapNotFound");
        }

        if (sub == Subcommand.show)
        {
            SpeLevelRecordInfo.Record record = SpelunkerMod.recordManager().getRecord(player, mapInfo.fileName);
            if (record != null)
            {
                sender.addChatMessage(new ChatComponentTranslation("commands.spelevelrecord.show",
                        player.getCommandSenderName(), mapName,
                        new ChatComponentText(Utils.formatTickToTime(record.time, true))
                                .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW))));
            }
            else
            {
                sender.addChatMessage(new ChatComponentTranslation("commands.spelevelrecord.recordNotFound",
                        player.getCommandSenderName(), mapName));
            }
        }
        else if (sub == Subcommand.delete)
        {
            boolean cleared = SpelunkerMod.recordManager().clearRecord(player, mapInfo.fileName);
            if (cleared)
            {
                SpelunkerPacketDispatcher.of(SpelunkerPacketType.DEL_LEVEL_BEST).sendPacketPlayer(player);
                sender.addChatMessage(new ChatComponentTranslation("commands.spelevelrecord.delete",
                        player.getCommandSenderName(), mapName));
            }
            else
            {
                sender.addChatMessage(new ChatComponentTranslation("commands.spelevelrecord.recordNotFound",
                        player.getCommandSenderName(), mapName));
            }
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_)
    {
        switch (p_71516_2_.length)
        {
            case 1:
                return getListOfStringsMatchingLastWord(p_71516_2_, Subcommand.show.toString(), Subcommand.delete.toString());
            case 2:
                return getListOfStringsMatchingLastWord(p_71516_2_, MinecraftServer.getServer().getAllUsernames());
            case 3:
                List<String> mapNames = Lists.newArrayList();
                for (SpelunkerMapInfo info : SpelunkerMod.mapManager().getInfoList())
                {
                    mapNames.add(Utils.removeFileExtension(info.fileName));
                }
                return getListOfStringsFromIterableMatchingLastWord(p_71516_2_, mapNames);
            default:
                return null;
        }
    }

    @Override
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_)
    {
        return p_82358_2_ == 1;
    }
}
