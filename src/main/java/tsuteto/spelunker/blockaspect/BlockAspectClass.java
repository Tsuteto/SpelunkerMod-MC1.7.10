package tsuteto.spelunker.blockaspect;

import net.minecraft.block.Block;
import net.minecraft.util.DamageSource;

class BlockAspectClass extends BlockAspectHC<Class>
{

    public BlockAspectClass(Class cond, DamageSource dmgsrc, float amount)
    {
        super(cond, dmgsrc, amount);
    }

    @Override
    public boolean matches(Block block)
    {
        return target.isAssignableFrom(block.getClass());
    }
}
