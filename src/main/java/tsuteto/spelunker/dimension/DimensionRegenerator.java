package tsuteto.spelunker.dimension;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class DimensionRegenerator
{
    public static boolean regenerateDimension(int dimId)
    {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        WorldServer world = server.worldServerForDimension(dimId);
        File dir = new File(DimensionManager.getCurrentSaveRootDirectory(), world.provider.getSaveFolder());

        for (Object obj : server.getConfigurationManager().playerEntityList)
        {
            EntityPlayerMP player = (EntityPlayerMP)obj;

            if (player.dimension == dimId)
            {
                SpelunkerDimensionTeleportation.transferPlayerToDimension(player, 0);
            }
        }

        try
        {
            world.saveAllChunks(true, null);
        }
        catch (MinecraftException e)
        {
            return false;
        }

        world.flush();

        MinecraftForge.EVENT_BUS.post(new WorldEvent.Unload(world));

        DimensionManager.setWorld(dimId, null);

        if (dir.exists())
        {
            try
            {
                FileUtils.deleteDirectory(dir);
            }
            catch (IOException e)
            {
                return false;
            }
        }

        if (DimensionManager.shouldLoadSpawn(dimId))
        {
            world = server.worldServerForDimension(dimId);

            try
            {
                world.saveAllChunks(true, null);
            }
            catch (MinecraftException e)
            {
                return false;
            }

            world.flush();
        }

        return true;
    }
}
