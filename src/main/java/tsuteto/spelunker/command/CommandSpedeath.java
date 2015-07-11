package tsuteto.spelunker.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

import java.util.List;

/**
 * Command /spedeath
 *
 * @author Tsuteto
 *
 */
public class CommandSpedeath extends CommandSpelunkerBase
{
    @Override
    public String getCommandName()
    {
        return "spedeath";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return true;
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        if (SpelunkerMod.settings().playerStatusInPublic)
        {
            return super.getCommandUsage(par1ICommandSender);
        }
        else
        {
            if (super.canCommandSenderUseCommand(par1ICommandSender))
            {
                return super.getCommandUsage(par1ICommandSender);
            }
            else
            {
                return super.getCommandUsage(par1ICommandSender) + ".private";
            }
        }
    }

    @Override
    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        String res;
        if (par2ArrayOfStr.length >= 1
                && (SpelunkerMod.settings().playerStatusInPublic || super.canCommandSenderUseCommand(par1ICommandSender)))
        {
            EntityPlayerMP entityPlayer = (EntityPlayerMP) this.getOnlinePlayerRequired(par2ArrayOfStr[0]);
            SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(entityPlayer);
            if (spelunker != null)
            {
                par1ICommandSender.addChatMessage(new ChatComponentTranslation("commands.spedeath.other",
                        entityPlayer.getCommandSenderName(),
                        new ChatComponentText(String.valueOf(spelunker.deaths)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW))));
            }
            else
            {
                throw new PlayerNotFoundException();
            }
        }
        else
        {
            EntityPlayerMP entityPlayer = getCommandSenderAsPlayer(par1ICommandSender);
            SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(entityPlayer);
            if (spelunker != null)
            {
                par1ICommandSender.addChatMessage(new ChatComponentTranslation("commands.spedeath.player",
                        new ChatComponentText(String.valueOf(spelunker.deaths)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW))));
            }
            else
            {
                throw new PlayerNotFoundException();
            }
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_)
    {
        switch (p_71516_2_.length)
        {
            case 1:
                return getListOfStringsMatchingLastWord(p_71516_2_, MinecraftServer.getServer().getAllUsernames());
            default:
                return null;
        }
    }

    @Override
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_)
    {
        return p_82358_2_ == 0;
    }
}
