package tsuteto.spelunker.item;

import net.minecraft.block.BlockOre;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class SpelunkerBlockOre extends BlockOre
{
    private int score;
    private String obtainedSound = null;

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
                if (obtainedSound != null)
                {
                    spelunker.playSound(obtainedSound, 1.0f, 1.0f);
                }
            }
        }
    }

    public SpelunkerBlockOre setSpelunkerScore(int i)
    {
        score = i;
        return this;
    }

    public SpelunkerBlockOre setObtainedSound(String str)
    {
        obtainedSound = str;
        return this;
    }
}
