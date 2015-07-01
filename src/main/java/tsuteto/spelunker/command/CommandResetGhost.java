package tsuteto.spelunker.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import tsuteto.spelunker.eventhandler.GhostSpawnHandler;

/**
 * Created by Tsuteto on 15/07/01.
 */
public class CommandResetGhost extends CommandSpelunkerBase
{

    @Override
    public String getCommandName()
    {
        return "resetghost";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_)
    {
        if (p_71518_1_ instanceof EntityPlayer)
        {
            return "/resetghost";
        }
        else
        {
            return "/resetghost [dimId]";
        }
    }

    @Override
    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_)
    {
        if (p_71515_1_ instanceof EntityPlayer)
        {
            GhostSpawnHandler.resetGhostList(p_71515_1_.getEntityWorld());
        }
        else
        {
            if (p_71515_2_.length < 1)
            {
                throw new WrongUsageException("/resetghost [dimId]");
            }
            World world = MinecraftServer.getServer().worldServerForDimension(parseInt(p_71515_1_, p_71515_2_[0]));
            GhostSpawnHandler.resetGhostList(world);
        }
    }
}
