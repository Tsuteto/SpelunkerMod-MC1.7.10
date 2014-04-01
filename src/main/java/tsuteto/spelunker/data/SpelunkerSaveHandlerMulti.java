package tsuteto.spelunker.data;

import java.io.File;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Handles save data for Spelunker
 *
 * @author Tsuteto
 *
 */
public class SpelunkerSaveHandlerMulti extends ModSaveHandler
{
    private static final String filename = "SpelunkerMod2";

    public SpelunkerSaveHandlerMulti(File worldDir)
    {
        super(worldDir);
    }

    public SpelunkerMultiWorldInfo loadData()
    {
        NBTTagCompound var2 = this.readData(filename);
        if (var2 != null)
        {
            return new SpelunkerMultiWorldInfo(var2);
        }
        else
        {
            return null;
        }
    }

    public void saveData(SpelunkerMultiWorldInfo multiWorldInfo)
    {
        super.saveData(multiWorldInfo.getNBTTagCompound(), filename);
    }
}
