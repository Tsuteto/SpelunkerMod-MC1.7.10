package tsuteto.spelunker.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class SpelunkerBlockRedstoneOre extends BlockRedstoneOre
{
    private int score;

    public SpelunkerBlockRedstoneOre(boolean par2)
    {
        super(par2);
    }

    @Override
    public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6)
    {
        super.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);
        if (!par1World.isRemote)
        {
            SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer(par2EntityPlayer);
            if (spelunker != null)
            {
                spelunker.addSpelunkerScore(score);
            }
        }
    }

    public Block setSpelunkerScore(int i)
    {
        score = i;
        return this;
    }
}
