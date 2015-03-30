package tsuteto.spelunker.dimension;

import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;
import net.minecraftforge.common.DimensionManager;
import tsuteto.spelunker.network.PacketDispatcher;
import tsuteto.spelunker.network.PacketManager;
import tsuteto.spelunker.network.packet.PacketDimRegistration;
import tsuteto.spelunker.util.ModLog;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class SpelunkerLevelManager
{
    public static final String levelDataFileName = "spelunkerLevels.dat";

    private final int dimTypeId;
    private final File levelDataFile;
    private final Map<Integer, SpelunkerLevelInfo> registeredLevels = Maps.newHashMap();

    public SpelunkerLevelManager(int dimTypeId, File dataDir)
    {
        this.dimTypeId = dimTypeId;
        this.levelDataFile = new File(dataDir, levelDataFileName);
    }

    public void loadLevelData()
    {
        if (levelDataFile.exists())
        {
            JsonReader reader = null;
            try
            {
                reader = new JsonReader(new FileReader(levelDataFile));
                SpelunkerLevelInfo[] data;
                try
                {
                    //noinspection unchecked
                    data = new Gson().fromJson(reader, SpelunkerLevelInfo[].class);
                }
                catch (JsonSyntaxException e)
                {
                    ModLog.warn(e, "Malformed level info.");
                    return;
                }

                if (data != null)
                {
                    for (SpelunkerLevelInfo info : data)
                    {
                        registeredLevels.put(info.dimId, info);
                    }
                }
            }
            catch (IOException e)
            {
                ModLog.warn(e, "Failed to read Spelunker level info");
            }
            finally
            {
                if (reader != null)
                {
                    try
                    {
                        reader.close();
                    }
                    catch (IOException ignored) {}
                }
            }
        }
    }

    public void saveLevelData()
    {
        JsonWriter writer = null;
        try
        {
            writer = new JsonWriter(new FileWriter(levelDataFile));
            writer.setIndent(" ");
            new Gson().toJson(registeredLevels.values(), Collection.class, writer);
        }
        catch (IOException e)
        {
            ModLog.warn(e, "Failed to save Spelunker level info");
        }
        finally
        {
            if (writer != null)
            {
                try
                {
                    writer.close();
                }
                catch (IOException ignored) {}
            }
        }
    }

    public void register(SpelunkerLevelInfo info)
    {
        registeredLevels.put(info.dimId, info);
        if (!DimensionManager.isDimensionRegistered(info.dimId))
        {
            DimensionManager.registerDimension(info.dimId, dimTypeId);
        }
        saveLevelData();
    }

    public void registerAll()
    {
        for (int dimId : registeredLevels.keySet())
        {
            DimensionManager.registerDimension(dimId, dimTypeId);
        }
    }

    public void unregisterAll()
    {
        for (int dimId : registeredLevels.keySet())
        {
            if (DimensionManager.isDimensionRegistered(dimId))
            {
                DimensionManager.unregisterDimension(dimId);
            }
        }
    }

    public SpelunkerLevelInfo getLevelInfo(int dimId)
    {
        return this.registeredLevels.get(dimId);
    }

    public void syncAllLevels(NetworkManager manager)
    {
        manager.scheduleOutboundPacket(this.buildPacket());
    }

    private FMLProxyPacket buildPacket()
    {
        ByteBuf payload = Unpooled.buffer();
        PacketDimRegistration packet = new PacketDimRegistration(Ints.toArray(registeredLevels.keySet()));
        packet.encodeInto(payload);

        return new FMLProxyPacket(payload, PacketManager.CP_CHANNEL_NAME);
    }

    public void syncLevel(int dimId, EntityPlayer player)
    {
        PacketDispatcher.packet(new PacketDimRegistration(dimId)).sendToPlayer(player);
    }
}
