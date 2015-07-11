package tsuteto.spelunker.command;

import net.minecraft.command.ICommandSender;
import tsuteto.spelunker.eventhandler.GhostSpawnHandler;

public class CommandResetGhost extends CommandSpelunkerBase
{

    @Override
    public String getCommandName()
    {
        return "resetghost";
    }

    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_)
    {
        GhostSpawnHandler.resetGhostList();
    }
}
