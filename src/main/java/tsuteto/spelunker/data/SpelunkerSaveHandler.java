package tsuteto.spelunker.data;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import tsuteto.spelunker.player.SpelunkerPlayerMP;
import tsuteto.spelunker.util.ModLog;

import java.io.File;

/**
 * Handles save data for Spelunker
 *
 * @author Tsuteto
 *
 */
public class SpelunkerSaveHandler extends ModSaveHandler
{
    private final String oldFileNameForSingle = "spelunker";
    private final boolean isSingle;
    private File oldSaveDirForSingle;

    private final long now = System.currentTimeMillis();

    public SpelunkerSaveHandler(File worldDir, boolean isSingle)
    {
        super(new File(worldDir, "spelunkers"));
        this.oldSaveDirForSingle = worldDir;
        this.isSingle = isSingle;
    }

    public void migrateSaveData(String username, String uuid)
    {
        File oldFile, newFile;
        if (isSingle)
        {
            oldFile = new File(oldSaveDirForSingle, oldFileNameForSingle + ".dat");
        }
        else
        {
            oldFile = new File(super.getSaveDirectory(), username + ".dat");
        }

        newFile = new File(super.getSaveDirectory(), uuid + ".dat");

        if (oldFile.exists() && oldFile.isFile() && !newFile.exists())
        {
            SpelunkerWorldInfo info = this.loadSpelunker(oldFile);
            info.setPlayerName(username);
            super.saveData(info.getNBTTagCompound(), oldFile);

            oldFile.renameTo(newFile);
        }
    }

    public SpelunkerWorldInfo loadSpelunker(File file)
    {
        NBTTagCompound var2 = super.readData(file);

        if (var2 != null)
        {
            return new SpelunkerWorldInfo(var2);
        }
        else
        {
            return null;
        }
    }

    public SpelunkerWorldInfo loadSpelunker(String filename)
    {
        NBTTagCompound var2 = super.readData(filename);

        if (var2 != null)
        {
            return new SpelunkerWorldInfo(var2);
        }
        else
        {
            return null;
        }
    }

    public void saveSpelunker(SpelunkerPlayerMP spelunker)
    {
        EntityPlayerMP entityPlayer = spelunker.player();
        String filename = entityPlayer.getUniqueID().toString();

        try
        {
            NBTTagCompound var2 = spelunker.getWorldInfo().getNBTTagCompound();
            super.saveData(var2, filename);
        }
        catch (Exception var5)
        {
            ModLog.warn(var5, "Failed to save spelunker data: %s", filename + ".dat");
        }
    }

    /**
     * Returns an array of usernames for which player.dat exists for.
     */
    public String[] getAvailableSpelunkerDat()
    {
        return super.getAvailableDatList();
    }
}
