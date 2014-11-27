package tsuteto.spelunker.entity;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import tsuteto.spelunker.Settings;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.item.ItemEntityPlacer;
import tsuteto.spelunker.item.SpelunkerItem;

public class SpelunkerEntity
{
    public static int renderIdRope = RenderingRegistry.getNextAvailableRenderId();

    public static void register(SpelunkerMod core, Settings settings)
    {
        // Register entity
        $(EntityGunBullet.class, "gunBullet", settings.entityGunBulletId, core)
                .setMotionParams(64, 20, false).register();
        $(EntityBatDroppings.class, "batDroppings", settings.entityBatDroppingsId, core)
                .setMotionParams(64, 10, true).register();
        $(EntityFlashBullet.class, "flashBullet", settings.entityFlashBulletId, core)
                .setMotionParams(64, 5, true).register();
        $(EntityFlash.class, "flash", settings.entityFlashId, core)
                .setMotionParams(64, 5, true).register();
        $(EntitySpelunkerItem.class, "speItem", settings.spelunkerItemId, core)
                .setMotionParams(64, 5, true).register();

        /*
         * Spelunker Level Components
         */
        $(EntityElevator.class, "elevator", settings.entityElevatorId, core)
                .setMotionParams(64, 20, true)
                .registerToEntityPlacer(0x00aa22, new ItemEntityPlacer.ISpawnHandler()
                {
                    @Override
                    public Entity spawn(World world, EntityPlayer player, int entityId, double x, double y, double z)
                    {
                        Entity entity = EntityList.createEntityByID(entityId, world);

                        if (entity != null)
                        {
                            entity.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
                            entity.rotationYaw = (float)(((MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);

                            if (!world.getCollidingBoundingBoxes(entity, entity.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty())
                            {
                                return null;
                            }

                            world.spawnEntityInWorld(entity);
                        }

                        return entity;
                    }
                }).register();

        $(EntityLift.class, "lift", settings.entityLiftId, core)
                .setMotionParams(64, 20, false).register();

        $(EntitySteamHole.class, "steamHole", settings.entitySteamId, core)
                .setMotionParams(64, 20, false)
                .registerToEntityPlacer(0x881100)
                .register();

    }

    private static EntityRegister $(Class<? extends Entity> entityClass, String name, int id, Object modInstance)
    {
        return new EntityRegister(entityClass, name, id, modInstance);
    }

    @SideOnly(Side.CLIENT) // added on purpose!
    public static void registerEntityRenderer()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityGunBullet.class, new RenderGunBullet());
        RenderingRegistry.registerEntityRenderingHandler(EntityBatDroppings.class, new RenderBatDroppings());
        RenderingRegistry.registerEntityRenderingHandler(EntityFlash.class, new RenderFlash());
        RenderingRegistry.registerEntityRenderingHandler(EntityFlashBullet.class, new RenderFlashBullet());
        RenderingRegistry.registerEntityRenderingHandler(EntitySpelunkerItem.class, new RenderSpelunkerItem());

        RenderingRegistry.registerEntityRenderingHandler(EntityElevator.class, new RenderElevator());
        RenderingRegistry.registerEntityRenderingHandler(EntityLift.class, new RenderLift());
        RenderingRegistry.registerEntityRenderingHandler(EntitySteamHole.class, new RenderSteamHole());
    }

    private static class EntityRegister
    {
        private static int localId = 0;

        private final Class<? extends Entity> entityClass;
        private final String name;
        private Object modInstance;
        private final int id;

        private int range = 64;
        private int updateFreq = 20;
        private boolean useVelocity = false;

        public EntityRegister(Class<? extends Entity> entityClass, String name, int id, Object modInstance)
        {
            this.entityClass = entityClass;
            this.name = name;
            this.modInstance = modInstance;
            this.id = id == -1 ? EntityRegistry.findGlobalUniqueEntityId() : id;
        }

        public EntityRegister setMotionParams(int range, int updateFreq, boolean useVelocity)
        {
            this.range = range;
            this.updateFreq = updateFreq;
            this.useVelocity = useVelocity;
            return this;
        }

        public EntityRegister registerToEntityPlacer(int color)
        {
            ItemEntityPlacer.registerEntity(id, color);
            return this;
        }
        public EntityRegister registerToEntityPlacer(int color, ItemEntityPlacer.ISpawnHandler handler)
        {
            ItemEntityPlacer.registerEntity(id, color, handler);
            return this;
        }

        public void register()
        {
            EntityRegistry.registerGlobalEntityID(entityClass, name, id);
            EntityRegistry.registerModEntity(entityClass, name, localId, modInstance, range, updateFreq, useVelocity);
            localId++;
        }
    }
}