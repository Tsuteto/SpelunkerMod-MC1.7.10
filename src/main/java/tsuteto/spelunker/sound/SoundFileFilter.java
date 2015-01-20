package tsuteto.spelunker.sound;

import java.io.File;
import java.io.FileFilter;

public class SoundFileFilter implements FileFilter
{
    private String soundName;

    public SoundFileFilter(String soundName)
    {
        this.soundName = soundName;
    }

    @Override
    public boolean accept(File file)
    {
        if (!file.isFile())
            return false;
        String fullname = file.getName();
        int p = fullname.lastIndexOf(".");
        if (p == -1)
            return false;
        String name = fullname.substring(0, p);
        String ext = fullname.substring(p + 1);
        return name.equals(soundName)
                && (ext.equalsIgnoreCase("ogg") || ext.equalsIgnoreCase("mus"));
    }
}
