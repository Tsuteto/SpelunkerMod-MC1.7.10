package tsuteto.spelunker.sidedproxy;

import net.minecraft.entity.player.EntityPlayer;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.player.ISpelunkerPlayer;

public interface ISidedProxy
{
    <T extends ISpelunkerPlayer> T getSpelunkerPlayer(EntityPlayer player);
    void registerComponent(SpelunkerMod mod);
    void registerTextureFile(String filepath);
    void checkBgmSoundFile();
    void installSoundFiles();
}
