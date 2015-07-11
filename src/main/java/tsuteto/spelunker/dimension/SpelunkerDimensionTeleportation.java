package tsuteto.spelunker.dimension;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldProvider;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.player.SpelunkerPlayerMP;
import tsuteto.spelunker.world.WorldProviderSpelunker;

public class SpelunkerDimensionTeleportation
{
    public static void transferPlayerToDimension(EntityPlayerMP par1EntityPlayerMP, int par2)
    {
        transferPlayerToDimension(par1EntityPlayerMP, par2, false);
    }

    public static void transferPlayerToDimension(EntityPlayerMP par1EntityPlayerMP, int par2, boolean tryAgain)
    {
        WorldProvider worldProvider = par1EntityPlayerMP.mcServer.worldServerForDimension(par2).provider;
        SpelunkerTeleporter teleporter = new SpelunkerTeleporter(par1EntityPlayerMP.mcServer.worldServerForDimension(par2));
        par1EntityPlayerMP.mcServer.getConfigurationManager().transferPlayerToDimension(
                par1EntityPlayerMP, par2, teleporter);
        
        // Try again if failed
        if (worldProvider instanceof WorldProviderSpelunker && !teleporter.isSucceeded)
        {
            if (!tryAgain)
            {
                transferPlayerToDimension(par1EntityPlayerMP, par2, true);
            }
            else
            {
                // Return the player if failed twice
                SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(par1EntityPlayerMP);
                transferPlayerToDimension(par1EntityPlayerMP, spelunker.getDimIdFromSpelunkerWorld());
            }
        }
    }
}
