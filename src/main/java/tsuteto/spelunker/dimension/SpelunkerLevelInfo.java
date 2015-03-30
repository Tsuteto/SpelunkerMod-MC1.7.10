package tsuteto.spelunker.dimension;

import net.minecraft.nbt.NBTTagCompound;

public class SpelunkerLevelInfo
{
    public static final String NBT_ROOT = "SpeLvl";
    public static final String NBT_DIM = "Dim";
    public static final String NBT_MAP_FILE = "MapFile";
    public static final String NBT_LEVEL_NAME = "LevelName";

    public int dimId;
    public String mapFileName;
    public String levelName;

    public static boolean hasInfo(NBTTagCompound nbt)
    {
        return nbt != null && nbt.hasKey(NBT_ROOT);
    }

    public void readFromNBT(NBTTagCompound nbt)
    {
        NBTTagCompound root = nbt.getCompoundTag(NBT_ROOT);
        dimId = root.getInteger(NBT_DIM);
        mapFileName = root.getString(NBT_MAP_FILE);
        levelName = root.getString(NBT_LEVEL_NAME);
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
        NBTTagCompound root = new NBTTagCompound();
        root.setInteger(NBT_DIM, dimId);
        root.setString(NBT_MAP_FILE, mapFileName);
        root.setString(NBT_LEVEL_NAME, levelName);
        nbt.setTag(NBT_ROOT, root);
    }
}
