package tsuteto.spelunker.data;

import java.io.File;
import java.util.logging.Level;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import tsuteto.spelunker.player.SpelunkerPlayerMP;
import tsuteto.spelunker.util.ModLog;

/**
 * Handles save data for Spelunker
 *
 * @author Tsuteto
 *
 */
public class SpelunkerSaveHandler extends ModSaveHandler
{
    private final String fileNameForSingle = "spelunker";
    private final boolean isSingle;

    private final long now = System.currentTimeMillis();

    public SpelunkerSaveHandler(File worldDir, boolean isSingle)
    {
        super(isSingle ? worldDir : new File(worldDir, "spelunkers"));

        this.isSingle = isSingle;
    }

    public SpelunkerWorldInfo loadSpelunker(String username)
    {
        String filename = isSingle ? fileNameForSingle : username;
        NBTTagCompound var2 = this.getSpelunkerData(filename);

        if (var2 != null)
        {
            return new SpelunkerWorldInfo(var2);
        }
        else
        {
            return null;
        }
    }

    public NBTTagCompound getSpelunkerData(String par1Str)
    {
        return super.readData(par1Str);
    }

    public void saveSpelunker(SpelunkerPlayerMP spelunker)
    {
        EntityPlayerMP entityPlayer = spelunker.player();
        String filename = isSingle ? fileNameForSingle : entityPlayer.getCommandSenderName();

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
