package tsuteto.spelunker.world.levelmapper;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tsuteto.spelunker.block.tileentity.TileEntityItemBox;
import tsuteto.spelunker.init.SpelunkerBlocks;
import tsuteto.spelunker.world.gen.WorldGenSpelunkerLevel;

public class MapPieceItem extends MapPieceBlock
{
    public MapPieceItem(String name, int color)
    {
        super(name, color);
    }

    public MapPieceItem setItem(ItemStack itemStack)
    {
        return this.setItem(1, itemStack);
    }

    public MapPieceItem setItem(int offsetZ, ItemStack itemStack)
    {
        this.putBlock(offsetZ, SpelunkerBlocks.blockItemBox, new ItemContainerHandler(itemStack));
        return this;
    }

    class ItemContainerHandler implements TileEntryBlock.IHandler
    {
        ItemStack itemStack;

        public ItemContainerHandler(ItemStack itemStack)
        {
            this.itemStack = itemStack;
        }

        @Override
        public void apply(WorldGenSpelunkerLevel gen, World world, int x, int y, int z, Block block, int meta, TileEntity tileEntity)
        {
            ((TileEntityItemBox) tileEntity).setItemStack(this.itemStack);
        }
    }
}
