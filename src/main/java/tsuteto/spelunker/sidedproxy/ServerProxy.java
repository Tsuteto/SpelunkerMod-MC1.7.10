package tsuteto.spelunker.sidedproxy;

import api.player.server.IServerPlayerAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.player.ISpelunkerPlayer;


public class ServerProxy implements ISidedProxy
{
    /**
     * Returns Spelunker instance by PlayerAPI
     *
     * @param player
     * @return
     */
    @Override
    public <T extends ISpelunkerPlayer> T getSpelunkerPlayer(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
        {
            return (T) ((IServerPlayerAPI) player).getServerPlayerBase(SpelunkerMod.modId);
        }
        // LittleMaid or entities extending EntityPlayer
        return null;
    }

    @Override
    public void registerComponent(SpelunkerMod mod)
    {
    }

    @Override
    public void registerTextureFile(String filepath)
    {
    }

    @Override
    public void checkBgmSoundFile()
    {
    }

    @Override
    public void installSoundFiles()
    {
    }
}
