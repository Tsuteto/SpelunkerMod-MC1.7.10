package tsuteto.spelunker.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemItemBox extends ItemBlock
{
    public static final String[] blockNames = new String[]{"normal", "hidden"};

    private IIcon[] icons;

    public ItemItemBox(Block p_i45332_1_)
    {
        super(p_i45332_1_);
    }

    public int getMetadata(int p_77647_1_)
    {
        return p_77647_1_;
    }

    public String getUnlocalizedName(ItemStack p_77667_1_)
    {
        int dmg = p_77667_1_.getItemDamage();
        if (dmg < 0 || dmg >= blockNames.length)
        {
            dmg = 0;
        }
        return field_150939_a.getUnlocalizedName() + "." + blockNames[dmg];
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int dmg)
    {
        if (dmg < 0 || dmg >= blockNames.length)
        {
            dmg = 0;
        }
        return icons[dmg];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister p_94581_1_)
    {
        this.icons = new IIcon[blockNames.length];
        String s = this.field_150939_a.getItemIconName();
        for (int i = 0; i < blockNames.length; i++)
        {
            this.icons[i] = p_94581_1_.registerIcon(s + "_" + blockNames[i]);
        }
    }
}