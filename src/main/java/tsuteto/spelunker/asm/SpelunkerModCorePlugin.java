package tsuteto.spelunker.asm;

import java.io.File;
import java.util.Map;
import java.util.logging.Logger;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.TransformerExclusions("tsuteto.spelunker.asm")
@IFMLLoadingPlugin.MCVersion("1.7.10")
public class SpelunkerModCorePlugin implements IFMLLoadingPlugin
{
    static File location;
    public static Logger log;
    public static String currentMcVersion = (String)cpw.mods.fml.relauncher.FMLInjectionData.data()[4];
    public static Boolean isObfuscated;

    public SpelunkerModCorePlugin()
    {
        log = Logger.getLogger("SpelunkerMod-core");
        //System.out.println("*** SpelunkerModCore ***");
    }

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[]{TransformerMain.class.getName()};
    }

    @Override
    public String getModContainerClass()
    {
        return null;
    }

    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data)
    {
        if (data.containsKey("coremodLocation"))
        {
            location = (File) data.get("coremodLocation");
        }
        if (data.containsKey("runtimeDeobfuscationEnabled"))
        {
            isObfuscated = (Boolean) data.get("runtimeDeobfuscationEnabled");
        }
    }

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }
}
