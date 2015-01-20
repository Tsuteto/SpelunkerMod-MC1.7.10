package tsuteto.spelunker.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.util.Utils;

import java.util.Random;

public class BlockWall extends Block
{
    private IIcon[] icons;

    protected BlockWall(Material p_i45394_1_)
    {
        super(p_i45394_1_);
    }

    @Override
    public int quantityDropped(Random p_149745_1_)
    {
        return 0;
    }

    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        long rand = Utils.generateRandomFromCoord(x, y, z);
        return this.icons[(int)(rand & 1L)];
    }

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.icons = new IIcon[2];
        this.icons[0] = p_149651_1_.registerIcon(SpelunkerMod.resourceDomain + "wall1");
        this.icons[1] = p_149651_1_.registerIcon(SpelunkerMod.resourceDomain + "wall2");

        this.blockIcon = this.icons[0];
    }
}
