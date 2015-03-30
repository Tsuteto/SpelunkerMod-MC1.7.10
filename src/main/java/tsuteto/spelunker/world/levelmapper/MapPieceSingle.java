package tsuteto.spelunker.world.levelmapper;

abstract class MapPieceSingle implements MapPiece
{
    private String name;
    private int color;

    MapPieceSingle(String name, int color)
    {
        this.name = name;
        this.color = color;
        MapPieces.registerMapPiece(color, this);
    }

    @Override
    public int getColor()
    {
        return this.color | (0xff << 24);
    }

    @Override
    public String getName()
    {
        return this.name;
    }
}
