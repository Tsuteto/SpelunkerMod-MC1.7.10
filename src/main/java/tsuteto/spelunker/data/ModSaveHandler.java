package tsuteto.spelunker.data;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import tsuteto.spelunker.util.ModLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Handles save data for Spelunker
 *
 * @author Tsuteto
 *
 */
public class ModSaveHandler
{
    private final File saveDirectory;

    private final long now = System.currentTimeMillis();

    public ModSaveHandler(File worldDir)
    {
        this.saveDirectory = worldDir;
        this.saveDirectory.mkdirs();
    }

    protected File getSaveDirectory()
    {
        return saveDirectory;
    }

    public NBTTagCompound readData(String par1Str)
    {
        return readData(new File(this.saveDirectory, par1Str + ".dat"));
    }

    public NBTTagCompound readData(File file)
    {
        try
        {
            if (file.exists())
            {
                return CompressedStreamTools.readCompressed(new FileInputStream(file));
            }
        }
        catch (Exception var3)
        {
            ModLog.warn(var3, "Failed to load NBT data: %s", file.getAbsolutePath());
        }

        return null;
    }

    public void saveData(NBTTagCompound var2, String filename)
    {
        saveData(var2, new File(this.saveDirectory, filename + ".dat"));
    }

    public void saveData(NBTTagCompound var2, File datFile)
    {
        File tmp = new File(datFile.getParent(), datFile.getName() + ".tmp");

        try
        {
            CompressedStreamTools.writeCompressed(var2, new FileOutputStream(tmp));

            if (datFile.exists())
            {
                datFile.delete();
            }

            tmp.renameTo(datFile);
        }
        catch (Exception var5)
        {
            ModLog.warn(var5, "Failed to save NBT data: %s", datFile.getAbsolutePath());
        }
    }

    /**
     * Returns an array of usernames for which player.dat exists.
     */
    public String[] getAvailableDatList()
    {
        String[] var1 = this.saveDirectory.list();

        for (int var2 = 0; var2 < var1.length; ++var2)
        {
            if (var1[var2].endsWith(".dat"))
            {
                var1[var2] = var1[var2].substring(0, var1[var2].length() - 4);
            }
        }

        return var1;
    }
}
