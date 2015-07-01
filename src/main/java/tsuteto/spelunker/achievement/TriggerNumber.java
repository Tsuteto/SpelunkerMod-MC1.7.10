package tsuteto.spelunker.achievement;

public class TriggerNumber implements AchievementTrigger
{
    private final int number;

    public TriggerNumber(int number)
    {
        this.number = number;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj != null && obj instanceof Integer)
        {
            return (Integer)obj >= this.number;
        }
        else
        {
            return false;
        }
    }

}
