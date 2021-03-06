package tsuteto.spelunker.data;

import net.minecraft.nbt.NBTTagCompound;
import tsuteto.spelunker.util.ModLog;

import java.io.File;

/**
 * Handles save data for Spelunker
 *
 * @author Tsuteto
 *
 */
public class SpelunkerSaveHandlerWorld extends ModSaveHandler
{
    private static final String filename = "SpelunkerMod";

    public SpelunkerSaveHandlerWorld(File worldDir)
    {
        super(worldDir);
        ModLog.debug("Located data for multi: " + this.getSaveDirectory());
    }

    public SpelunkerGeneralInfo loadData()
    {
        this.migration();

        NBTTagCompound var2 = this.readData(filename);
        if (var2 != null)
        {
            return new SpelunkerGeneralInfo(var2);
        }
        else
        {
            SpelunkerGeneralInfo worldInfo = new SpelunkerGeneralInfo();
            this.saveData(worldInfo);
            ModLog.debug("World general info newly generated");
            return worldInfo;
        }
    }

    public void saveData(SpelunkerGeneralInfo multiWorldInfo)
    {
        super.saveData(multiWorldInfo.getNBTTagCompound(), filename);
    }

    private void migration()
    {
        File oldFile = new File(this.getSaveDirectory(), "SpelunkerMod2.dat");
        File newFile = new File(this.getSaveDirectory(), filename + ".dat");

        if (oldFile.exists() && oldFile.isFile() && !newFile.exists())
        {
            oldFile.renameTo(newFile);
        }
    }
}
