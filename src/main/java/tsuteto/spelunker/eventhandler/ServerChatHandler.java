package tsuteto.spelunker.eventhandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.ServerChatEvent;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class ServerChatHandler
{
    @SubscribeEvent
    public void onServerChat(ServerChatEvent event)
    {
        EntityPlayerMP player = event.player;
        String message = event.message;

        if (player != null && message != null)
        {
            SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(player);
            if (spelunker != null && spelunker.isHardcore())
            {
                String forComp = message.toLowerCase().replace(" ", "");
                for (String word : SpelunkerMod.settings().cursedWords)
                {
                    if (word.length() > 0 && forComp.indexOf(word.toLowerCase().replace(" ", "")) != -1)
                    {
                        spelunker.shouldDieOfCursedWords = true;
                    }
                }
            }
        }
    }
}
