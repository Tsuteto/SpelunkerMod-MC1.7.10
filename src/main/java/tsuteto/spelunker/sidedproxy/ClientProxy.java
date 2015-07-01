package tsuteto.spelunker.sidedproxy;

import api.player.client.ClientPlayerAPI;
import api.player.client.IClientPlayerAPI;
import api.player.server.IServerPlayerAPI;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.eventhandler.ClientTickHandler;
import tsuteto.spelunker.gui.ScreenRenderer;
import tsuteto.spelunker.init.SpelunkerBlocks;
import tsuteto.spelunker.init.SpelunkerEntity;
import tsuteto.spelunker.player.ISpelunkerPlayer;
import tsuteto.spelunker.player.SpelunkerPlayerSP;
import tsuteto.spelunker.util.Utils;

import java.io.File;

public class ClientProxy implements ISidedProxy
{

    /**
     * Returns Spelunker instance by PlayerAPI
     *
     * @param player
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends ISpelunkerPlayer> T getSpelunkerPlayer(EntityPlayer player)
    {
        if (player instanceof EntityPlayerSP)
        {
            return (T) ((IClientPlayerAPI) player).getClientPlayerBase(SpelunkerMod.modId);
        }
        else if (player instanceof EntityPlayerMP)
        {
            return (T) ((IServerPlayerAPI) player).getServerPlayerBase(SpelunkerMod.modId);
        }
        // EntityOtherPlayerMP, LittleMaid or EntityPlayer-extended entities
        return null;
    }

    @Override
    public void registerComponent(SpelunkerMod mod)
    {
        MinecraftForge.EVENT_BUS.register(new ScreenRenderer());
        FMLCommonHandler.instance().bus().register(new ClientTickHandler(mod));

        ClientPlayerAPI.register(SpelunkerMod.modId, SpelunkerPlayerSP.class);

        SpelunkerEntity.registerEntityRenderer();
        SpelunkerBlocks.registerBlockRenderer();
    }

    @Override
    public void registerTextureFile(String filepath)
    {
    }

    @Override
    public void checkBgmSoundFile()
    {
        // BGM file check
        if (Utils.soundFileExists("main_bgm"))
            SpelunkerMod.isBgmMainAvailable = true;

        if (Utils.soundFileExists("2xscore_bgm"))
            SpelunkerMod.isBgm2xScoreAvailable = true;

        if (Utils.soundFileExists("invincible_bgm"))
            SpelunkerMod.isBgmInvincibleAvailable = true;

        if (Utils.soundFileExists("speedpotion_bgm"))
            SpelunkerMod.isBgmSpeedPotionAvailable = true;

        if (Utils.soundFileExists("ghost_bgm"))
            SpelunkerMod.isBgmGhostComingAvailable = true;

        if (Utils.soundFileExists("checkpoint"))
            SpelunkerMod.isBgmCheckPointAvailable = true;

        if (Utils.soundFileExists("allcleared"))
            SpelunkerMod.isBgmAllCleardAvailable = true;
    }

    @Override
    public File getMapDataDir()
    {
        Minecraft mc = FMLClientHandler.instance().getClient();
        return new File(mc.mcDataDir, SpelunkerMod.levelMapFileDir);
    }

}
