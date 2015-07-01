package tsuteto.spelunker.data;

import net.minecraft.nbt.NBTTagCompound;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.constants.SpelunkerGameMode;

public class SpelunkerWorldGeneralInfo
{
    public boolean isWorldInitialized = false;
    private String multiMode;
    public boolean hardcore;
    public int totalLives;
    public boolean isGameToBeFinished = false;

    public SpelunkerWorldGeneralInfo()
    {
        multiMode = SpelunkerMod.settings().gameMode.toString();
        hardcore = SpelunkerMod.settings().hardcore;
        totalLives = SpelunkerMod.settings().initialLives;
        isGameToBeFinished = totalLives <= 0;
    }

    public SpelunkerWorldGeneralInfo(NBTTagCompound nbttagcompound)
    {
        isWorldInitialized = nbttagcompound.getBoolean("initw");
        multiMode = nbttagcompound.getString("mode");
        hardcore = nbttagcompound.getBoolean("hc");
        totalLives = nbttagcompound.getInteger("lives");
        isGameToBeFinished = totalLives <= 0;

        if (multiMode.isEmpty())
        {
            multiMode = SpelunkerGameMode.Adventure.toString();
        }
    }

    public NBTTagCompound getNBTTagCompound()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        updateTagCompound(nbttagcompound);
        return nbttagcompound;
    }

    private void updateTagCompound(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setBoolean("initw", isWorldInitialized);
        nbttagcompound.setString("mode", multiMode);
        nbttagcompound.setBoolean("hc", hardcore);
        nbttagcompound.setInteger("lives", totalLives);
    }

    public SpelunkerGameMode getMode()
    {
        return SpelunkerGameMode.valueOf(multiMode);
    }
}
