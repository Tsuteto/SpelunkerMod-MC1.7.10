package tsuteto.spelunker.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class SpelunkerMaterial
{
    public static final Material invisible = new Material(MapColor.airColor)
    {
        public boolean isSolid()
        {
            return false;
        }
        public boolean getCanBlockGrass()
        {
            return false;
        }
        public boolean blocksMovement()
        {
            return false;
        }
    };
}
