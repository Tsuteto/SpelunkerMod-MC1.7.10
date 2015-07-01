package tsuteto.spelunker.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import tsuteto.spelunker.util.ModLog;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;

public class SpelunkerGuiHandler implements IGuiHandler
{
    public static final int GUIID_MAP_SELECTOR = 0;
    public static final int GUIID_LEAVE_SPE_WORLD = 1;
    public static final int GUIID_LEVEL_BUILDER = 2;

    private HashMap<Integer, GuiEntry> guiRegistry = Maps.newHashMap();

    public SpelunkerGuiHandler()
    {
        this.registerGuiEntry(createEntry(GUIID_MAP_SELECTOR).withName("MapSelectorPortal", "SpelunkerPortalStage", "SpelunkerPortal"));
        this.registerGuiEntry(createEntry(GUIID_LEAVE_SPE_WORLD).withName("LeaveSpeWorld", "SpelunkerPortalStage", "SpelunkerPortal"));
        this.registerGuiEntry(createEntry(GUIID_LEVEL_BUILDER).withName("MapSelectorBuilder", null, "LevelBuilder").setCoord());
    }

    private GuiEntry createEntry(int id)
    {
        if (FMLLaunchHandler.side() == Side.CLIENT)
        {
            return new GuiEntryClient(id);
        }
        else
        {
            return new GuiEntryServer(id);
        }
    }

    public void registerGuiEntry(GuiEntry entry)
    {
        guiRegistry.put(entry.id, entry);
    }

    @Override
    public Object getServerGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z)
    {
        GuiEntry entry = guiRegistry.get(guiId);
        return this.getGuiElement(player, world, x, y, z, entry, entry.getContainerClass());
    }

    @Override
    public Object getClientGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z)
    {
        GuiEntry entry = guiRegistry.get(guiId);
        return this.getGuiElement(player, world, x, y, z, entry, entry.getGuiClass());
    }

    private Object getGuiElement(EntityPlayer player, World world, int x, int y, int z, GuiEntry entry, Class<?> classToConstruct)
    {
        if (entry != null)
        {
            List<Class<?>> types = Lists.newArrayList();
            List<Object> args = Lists.newArrayList();

            types.add(player.inventory.getClass());
            args.add(player.inventory);

            if (entry.getTileEntityClass() != null)
            {
                types.add(entry.getTileEntityClass());

                TileEntity tile = world.getTileEntity(x, y, z);
                if (!entry.getTileEntityClass().isAssignableFrom(tile.getClass()))
                {
                    return null;
                }

                args.add(tile);
            }

            if (entry.hasCoord())
            {
                types.add(int.class);
                types.add(int.class);
                types.add(int.class);

                args.add(x);
                args.add(y);
                args.add(z);
            }

            try
            {
                Constructor constructor = classToConstruct.getConstructor(types.toArray(new Class<?>[types.size()]));
                return constructor.newInstance(args.toArray(new Object[args.size()]));
            }
            catch (Exception e)
            {
                ModLog.log(Level.WARN, e, "Failed to open a gui screen!");
            }
        }
        return null;
    }
}
