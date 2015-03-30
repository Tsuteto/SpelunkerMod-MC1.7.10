package tsuteto.spelunker.levelmap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.util.ResourceInstaller;
import tsuteto.spelunker.util.Utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.Map;

public class SpelunkerMapManager
{
    public static final String[] SAMPLE_FILES = new String[]{"Sample Map.png"};

    private Map<String, SpelunkerMapInfo> fileNameToInfoMapping = Maps.newHashMap();
    private List<SpelunkerMapInfo> mapFileList = Lists.newArrayList();

    public SpelunkerMapManager(File mapDir)
    {
        boolean isInitial = !mapDir.exists();

        if (isInitial && !mapDir.mkdirs()) return;

        if (isInitial || SpelunkerMod.settings().restoreSample)
        {
            this.installSampleMaps(mapDir);
        }

        this.registerResourceMaps();
        this.registerUserMaps(mapDir);
    }

    private void installSampleMaps(File mapDir)
    {
        ResourceInstaller installer = new ResourceInstaller(mapDir);
        for (String filename : SAMPLE_FILES)
        {
            installer.addResource("/assets/spelunker/leveldata/samples/" + filename, filename);
        }
        installer.install();
    }

    private void registerResourceMaps()
    {
        SpelunkerMapInfo info = new SpelunkerMapInfo();
        info.fileName = "SpelunkerMap.png";
        info.mapName = "Famicom Spelunker Map (Half)";
        info.source = MapSource.RESOURCE;
        this.addMap(info);
    }

    private void registerUserMaps(File mapDir)
    {
        File[] files = mapDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name)
            {
                if (new File(dir, name).isFile())
                {
                    int dotpos = name.lastIndexOf(".");
                    if (dotpos > 0)
                    {
                        String ext = name.substring(dotpos + 1);
                        return "png".equalsIgnoreCase(ext);
                    }
                }
                return false;
            }
        });

        for (File file : files)
        {
            SpelunkerMapInfo info = new SpelunkerMapInfo();
            info.fileName = file.getName();
            info.mapName = Utils.removeFileExtension(file.getName());
            info.source = MapSource.USER;
            this.addMap(info);
        }
    }

    private void addMap(SpelunkerMapInfo info)
    {
        fileNameToInfoMapping.put(info.fileName, info);
        mapFileList.add(info);
    }

    public Map<String, SpelunkerMapInfo> getInfoMapping()
    {
        return fileNameToInfoMapping;
    }

    public SpelunkerMapInfo getInfo(String filename)
    {
        return fileNameToInfoMapping.get(filename);
    }

    public List<SpelunkerMapInfo> getInfoList()
    {
        return mapFileList;
    }
}
