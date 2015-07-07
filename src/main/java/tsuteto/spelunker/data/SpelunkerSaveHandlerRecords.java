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
public class SpelunkerSaveHandlerRecords extends ModSaveHandler
{
    private static final String filename = "level-records";

    public SpelunkerSaveHandlerRecords(File dataDir)
    {
        super(dataDir);
        ModLog.debug("Located data for multi: " + this.getSaveDirectory());
    }

    public SpeLevelRecordManager loadData()
    {
        NBTTagCompound var2 = this.readData(filename);
        if (var2 != null)
        {
            return new SpeLevelRecordManager(this, var2);
        }
        else
        {
            // Newly created
            SpeLevelRecordManager recordManager = new SpeLevelRecordManager(this);
            this.saveData(recordManager);
            ModLog.debug("Spelunker Level Record newly generated");
            return recordManager;
        }
    }

    public void saveData(SpeLevelRecordManager recordManager)
    {
        super.saveData(recordManager.getNBTTagCompound(), filename);
    }
}
