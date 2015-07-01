package tsuteto.spelunker.world.levelmapper;

import net.minecraft.client.resources.I18n;
import net.minecraft.world.World;
import tsuteto.spelunker.block.tileentity.TileEntityItemBoxHidden;
import tsuteto.spelunker.init.SpelunkerBlocks;
import tsuteto.spelunker.world.gen.WorldGenSpelunkerLevel;

public class MapPieceItemHidden extends MapPiece
{
    MapPieceItemHidden(String name, int color)
    {
        super(name);
        for (int i = 0; i < 16; i++)
        {
            MapPieces.registerMapPiece(color + i, this);
        }
    }

    @Override
    public void place(WorldGenSpelunkerLevel gen, World world, int x, int y, int z, SpelunkerLevelMapper mapper, int mapX, int mapY)
    {
        gen.setBlockAndNotifyAdequately(
                world, x, y, z + 1, SpelunkerBlocks.blockItemBox, 1);

        TileEntityItemBoxHidden te = (TileEntityItemBoxHidden)world.getTileEntity(x, y, z + 1);
        int id = mapper.getColor(mapX, mapY) & 0xf;
        gen.hiddenItemMapper.registerItemBox(id, te);
    }

    @Override
    public String getLocalizedName(int color)
    {
        return I18n.format("Spelunker.mapPiece." + this.getName(color), (color & 0xf) + 1);
    }
}
