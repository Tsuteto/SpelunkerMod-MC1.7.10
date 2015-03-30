package tsuteto.spelunker.dimension;

import net.minecraft.entity.player.EntityPlayerMP;

public class SpelunkerDimensionTeleportation
{
    public static void transferPlayerToDimension(EntityPlayerMP par1EntityPlayerMP, int par2)
    {
        transferPlayerToDimension(par1EntityPlayerMP, par2, false);
    }

    public static void transferPlayerToDimension(EntityPlayerMP par1EntityPlayerMP, int par2, boolean tryAgain)
    {
        SpelunkerTeleporter teleporter = new SpelunkerTeleporter(par1EntityPlayerMP.mcServer.worldServerForDimension(par2));
        par1EntityPlayerMP.mcServer.getConfigurationManager().transferPlayerToDimension(
                par1EntityPlayerMP, par2, teleporter);
        
        // Try again if failed
        if (par2 != 0 && !teleporter.isSucceeded)
        {
            if (!tryAgain)
            {
                transferPlayerToDimension(par1EntityPlayerMP, par2, true);
            }
            else
            {
                transferPlayerToDimension(par1EntityPlayerMP, 0);
            }
        }
    }
}
