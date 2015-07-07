package tsuteto.spelunker.data;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.constants.SpelunkerGameMode;

/**
 * Handles player info of the world for Spelunker
 *
 * @author Tsuteto
 *
 */
public class SpelunkerPlayerInfo
{
    private String mode;
    private boolean hardcore;
    private int deaths;
    private int score;
    private int hiscore;
    private int lives;
    private boolean isDragonDefeated;
    private int goldenSpelunkers;
    private SpeLevelInfo speLevel;
    private String playerName;

    public SpelunkerPlayerInfo(GameProfile profile)
    {
        mode = SpelunkerMod.settings().gameMode.toString();
        hardcore = SpelunkerMod.settings().hardcore;
        deaths = 0;
        score = 0;
        hiscore = 0;
        lives = SpelunkerMod.settings().initialLives - 1;
        isDragonDefeated = false;
        goldenSpelunkers = SpelunkerMod.settings().goldenSpelunkers;
        speLevel = null;

        playerName = profile.getName();
    }

    public SpelunkerPlayerInfo(NBTTagCompound root)
    {
        playerName = root.getString("name");
        mode = root.getString("mode");
        hardcore = root.getBoolean("hc");
        deaths = root.getInteger("deaths");
        lives = root.getInteger("lives");
        score = root.getInteger("score");
        hiscore = root.getInteger("hiscore");
        goldenSpelunkers = root.getByte("gs");
        isDragonDefeated = root.getBoolean("dragon");

        if (root.hasKey("spelvl"))
        {
            speLevel = new SpeLevelInfo(root.getCompoundTag("spelvl"));
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

    private void writeToNBT(NBTTagCompound root)
    {
        root.setString("name", playerName);
        root.setString("mode", mode);
        root.setBoolean("hc", hardcore);
        root.setInteger("deaths", deaths);
        root.setInteger("lives", lives);
        root.setInteger("score", score);
        root.setInteger("hiscore", hiscore);
        root.setByte("gs", (byte) goldenSpelunkers);
        root.setBoolean("dragon", isDragonDefeated);

        if (speLevel != null)
        {
            speLevel.writeToNBT(root);
        }
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

    public SpeLevelInfo getSpeLevelInfo()
    {
        return this.speLevel;
    }

    public void createSpeLevelInfo(EntityPlayer player)
    {
        this.speLevel = new SpeLevelInfo(player);
    }

    public void discardSpeLevelInfo()
    {
        this.speLevel = null;
    }

    public boolean hasSpeLevelInfo()
    {
        return this.speLevel != null;
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
