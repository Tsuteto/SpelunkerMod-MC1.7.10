package tsuteto.spelunker.blockaspect;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.DamageSource;

class BlockAspectMaterial extends BlockAspectHC<Material>
{

    public BlockAspectMaterial(Material material, DamageSource dmgsrc, float amount)
    {
        super(material, dmgsrc, amount);
    }

    @Override
    public boolean matches(Block block)
    {
        return target.equals(block.getMaterial());
    }

}
