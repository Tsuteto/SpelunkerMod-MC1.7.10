package tsuteto.spelunker.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockCPStatue extends Block
{
    private static final int ICON_MAX_NUM = 20;

    private final int id;
    private IIcon[] icons;

    public BlockCPStatue(int id)
    {
        super(Material.rock);
        this.id = id;
    }

    @Override
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return icons[p_149691_2_];
    }

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.icons = new IIcon[16];

        for (int i = 0; i < this.icons.length; i++)
        {
            int iconId = i + this.id * this.icons.length;
            if (iconId < ICON_MAX_NUM)
            {
                icons[i] = p_149651_1_.registerIcon(this.getTextureName() + "_" + iconId);
            }
        }
    }
}
