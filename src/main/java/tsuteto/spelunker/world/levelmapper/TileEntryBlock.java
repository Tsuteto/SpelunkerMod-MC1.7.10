package tsuteto.spelunker.world.levelmapper;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tsuteto.spelunker.util.BlockParam;
import tsuteto.spelunker.world.gen.WorldGenSpelunkerLevel;

class TileEntryBlock
{
    int offsetX;
    int offsetY;
    int offsetZ;
    BlockParam block;
    IHandler handler = null;

    public TileEntryBlock(int offsetZ, BlockParam block)
    {
        this(0, 0, offsetZ, block);
    }

    public TileEntryBlock(int offsetX, int offsetY, int offsetZ, BlockParam block)
    {
        this.block = block;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    public TileEntryBlock setHandler(IHandler handler)
    {
        this.handler = handler;
        return this;
    }

    interface IHandler
    {
        void apply(WorldGenSpelunkerLevel gen, World world, int x, int y, int z, Block block, int meta, TileEntity tileEntity);
    }
}
