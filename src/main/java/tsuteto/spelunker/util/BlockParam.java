package tsuteto.spelunker.util;

import net.minecraft.block.Block;

public class BlockParam
{
    public Block block;
    public int meta;

    public BlockParam(Block block, int meta)
    {
        this.block = block;
        this.meta = meta;
    }
}
