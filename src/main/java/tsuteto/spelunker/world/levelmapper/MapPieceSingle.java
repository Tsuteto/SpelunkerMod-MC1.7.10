package tsuteto.spelunker.world.levelmapper;

abstract class MapPieceSingle implements MapPiece
{
    private int color;

    MapPieceSingle(int color)
    {
        this.color = color;
        SpelunkerLevelMapper.registerMapPiece(color, this);
    }

    @Override
    public int getColor()
    {
        return this.color;
    }
}
