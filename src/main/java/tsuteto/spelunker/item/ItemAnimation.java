package tsuteto.spelunker.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public abstract class ItemAnimation extends SpelunkerItem
{
    protected IIcon[] icons;
    protected int animationTick = 0;

    @Override
    public void animate()
    {
        this.itemIcon = this.icons[animationTick];
        animationTick = ++animationTick % this.getAnimationFlames();
    }

    public abstract int getAnimationFlames();
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister par1IconRegister)
    {
        this.icons = new IIcon[this.getAnimationFlames()];
        for (int i = 0; i < this.icons.length; i++)
        {
            this.icons[i] = par1IconRegister.registerIcon(this.getUnlocalizedName().replace("item.", "") + "_" + i);
        }
        this.itemIcon = this.icons[0];
    }
}
