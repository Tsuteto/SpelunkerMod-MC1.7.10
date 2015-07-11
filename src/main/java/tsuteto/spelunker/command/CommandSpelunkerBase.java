package tsuteto.spelunker.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.data.SpelunkerPlayerInfo;
import tsuteto.spelunker.data.SpelunkerSaveHandler;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

import java.util.List;

/**
 * Command base class for Spelunker Mod
 *
 * @author Tsuteto
 *
 */
public abstract class CommandSpelunkerBase extends CommandBase
{
    @Override
    public String getCommandUsage(ICommandSender p_71518_1_)
    {
        return "commands." + this.getCommandName() + ".usage";
    }

    protected EntityPlayer getOnlinePlayerRequired(String par1Str)
    {
        EntityPlayerMP var2 = MinecraftServer.getServer().getConfigurationManager().func_152612_a(par1Str); //getPlayerForUsername

        if (var2 == null)
        {
            throw new PlayerNotFoundException();
        }
        else
        {
            return var2;
        }
    }

    protected EntityPlayer getOnlinePlayer(String par1Str)
    {
        return MinecraftServer.getServer().getConfigurationManager().func_152612_a(par1Str);
    }

    protected String[] getPlayers()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }

    protected List getPlayerEntities()
    {
        return MinecraftServer.getServer().getConfigurationManager().playerEntityList;
    }

    protected SpelunkerPlayerInfo getWorldInfo(String par1Str)
    {
        EntityPlayerMP var2 = MinecraftServer.getServer().getConfigurationManager().func_152612_a(par1Str);

        if (var2 == null)
        {
            SpelunkerSaveHandler saveHandler = SpelunkerMod.getSaveHandler();
            SpelunkerPlayerInfo worldInfo = saveHandler.loadSpelunker(par1Str);
            if (worldInfo == null)
            {
                return null;
            }
            else
            {
                return worldInfo;
            }
        }
        else
        {
            SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(var2);
            if (spelunker == null)
            {
                return null;
            }
            else
            {
                return spelunker.getWorldInfo();
            }
        }
    }

}
