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
import tsuteto.spelunker.entity.render.*;
import tsuteto.spelunker.item.ItemEntityPlacer;

public class SpelunkerEntity
{
    public static void register(SpelunkerMod core, Settings settings)
    {
        EntityRegister.setModInstance(core);

        $(EntityGunBullet.class, "gunBullet", settings.entityGunBulletId)
                .setMotionParams(64, 20, false).register();
        $(EntityBatDroppings.class, "batDroppings", settings.entityBatDroppingsId)
                .setMotionParams(64, 10, true).register();
        $(EntityFlashBullet.class, "flashBullet", settings.entityFlashBulletId)
                .setMotionParams(64, 5, true).register();
        $(EntityFlash.class, "flash", settings.entityFlashId)
                .setMotionParams(64, 5, true).register();
        $(EntitySpelunkerItem.class, "speItem", settings.entitySpelunkerItemId)
                .setMotionParams(64, 5, true).register();
        $(EntityDynamitePrimed.class, "dynamite", settings.entityDynamiteId)
                .setMotionParams(64, 5, true).register();

        $(EntityElevator.class, "elevator", settings.entityElevatorId)
                .setMotionParams(64, 20, true)
                .registerToEntityPlacer(0x00aa22, new ItemEntityPlacer.ISpawnHandler()
                {
                    @Override
                    public Entity spawn(World world, EntityPlayer player, int entityId, double x, double y, double z, int side)
                    {
                        Entity entity = EntityList.createEntityByID(entityId, world);

                        if (entity != null)
                        {
                            entity.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
                            if (player != null)
                            {
                                entity.rotationYaw = (float) (((MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);
                            }

                            if (!world.getCollidingBoundingBoxes(entity, entity.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty())
                            {
                                return null;
                            }
                        }

                        return entity;
                    }
                }).register();

        $(EntityLift.class, "lift", settings.entityLiftId)
                .setMotionParams(64, 20, false).register();

        $(EntitySteamHole.class, "steamHole", settings.entitySteamId)
                .setMotionParams(64, 20, false)
                .registerToEntityPlacer(0x881100, EntitySteamHole.getSpawnHandlerSteamable())
                .registerToEntityPlacer(0x440800, EntitySteamHole.getSpawnHandlerPeddle(), "peddle")
                .register();

        $(EntityFlameHole.class, "flameHole", settings.entityFlameId)
                .setMotionParams(64, 20, false)
                .registerToEntityPlacer(0x888800, EntityFlameHole.getSpawnHandler())
                .register();

        $(EntityStillBat.class, "stillBat", settings.entityStillBatId)
                .setMotionParams(64, 4, true)
                .setEgg(0x111111, 0x555555)
                .register();

        $(EntityGhost.class, "ghost", settings.entityGhostId)
                .setMotionParams(64, 4, true)
                .setEgg(0x09b6ff, 0xd30723)
                .register();

        // EntityFallingFloor uses EntityFallingBlock in vanilla
    }

    private static EntityRegister $(Class<? extends Entity> entityClass, String name, int id)
    {
        return new EntityRegister(entityClass, name, id);
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
        RenderingRegistry.registerEntityRenderingHandler(EntityFlameHole.class, new RenderFlameHole());
        RenderingRegistry.registerEntityRenderingHandler(EntityStillBat.class, new RenderStillBat());
        RenderingRegistry.registerEntityRenderingHandler(EntityDynamitePrimed.class, new RenderDynamitePrimed());
        RenderingRegistry.registerEntityRenderingHandler(EntityGhost.class, new RenderGhost(0.5F));
        // EntityFallingFloor uses RenderFallingBlock in vanilla
    }

    private static class EntityRegister
    {
        private static Object modInstance;

        public static void setModInstance(Object modInstance)
        {
            EntityRegister.modInstance = modInstance;
        }

        private static int localId = 0;

        private final Class<? extends Entity> entityClass;
        private final String name;
        private final int id;

        private int range = 64;
        private int updateFreq = 20;
        private boolean useVelocity = false;

        private boolean shouldAddEgg = false;
        private int color1;
        private int color2;

        public EntityRegister(Class<? extends Entity> entityClass, String name, int id)
        {
            this.entityClass = entityClass;
            this.name = name;
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
            ItemEntityPlacer.registerEntity(id, name, color);
            return this;
        }

        public EntityRegister registerToEntityPlacer(int color, ItemEntityPlacer.ISpawnHandler handler)
        {
            ItemEntityPlacer.registerEntity(id, name, color, handler);
            return this;
        }

        public EntityRegister registerToEntityPlacer(int color, ItemEntityPlacer.ISpawnHandler handler, String name)
        {
            ItemEntityPlacer.registerEntity(id, name, color, handler);
            return this;
        }

        public EntityRegister setEgg(int color1, int color2)
        {
            this.shouldAddEgg = true;
            this.color1 = color1;
            this.color2 = color2;
            return this;
        }

        public void register()
        {
            if (shouldAddEgg)
            {
                EntityRegistry.registerGlobalEntityID(entityClass, name, id, color1, color2);
            }
            else
            {
                EntityRegistry.registerGlobalEntityID(entityClass, name, id);
            }
            EntityRegistry.registerModEntity(entityClass, name, localId, modInstance, range, updateFreq, useVelocity);
            localId++;
        }
    }
}