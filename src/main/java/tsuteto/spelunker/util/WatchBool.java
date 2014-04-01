package tsuteto.spelunker.util;

/**
 * Watches changes of a boolean parameter
 *
 * @author Tsuteto
 *
 */
public class WatchBool
{
    boolean val = false;

    public WatchBool(boolean val)
    {
        this.val = val;
    }

    public boolean checkVal(Boolean newVal)
    {
        if (val != newVal)
        {
            val = newVal;
            return true;
        }
        return false;
    }

    public boolean getVal()
    {
        return val;
    }

    public void setVal(boolean val)
    {
        this.val = val;
    }
}
