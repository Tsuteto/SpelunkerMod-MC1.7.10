package tsuteto.spelunker.util;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class UpdateNotification
{
    private static UpdateNotification instance = new UpdateNotification();

    public static UpdateNotification instance()
    {
        return instance;
    }

    private boolean isEnabled = true;
    private UpdateInfo updateInfo = null;
    private ModContainer container;
    private boolean isCompleted = false;
    private ModMetadata metadata;

    public static void initialize(Configuration conf, ModMetadata metadata)
    {
        instance.container = Loader.instance().activeModContainer();
        instance.metadata = metadata;
        instance.isEnabled = conf.get("general", "updateCheck", instance.isEnabled).getBoolean(true);

        if (instance.isEnabled)
        {
            FMLCommonHandler.instance().bus().register(instance);
        }
    }

    public boolean isCompleted()
    {
        return this.isCompleted;
    }

    @SuppressWarnings("unchecked")
    public void checkUpdate()
    {
        if (!isEnabled) return;

        new Thread("SpelunkerMod update check")
        {
            @Override
            public void run()
            {
                try
                {
                    // Receiving update info
                    String receivedData;
                    try
                    {
                        URL url = new URL(metadata.updateUrl);
                        InputStream con = url.openStream();
                        receivedData = new String(ByteStreams.toByteArray(con));
                        con.close();
                        ModLog.debug("receivedData:%n%s", receivedData);
                    } catch (IOException e)
                    {
                        ModLog.log(Level.WARN, e, "Failed to receive update info.");
                        return;
                    }

                    // Convert into Json
                    List<Map<String, Object>> updateInfoList;
                    try
                    {
                        updateInfoList = new Gson().fromJson(receivedData, List.class);
                    } catch (JsonSyntaxException e)
                    {
                        ModLog.log(Level.WARN, e, "Malformed update info.");
                        return;
                    }

                    // Retrieve update info for this MC version
                    Map<String, String> updateInfoJson = findUpdateInfoForMcVersion(updateInfoList);

                    if (updateInfoJson == null)
                    {
                        ModLog.info("No update info for this MC version.");
                        return;
                    }

                    String currVersion = container.getVersion();
                    currVersion = currVersion.substring(0, currVersion.indexOf("-"));

                    String newVersion = updateInfoJson.get("version");
                    if (!currVersion.equals(newVersion))
                    {
                        updateInfo = new UpdateInfo();
                        updateInfo.version = updateInfoJson.get("version");
                        updateInfo.downloadUrl = updateInfoJson.get("downloadUrl");
                    }
                }
                finally
                {
                    isCompleted = true;
                }
            }

            /**
             * Retrieve update info for current MC version
             * @param list
             * @return
             */
            private Map<String, String> findUpdateInfoForMcVersion(List<Map<String, Object>> list)
            {
                String currentVer = container.getVersion();
                for (Map<String, Object> map : list)
                {
                    boolean isMatched = container.acceptableMinecraftVersionRange()
                            .containsVersion(new DefaultArtifactVersion((String)map.get("mcversion")));
                    if (isMatched)
                    {
                        return (Map<String, String>)map.get("updateinfo");
                    }
                }
                return null;
            }
        }.start();
    }

    private void notifyUpdate(ICommandSender sender, Side side)
    {
        if (updateInfo != null)
        {
            if (side == Side.SERVER)
            {
                sender.addChatMessage(new ChatComponentTranslation(
                        "Spelunker.update.server", updateInfo.version, updateInfo.downloadUrl));
            }
            else
            {
                ChatStyle style = new ChatStyle();
                style.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, updateInfo.downloadUrl));

                sender.addChatMessage(new ChatComponentTranslation(
                        "Spelunker.update.client", updateInfo.version,
                        new ChatComponentTranslation("Spelunker.update.link").setChatStyle(style)));
            }

            ModLog.debug("Update available! %s", updateInfo.version);
        }

    }

    public void onServerStarting(FMLServerStartingEvent event)
    {
        if (event.getSide() == Side.SERVER)
        {
            this.notifyUpdate(event.getServer(), event.getSide());
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        this.notifyUpdate(event.player, Side.CLIENT);
    }

    public static class UpdateInfo
    {
        public String version;
        public String downloadUrl;
    }
}
