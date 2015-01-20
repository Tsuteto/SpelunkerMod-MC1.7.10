package tsuteto.spelunker.data;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.NBTTagCompound;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.constants.SpelunkerGameMode;

/**
 * Handles world info for Spelunker
 *
 * @author Tsuteto
 *
 */
public class SpelunkerWorldInfo
{
    private String mode;
    private boolean hardcore;
    private int deaths;
    private int score;
    private int hiscore;
    private int lives;
    private boolean isDragonDefeated;
    private int goldenSpelunkers;
    private String playerName;

    public SpelunkerWorldInfo()
    {
        mode = SpelunkerMod.settings().gameMode.toString();
        hardcore = SpelunkerMod.settings().hardcore;
        deaths = 0;
        score = 0;
        hiscore = 0;
        lives = SpelunkerMod.settings().initialLives - 1;
        isDragonDefeated = false;
        goldenSpelunkers = SpelunkerMod.settings().goldenSpelunkers;
        playerName = null;
    }

    public SpelunkerWorldInfo(GameProfile profile)
    {
        super();
        playerName = profile.getName();
    }

    public SpelunkerWorldInfo(NBTTagCompound nbttagcompound)
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

        if (mode.isEmpty())
        {
            mode = SpelunkerGameMode.Adventure.toString();
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
        nbttagcompound.setString("name", playerName);
        nbttagcompound.setString("mode", mode);
        nbttagcompound.setBoolean("hc", hardcore);
        nbttagcompound.setInteger("deaths", deaths);
        nbttagcompound.setInteger("lives", lives);
        nbttagcompound.setInteger("score", score);
        nbttagcompound.setInteger("hiscore", hiscore);
        nbttagcompound.setByte("gs", (byte)goldenSpelunkers);
        nbttagcompound.setBoolean("dragon", isDragonDefeated);
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

    public boolean isDragonDefeated()
    {
        return isDragonDefeated;
    }

    public void setDragonDefeated()
    {
        isDragonDefeated = true;
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
