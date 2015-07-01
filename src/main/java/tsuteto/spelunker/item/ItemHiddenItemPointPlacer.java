package tsuteto.spelunker.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.block.BlockItemBox;
import tsuteto.spelunker.block.tileentity.TileEntityItemSpawnPoint;
import tsuteto.spelunker.init.SpelunkerBlocks;

import java.util.List;

public class ItemHiddenItemPointPlacer extends Item
{
    public ItemHiddenItemPointPlacer()
    {
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
    }

    public int getColorFromItemStack(ItemStack p_82790_1_, int layer)
    {
        return this.hasItemBoxPosition(p_82790_1_) ? 0x89bc1b : 0xa0a0a0;
    }

    public boolean hasItemBoxPosition(ItemStack itemStack)
    {
        NBTTagCompound nbt = itemStack.getTagCompound();
        return nbt != null && nbt.hasKey("ItemBoxInfo");
    }

    @Override
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        Block block = p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_);

        if (block == SpelunkerBlocks.blockItemBox)
        {
            if (BlockItemBox.isHiddenBox(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_))
            {
                this.setItemBoxPosition(p_77648_1_, p_77648_4_, p_77648_5_, p_77648_6_);
                return true;
            }
        }
        else if (this.hasItemBoxPosition(p_77648_1_))
        {
            if (!p_77648_3_.isRemote)
            {
                p_77648_4_ += Facing.offsetsXForSide[p_77648_7_];
                p_77648_5_ += Facing.offsetsYForSide[p_77648_7_];
                p_77648_6_ += Facing.offsetsZForSide[p_77648_7_];
                if (p_77648_3_.isAirBlock(p_77648_4_, p_77648_5_, p_77648_6_))
                {
                    p_77648_3_.setBlock(p_77648_4_, p_77648_5_, p_77648_6_, SpelunkerBlocks.blockItemSpawnPoint);
                    TileEntityItemSpawnPoint point = (TileEntityItemSpawnPoint) p_77648_3_.getTileEntity(p_77648_4_, p_77648_5_, p_77648_6_);
                    Coord target = this.getItemBoxPosition(p_77648_1_);
                    if (target != null)
                    {
                        point.setTarget(target.posX, target.posY, target.posZ);
                        this.clearItemBoxPosition(p_77648_1_);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void setItemBoxPosition(ItemStack itemStack, int x, int y, int z)
    {
        NBTTagCompound nbt = itemStack.getTagCompound();
        if (nbt == null)
        {
            nbt = new NBTTagCompound();
            itemStack.setTagCompound(nbt);
        }

        NBTTagCompound info = new NBTTagCompound();
        info.setInteger("CoordX", x);
        info.setInteger("CoordY", y);
        info.setInteger("CoordZ", z);
        nbt.setTag("ItemBoxInfo", info);
    }

    public Coord getItemBoxPosition(ItemStack itemStack)
    {
        NBTTagCompound nbt = itemStack.getTagCompound();
        if (nbt != null && nbt.hasKey("ItemBoxInfo"))
        {
            NBTTagCompound info = nbt.getCompoundTag("ItemBoxInfo");
            return new Coord(info.getInteger("CoordX"), info.getInteger("CoordY"), info.getInteger("CoordZ"));
        }
        return null;
    }

    public void clearItemBoxPosition(ItemStack itemStack)
    {
        NBTTagCompound nbt = itemStack.getTagCompound();
        if (nbt != null && nbt.hasKey("ItemBoxInfo"))
        {
            nbt.removeTag("ItemBoxInfo");
        }
    }

    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_)
    {
        if (this.hasItemBoxPosition(p_77624_1_))
        {
            Coord coord = this.getItemBoxPosition(p_77624_1_);
            p_77624_3_.add(I18n.format(this.getUnlocalizedName() + ".info.coord", coord.posX, coord.posY, coord.posZ));
            p_77624_3_.add(I18n.format(this.getUnlocalizedName() + ".desc.3"));
            p_77624_3_.add(I18n.format(this.getUnlocalizedName() + ".desc.4"));
        }
        else
        {
            p_77624_3_.add(I18n.format(this.getUnlocalizedName() + ".info.na"));
            p_77624_3_.add(I18n.format(this.getUnlocalizedName() + ".desc.1"));
            p_77624_3_.add(I18n.format(this.getUnlocalizedName() + ".desc.2"));
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister p_94581_1_)
    {
        this.itemIcon = p_94581_1_.registerIcon(SpelunkerMod.resourceDomain + "rodCross");
    }

    private static class Coord
    {
        int posX;
        int posY;
        int posZ;

        public Coord(int posX, int posY, int posZ)
        {
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
        }
    }
}
