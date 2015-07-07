package tsuteto.spelunker.data;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Manages score for the Spelunker
 *
 * @author Tsuteto
 *
 */
public class ScoreManager
{
    public int scoreActual = 0;
    public int scoreDisplay = 0;
    public int hiscore = 0;

    public ScoreManager()
    {
    }

    public void load(SpelunkerPlayerInfo modinfo)
    {
        scoreActual = modinfo.getScore();
        scoreDisplay = scoreActual;
        hiscore = modinfo.getHiscore();
    }

    public void store(SpelunkerPlayerInfo modinfo)
    {
        modinfo.setScore(scoreActual);
        modinfo.setHiscore(hiscore);
    }

    @SideOnly(Side.CLIENT)
    public void onTick()
    {
        int diff = scoreActual - scoreDisplay;

        if (diff >= 100)
        {
            scoreDisplay += 100;
        }
        else if (diff >= 10)
        {
            scoreDisplay += 10;
        }
        else if (diff != 0)
        {
            scoreDisplay = scoreActual;
        }
    }

    public void addScore(int amount)
    {
        scoreActual += amount;
        if (hiscore < scoreActual)
        {
            hiscore = scoreActual;
        }
    }

    @SideOnly(Side.CLIENT)
    public void initScore(int amount)
    {
        scoreActual = amount;
        scoreDisplay = amount;
    }

    @SideOnly(Side.CLIENT)
    public void setScore(int amount)
    {
        scoreActual = amount;
        if (hiscore < scoreActual)
        {
            hiscore = scoreActual;
        }
    }

    @SideOnly(Side.CLIENT)
    public int getDisplayScore()
    {
        return scoreDisplay & 0xffffff;
    }

    public void reset()
    {
        scoreActual = scoreDisplay = 0;
    }
}
