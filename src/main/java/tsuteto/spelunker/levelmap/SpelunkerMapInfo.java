package tsuteto.spelunker.levelmap;

public class SpelunkerMapInfo
{
    public MapSource source;
    public String fileName;
    public String mapName;

    public SpelunkerMapInfo() {}

    public SpelunkerMapInfo(MapSource source, String fileName, String mapName)
    {
        this.source = source;
        this.fileName = fileName;
        this.mapName = mapName;
    }

    @Override
    public int hashCode()
    {
        return fileName.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof SpelunkerMapInfo)) return false;
        return fileName != null && fileName.equals(((SpelunkerMapInfo) obj).fileName);
    }
}
