package tsuteto.spelunker.item;

import com.google.common.collect.Maps;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import tsuteto.spelunker.util.ModLog;

import java.util.HashMap;
import java.util.List;

public class ItemEntityPlacer extends Item
{
    private static int nextId = 0;

    @SideOnly(Side.CLIENT)
    private IIcon theIcon;

    private static final HashMap<Integer, Info> entityRegistry = Maps.newHashMap();
    private static final HashMap<String, Integer> nameToIdMap = Maps.newHashMap();

    public ItemEntityPlacer()
    {
        this.setHasSubtypes(true);
    }

    public static void registerEntity(int entityId, String name, int color, ISpawnHandler handler)
    {
        entityRegistry.put(nextId, new Info(nextId, entityId, name, color, handler));
        nameToIdMap.put(name, nextId);
        nextId++;
    }

    public static void registerEntity(int entityId, String name, int color)
    {
        registerEntity(entityId, name, color, new DefaultSpawnHandler());
    }

    public static int getIdFromName(String name)
    {
        return nameToIdMap.get(name);
    }

    public String getItemStackDisplayName(ItemStack is)
    {
        String s = StatCollector.translateToLocal(this.getUnlocalizedName() + ".name").trim();
        Info info = entityRegistry.get(is.getItemDamage());
        if (info != null)
        {
            s = s + " " + StatCollector.translateToLocal("entity." + info.name + ".name");
        }

        return s;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack p_82790_1_, int layer)
    {
        Info info = entityRegistry.get(Integer.valueOf(p_82790_1_.getItemDamage()));
        return info != null ? (layer == 1 ? info.color : 16777215) : 16777215;
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        if (p_77648_3_.isRemote)
        {
            return true;
        }
        else
        {
            Block block = p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_);
            p_77648_4_ += Facing.offsetsXForSide[p_77648_7_];
            p_77648_5_ += Facing.offsetsYForSide[p_77648_7_];
            p_77648_6_ += Facing.offsetsZForSide[p_77648_7_];
            double d0 = 0.0D;

            if (p_77648_7_ == 1 && block.getRenderType() == 11)
            {
                d0 = 0.5D;
            }

            Entity entity = spawnEntity(p_77648_3_, p_77648_2_, p_77648_1_.getItemDamage(), (double) p_77648_4_ + 0.5D, (double) p_77648_5_ + d0, (double) p_77648_6_ + 0.5D, p_77648_7_);

            if (entity != null)
            {
                if (!p_77648_2_.capabilities.isCreativeMode)
                {
                    --p_77648_1_.stackSize;
                }
            }

            return true;
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_)
    {
        if (p_77659_2_.isRemote)
        {
            return p_77659_1_;
        }
        else
        {
            MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(p_77659_2_, p_77659_3_, true);

            if (movingobjectposition == null)
            {
                return p_77659_1_;
            }
            else
            {
                if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                {
                    int i = movingobjectposition.blockX;
                    int j = movingobjectposition.blockY;
                    int k = movingobjectposition.blockZ;

                    if (!p_77659_2_.canMineBlock(p_77659_3_, i, j, k))
                    {
                        return p_77659_1_;
                    }

                    if (!p_77659_3_.canPlayerEdit(i, j, k, movingobjectposition.sideHit, p_77659_1_))
                    {
                        return p_77659_1_;
                    }

                    if (p_77659_2_.getBlock(i, j, k) instanceof BlockLiquid)
                    {
                        Entity entity = spawnEntity(p_77659_2_, p_77659_3_, p_77659_1_.getItemDamage(), (double) i, (double) j, (double) k, movingobjectposition.sideHit);

                        if (entity != null)
                        {
                            if (!p_77659_3_.capabilities.isCreativeMode)
                            {
                                --p_77659_1_.stackSize;
                            }
                        }
                    }
                }

                return p_77659_1_;
            }
        }
    }

    /**
     * Spawns the creature specified by the egg's type in the location specified by the last three parameters.
     * Parameters: world, entityID, x, y, z.
     */
    public static Entity spawnEntity(World world, EntityPlayer player, int id, double x, double y, double z, int side)
    {
        if (!entityRegistry.containsKey(Integer.valueOf(id)))
        {
            return null;
        }
        else
        {
            Entity entity = null;
            Info info = entityRegistry.get(id);
            if (info != null)
            {
                entity = info.handler.spawn(world, player, info.spawnedID, x, y, z, side);
                if (entity != null)
                {
                    world.spawnEntityInWorld(entity);
                }
            }
            return entity;
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    /**
     * Gets an icon index based on an item's damage value and the given render pass
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int p_77618_1_, int p_77618_2_)
    {
        return p_77618_2_ > 0 ? this.theIcon : super.getIconFromDamageForRenderPass(p_77618_1_, p_77618_2_);
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_)
    {

        for (Info info : entityRegistry.values())
        {
            if (!EntityLivingBase.class.isAssignableFrom(EntityList.getClassFromID(info.spawnedID)))
            {
                p_150895_3_.add(new ItemStack(p_150895_1_, 1, info.id));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister p_94581_1_)
    {
        super.registerIcons(p_94581_1_);
        this.theIcon = p_94581_1_.registerIcon(this.getIconString() + "_overlay");
    }

    private static class Info
    {
        public int id;
        public int spawnedID;
        public String name;
        public int color;
        public ISpawnHandler handler;

        public Info(int id, int spawnedID, String name, int color, ISpawnHandler handler)
        {
            this.id = id;
            this.spawnedID = spawnedID;
            this.name = name;
            this.color = color;
            this.handler = handler;
        }
    }

    public static interface ISpawnHandler
    {
        public Entity spawn(World world, EntityPlayer player, int entityId, double x, double y, double z, int side);
    }

    public static class DefaultSpawnHandler implements ISpawnHandler
    {

        @Override
        public Entity spawn(World world, EntityPlayer player, int entityId, double x, double y, double z, int side)
        {
            Entity entity = EntityList.createEntityByID(entityId, world);

            if (entity != null)
            {
                entity.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
                //entity.rotationYaw = (float)(((MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);

                List collidingBoxes = world.getCollidingBoundingBoxes(entity, entity.boundingBox.expand(-0.1D, -0.1D, -0.1D));
                if (!collidingBoxes.isEmpty())
                {
                    ModLog.debug("%s colliding with:", entity.getCommandSenderName());
                    for (Object box : collidingBoxes) ModLog.debug(box);
                    return null;
                }
            }

            return entity;
        }
    }
}
