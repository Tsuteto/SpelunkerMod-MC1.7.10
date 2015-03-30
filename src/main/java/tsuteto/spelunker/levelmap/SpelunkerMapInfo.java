package tsuteto.spelunker.levelmap;

public class SpelunkerMapInfo
{
    public String fileName;
    public String mapName;
    public MapSource source;

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
