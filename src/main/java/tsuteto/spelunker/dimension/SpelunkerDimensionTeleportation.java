package tsuteto.spelunker.dimension;

import net.minecraft.entity.player.EntityPlayerMP;

public class SpelunkerDimensionTeleportation
{
    public void transferPlayerToDimension(EntityPlayerMP par1EntityPlayerMP, int par2)
    {
        par1EntityPlayerMP.mcServer.getConfigurationManager().transferPlayerToDimension(
                par1EntityPlayerMP, par2,
                new SpelunkerTeleporter(par1EntityPlayerMP.mcServer.worldServerForDimension(par2)));
    }
}
