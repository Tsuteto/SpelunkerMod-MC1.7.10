package tsuteto.spelunker.world.levelmapper;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tsuteto.spelunker.block.BlockRespawnPoint;

public class MapPieceRespawnPoint extends MapPieceBlock
{
    public MapPieceRespawnPoint(int color)
    {
        super(color);
    }

    @Override
    public MapPieceBlock putBlock(int z, Block block)
    {
        return super.putBlock(z, block, new TileEntryBlock.IHandler()
        {
            @Override
            public void apply(World world, int x, int y, int z, Block block, int meta, TileEntity tileEntity)
            {
                ((BlockRespawnPoint) block).initTileEntity(world, x, y, z);
            }
        });
    }
}
