package tsuteto.spelunker.levelmap;

public class SpelunkerMapInfo
{
    public MapSource source;
    public String fileName;
    public String mapName;
    public long revision;

    public SpelunkerMapInfo() {}

    public SpelunkerMapInfo(MapSource source, String fileName, String mapName, long revision)
    {
        this.source = source;
        this.fileName = fileName;
        this.mapName = mapName;
        this.revision = revision;
    }

    @Override
    public int hashCode()
    {
        return fileName.hashCode() + this.source.ordinal();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof SpelunkerMapInfo)) return false;
        SpelunkerMapInfo another = (SpelunkerMapInfo) obj;
        return fileName != null && fileName.equals(another.fileName) && source == another.source;
    }
}
