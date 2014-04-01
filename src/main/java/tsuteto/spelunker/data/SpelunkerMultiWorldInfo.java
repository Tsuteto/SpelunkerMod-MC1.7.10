package tsuteto.spelunker.data;

import net.minecraft.nbt.NBTTagCompound;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.constants.SpelunkerGameMode;

public class SpelunkerMultiWorldInfo
{
    private String multiMode;
    public boolean hardcore;
    public int totalLives;
    public boolean isGameToBeFinished = false;

    public SpelunkerMultiWorldInfo()
    {
        multiMode = SpelunkerMod.settings().gameMode.toString();
        hardcore = SpelunkerMod.settings().hardcore;
        totalLives = SpelunkerMod.settings().initialLives;
        isGameToBeFinished = totalLives <= 0;
    }

    public SpelunkerMultiWorldInfo(NBTTagCompound nbttagcompound)
    {
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
        nbttagcompound.setString("mode", multiMode);
        nbttagcompound.setBoolean("hc", hardcore);
        nbttagcompound.setInteger("lives", totalLives);
    }

    public SpelunkerGameMode getMode()
    {
        return SpelunkerGameMode.valueOf(multiMode);
    }
}
