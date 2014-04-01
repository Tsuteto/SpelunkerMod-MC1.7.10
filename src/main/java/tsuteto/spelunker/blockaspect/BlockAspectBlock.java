package tsuteto.spelunker.blockaspect;

import net.minecraft.block.Block;
import net.minecraft.util.DamageSource;

class BlockAspectBlock extends BlockAspectHC<Block>
{

    public BlockAspectBlock(Block block, DamageSource dmgsrc, float amount)
    {
        super(block, dmgsrc, amount);
    }

    @Override
    public boolean matches(Block block)
    {
        return target == block;
    }
}
