package tsuteto.spelunker.world.levelmapper;

abstract public class MapPieceSingle extends MapPiece
{
    MapPieceSingle(String name, int color)
    {
        super(name);
        MapPieces.registerMapPiece(color, this);
    }
}
