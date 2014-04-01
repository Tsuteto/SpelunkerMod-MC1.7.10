package tsuteto.spelunker.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

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

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
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
            return "/spedeath [player]";
        }
        else
        {
            if (super.canCommandSenderUseCommand(par1ICommandSender))
            {
                return "/spedeath [player]";
            }
            else
            {
                return "/spedeath";
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
                res = String.format("Number of %1$s's deaths: %2$d", entityPlayer.getCommandSenderName(), spelunker.deaths);
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
                res = String.format("Number of your deaths: %2$d", entityPlayer.getCommandSenderName(), spelunker.deaths);
            }
            else
            {
                throw new PlayerNotFoundException();
            }
        }
        par1ICommandSender.addChatMessage(new ChatComponentText(res));
    }
}
