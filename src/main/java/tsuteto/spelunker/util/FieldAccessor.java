package tsuteto.spelunker.util;

import java.lang.reflect.Field;

public class FieldAccessor<T>
{
    private final Field fld;

    public FieldAccessor(Field fld)
    {
        this.fld = fld;
        fld.setAccessible(true);
    }

    public T get(Object obj) throws IllegalArgumentException, IllegalAccessException
    {
        return (T)fld.get(obj);
    }

    public void set(Object obj, T val) throws IllegalArgumentException, IllegalAccessException
    {
        fld.set(obj, val);
    }

    public T getSafely(Object obj)
    {
        try
        {
            return (T)fld.get(obj);
        } catch (IllegalAccessException e)
        {
            ModLog.warn(e, "Reflection error: get %s", fld.getName());
            return null;
        }
    }

    public void setSafely(Object obj, T val)
    {
        try
        {
            fld.set(obj, val);
        } catch (IllegalAccessException e)
        {
            ModLog.warn(e, "Reflection error: set %s", fld.getName());
        }
    }
}
