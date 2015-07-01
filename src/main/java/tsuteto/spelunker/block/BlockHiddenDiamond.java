package tsuteto.spelunker.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.player.SpelunkerPlayerMP;

import java.util.Random;

public class BlockHiddenDiamond extends BlockBreakable
{
    public BlockHiddenDiamond(Material p_i45394_1_)
    {
        super(p_i45394_1_);
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return Items.diamond;
    }

    @Override
    public int quantityDropped(Random p_149745_1_)
    {
        return 1;
    }

    public void onBlockDestroyedByExplosion(World world, int i, int j, int k, Explosion explosion)
    {
        if (!world.isRemote && this.blockExists(world, i, j, k))
        {
            EntityLivingBase exploder = explosion.getExplosivePlacedBy();
            if (exploder instanceof EntityPlayer)
            {
                SpelunkerPlayerMP spelunker = SpelunkerMod.getSpelunkerPlayer((EntityPlayer) exploder);
                if (spelunker != null)
                {
                    spelunker.addSpelunkerScore(10000);
                    world.playSoundAtEntity(exploder, "spelunker:diamond", 1.0f, 1.0f);
                }
            }
            this.dropBlockAsItemWithChance(world, i, j, k, world.getBlockMetadata(i, j, k), 1.0F, 0);
        }

        super.onBlockDestroyedByExplosion(world, i, j, k, explosion);
    }

    @Override
    public boolean canDropFromExplosion(Explosion p_149659_1_)
    {
        return false;
    }
}
