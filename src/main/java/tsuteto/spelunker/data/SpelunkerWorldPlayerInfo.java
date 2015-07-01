package tsuteto.spelunker.data;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.NBTTagCompound;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.constants.SpelunkerGameMode;

import java.util.List;

/**
 * Handles player info of the world for Spelunker
 *
 * @author Tsuteto
 *
 */
public class SpelunkerWorldPlayerInfo
{
    private String mode;
    private boolean hardcore;
    private int deaths;
    private int score;
    private int hiscore;
    private int lives;
    private List<Byte> passedCheckPoints = Lists.newArrayList();
    private boolean isSpeLevelCleared;
    private boolean isDragonDefeated;
    private int goldenSpelunkers;
    private String playerName;

    public SpelunkerWorldPlayerInfo(GameProfile profile)
    {
        mode = SpelunkerMod.settings().gameMode.toString();
        hardcore = SpelunkerMod.settings().hardcore;
        deaths = 0;
        score = 0;
        hiscore = 0;
        lives = SpelunkerMod.settings().initialLives - 1;
        isSpeLevelCleared = false;
        isDragonDefeated = false;
        goldenSpelunkers = SpelunkerMod.settings().goldenSpelunkers;
        playerName = profile.getName();
    }

    public SpelunkerWorldPlayerInfo(NBTTagCompound nbttagcompound)
    {
        playerName = nbttagcompound.getString("name");
        mode = nbttagcompound.getString("mode");
        hardcore = nbttagcompound.getBoolean("hc");
        deaths = nbttagcompound.getInteger("deaths");
        lives = nbttagcompound.getInteger("lives");
        score = nbttagcompound.getInteger("score");
        hiscore = nbttagcompound.getInteger("hiscore");
        goldenSpelunkers = nbttagcompound.getByte("gs");
        isDragonDefeated = nbttagcompound.getBoolean("dragon");

        if (nbttagcompound.hasKey("spelvl"))
        {
            NBTTagCompound speLevel = nbttagcompound.getCompoundTag("spelvl");
            isSpeLevelCleared = speLevel.getBoolean("cleared");

            byte[] chkpts = speLevel.getByteArray("chkpts");
            for (byte chkpt : chkpts)
            {
                passedCheckPoints.add(chkpt);
            }
        }

        if (mode.isEmpty())
        {
            mode = SpelunkerGameMode.Adventure.toString();
        }
    }

    public NBTTagCompound getNBTTagCompound()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeToNBT(nbttagcompound);
        return nbttagcompound;
    }

    private void writeToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setString("name", playerName);
        nbttagcompound.setString("mode", mode);
        nbttagcompound.setBoolean("hc", hardcore);
        nbttagcompound.setInteger("deaths", deaths);
        nbttagcompound.setInteger("lives", lives);
        nbttagcompound.setInteger("score", score);
        nbttagcompound.setInteger("hiscore", hiscore);
        nbttagcompound.setByte("gs", (byte)goldenSpelunkers);
        nbttagcompound.setBoolean("dragon", isDragonDefeated);

        NBTTagCompound speLevel = new NBTTagCompound();
        speLevel.setBoolean("specleared", isSpeLevelCleared);
        byte[] chkpts = new byte[passedCheckPoints.size()];
        for (int i = 0; i < passedCheckPoints.size(); i++)
        {
            chkpts[i] = (byte)(int) passedCheckPoints.get(i);
        }

        speLevel.setByteArray("chkpts", chkpts);
        nbttagcompound.setTag("spelvl", speLevel);
    }

    public int getDeaths()
    {
        return deaths;
    }

    public void setDeaths(int i)
    {
        deaths = i;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    public int getHiscore()
    {
        return hiscore;
    }

    public void setHiscore(int hiscore)
    {
        this.hiscore = hiscore;
    }

    public int getLives()
    {
        return lives;
    }

    public void setLives(int lives)
    {
        this.lives = lives;
    }

    public SpelunkerGameMode getMode()
    {
        return SpelunkerGameMode.valueOf(mode);
    }

    public void setMode(SpelunkerGameMode initialMode)
    {
        this.mode = initialMode.toString();
    }

    public boolean isHardcore()
    {
        return hardcore;
    }

    public void setHardcore(boolean hardcore)
    {
        this.hardcore = hardcore;
    }

    public boolean isCheckPointPassed(int no)
    {
        return passedCheckPoints.contains((byte)no);
    }

    public int getCheckPointCount()
    {
        return passedCheckPoints.size();
    }

    public void setCheckPointPassed(int no)
    {
        Byte b = (byte)no;
        if (!passedCheckPoints.contains(b))
        {
            passedCheckPoints.add(b);
        }
    }

    public void resetCheckPoints()
    {
        this.passedCheckPoints.clear();
    }

    public boolean isSpeLevelCleared()
    {
        return isSpeLevelCleared;
    }

    public void setSpeLevelCleared(boolean isSpeLevelCleared)
    {
        this.isSpeLevelCleared = isSpeLevelCleared;
    }

    public boolean isDragonDefeated()
    {
        return isDragonDefeated;
    }

    public void setDragonDefeated()
    {
        this.isDragonDefeated = true;
    }

    public int getGoldenSpelunkers()
    {
        return goldenSpelunkers;
    }

    public void setGoldenSpelunkers(int goldenSpelunkers)
    {
        this.goldenSpelunkers = goldenSpelunkers;
    }

    public void addGoldenSpelunker()
    {
        this.goldenSpelunkers += 1;
    }

    public String getPlayerName()
    {
        return playerName;
    }

    public void setPlayerName(String playerName)
    {
        this.playerName = playerName;
    }
}
