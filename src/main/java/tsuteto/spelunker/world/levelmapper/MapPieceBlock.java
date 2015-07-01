package tsuteto.spelunker.world.levelmapper;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tsuteto.spelunker.util.BlockParam;
import tsuteto.spelunker.world.gen.WorldGenSpelunkerLevel;

import java.util.List;

public class MapPieceBlock extends MapPieceSingle
{
    private List<TileEntryBlock> blockMapping = Lists.newArrayList();

    public MapPieceBlock(String name, int color)
    {
        super(name, color);
    }

    public MapPieceBlock putBlock(int x, int y, int z, Block block)
    {
        return this.putBlock(x, y, z, block, 0);
    }

    public MapPieceBlock putBlock(int x, int y, int z, Block block, int meta)
    {
        return this.putBlock(x, y, z, block, meta, null);
    }

    public MapPieceBlock putBlock(int x, int y, int z, Block block, int meta, TileEntryBlock.IHandler handler)
    {
        this.blockMapping.add(new TileEntryBlock(x, y, z, new BlockParam(block, meta)).setHandler(handler));
        return this;
    }

    public MapPieceBlock putBlock(int z, Block block)
    {
        return this.putBlock(z, block, 0);
    }

    public MapPieceBlock putBlock(int z, Block block, TileEntryBlock.IHandler handler)
    {
        return this.putBlock(z, block, 0, handler);
    }

    public MapPieceBlock putBlock(int z, Block block, int meta)
    {
        this.blockMapping.add(new TileEntryBlock(z, new BlockParam(block, meta)));
        return this;
    }

    public MapPieceBlock putBlock(int x, Block block, int meta, TileEntryBlock.IHandler handler)
    {
        this.blockMapping.add(new TileEntryBlock(x, new BlockParam(block, meta)).setHandler(handler));
        return this;
    }

    public MapPieceBlock fillBlock(Block block)
    {
        return this.fillBlock(block, 0);
    }

    public MapPieceBlock fillBlock(Block block, int meta)
    {
        return this.fillBlock(block, meta, null);
    }

    public MapPieceBlock fillBlock(Block block, int meta, TileEntryBlock.IHandler handler)
    {
        BlockParam param = new BlockParam(block, meta);

        for (int i = 0; i < 3; i++)
        {
            this.blockMapping.add(new TileEntryBlock(i, param).setHandler(handler));
        }
        return this;
    }

    @Override
    public void place(WorldGenSpelunkerLevel gen, World world, int x, int y, int z, SpelunkerLevelMapper mapper, int mapX, int mapY)
    {
        for (TileEntryBlock entry : this.blockMapping)
        {
            gen.setBlockAndNotifyAdequately(
                    world, x + entry.offsetX, y + entry.offsetY, z + entry.offsetZ, entry.block.block, entry.block.meta);
            if (entry.handler != null)
            {
                TileEntity te = world.getTileEntity(x + entry.offsetX, y + entry.offsetY, z + entry.offsetZ);
                entry.handler.apply(gen, world, x + entry.offsetX, y + entry.offsetY, z + entry.offsetZ, entry.block.block, entry.block.meta, te);
            }
        }
    }

}
