package tsuteto.spelunker.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.data.SpelunkerSaveHandler;
import tsuteto.spelunker.data.SpelunkerWorldInfo;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

/**
 * Command base class for Spelunker Mod
 *
 * @author Tsuteto
 *
 */
public abstract class CommandSpelunkerBase extends CommandBase
{
    protected EntityPlayer getOnlinePlayerRequired(String par1Str)
    {
        EntityPlayerMP var2 = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(par1Str);

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
        return MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(par1Str);
    }

    protected SpelunkerWorldInfo getWorldInfo(String par1Str)
    {
        EntityPlayerMP var2 = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(par1Str);

        if (var2 == null)
        {
            SpelunkerSaveHandler saveHandler = SpelunkerMod.getSaveHandler();
            SpelunkerWorldInfo worldInfo = saveHandler.loadSpelunker(par1Str);
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
