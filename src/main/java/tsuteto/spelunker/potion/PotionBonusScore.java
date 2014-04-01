package tsuteto.spelunker.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

public class PotionBonusScore extends Potion
{

    public PotionBonusScore(int par1, boolean par2, int par3)
    {
        super(par1, par2, par3);
    }

    @Override
    public void performEffect(EntityLivingBase par1EntityLivingBase, int par2)
    {
        if (par1EntityLivingBase instanceof EntityPlayerMP)
        {
            SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer((EntityPlayer)par1EntityLivingBase);
            if (spelunker != null)
            {
                spelunker.addSpelunkerScore(50);
            }
        }
    }

    @Override
    public boolean isReady(int par1, int par2)
    {
        return par1 % 5 == 0;
    }
}
