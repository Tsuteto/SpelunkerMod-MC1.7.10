package tsuteto.spelunker.world.levelmapper;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tsuteto.spelunker.block.tileentity.TileEntityLockedGate;
import tsuteto.spelunker.init.SpelunkerBlocks;
import tsuteto.spelunker.world.gen.WorldGenSpelunkerLevel;

public class MapPieceLockedGate extends MapPieceBlock
{
    public MapPieceLockedGate(String name, int color, int side, final int gateColor)
    {
        super(name, color);
        this.fillBlock(SpelunkerBlocks.blockLockedGate, side, new TileEntryBlock.IHandler()
        {
            @Override
            public void apply(WorldGenSpelunkerLevel gen, World world, int x, int y, int z, Block block, int meta, TileEntity tileEntity)
            {
                ((TileEntityLockedGate)tileEntity).colorId = gateColor;
            }
        });
    }
}
