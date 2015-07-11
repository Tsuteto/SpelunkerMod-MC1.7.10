package tsuteto.spelunker.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.constants.SpelunkerPacketType;
import tsuteto.spelunker.data.SpelunkerGeneralInfo;
import tsuteto.spelunker.network.SpelunkerPacketDispatcher;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class CommandSpeHardcore extends CommandSpelunkerBase
{
    @Override
    public String getCommandName()
    {
        return "spehardcore";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length < 1)
        {
            throw new WrongUsageException(getCommandUsage(sender));
        }

        boolean isHardcore = parseBoolean(sender, args[0]);

        SpelunkerGeneralInfo worldInfo = SpelunkerMod.getWorldInfo();
        worldInfo.hardcore = isHardcore;
        SpelunkerMod.saveWorldInfo();

        for (Object obj : this.getPlayerEntities())
        {
            EntityPlayerMP player = (EntityPlayerMP)obj;
            SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(player);
            if (spelunker != null) spelunker.setSpelunkerExtra(true);
        }
        SpelunkerPacketDispatcher.of(SpelunkerPacketType.TOGGLE_HC)
                .addBool(isHardcore)
                .sendPacketAll();
    }

}
