package tsuteto.spelunker.dimension;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Direction;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import tsuteto.spelunker.init.SpelunkerBlocks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class SpelunkerTeleporter extends Teleporter
{
    public static final Block portalBlock = SpelunkerBlocks.blockStartPoint;

    public boolean isSucceeded;

    private final WorldServer worldServerInstance;

    /** A private Random() function in Teleporter */
    private final Random random;

    private final LongHashMap destinationCoordinateCache = new LongHashMap();

    private final List destinationCoordinateKeys = new ArrayList();

    public SpelunkerTeleporter(WorldServer world)
    {
        super(world);
        this.worldServerInstance = world;
        this.random = new Random();
    }

    /**
     * Place an entity in a nearby portal, creating one if necessary.
     */
    @Override
    public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8)
    {
        if (this.worldServerInstance.provider.dimensionId != 0)
        {
            this.isSucceeded = this.placeInExistingPortal(par1Entity, par2, par4, par6, par8);
        }
        else
        {
            this.locateOverworldSpawnPoint(par1Entity);
        }
    }

    public void locateOverworldSpawnPoint(Entity entity)
    {
        World world = this.worldServerInstance;
        byte byte0 = 16;
        double d = -1D;
        int i;
        int j;
        int k;
        if (entity instanceof EntityPlayer)
        {
            ChunkCoordinates coord = ((EntityPlayer) entity).getBedLocation(0);
            if (coord == null) coord = world.getSpawnPoint();
            i = coord.posX;
            j = coord.posY;
            k = coord.posZ;
        }
        else
        {
            i = MathHelper.floor_double(entity.posX);
            j = MathHelper.floor_double(entity.posY);
            k = MathHelper.floor_double(entity.posZ);
        }
        int l = i;
        int i1 = j;
        int j1 = k;
        int k1 = 0;
        for(int i2 = i - byte0; i2 <= i + byte0; i2++)
        {
            double d1 = (i2 + 0.5D) - i;
            for(int j3 = k - byte0; j3 <= k + byte0; j3++)
            {
                double d3 = (j3 + 0.5D) - k;
                for(int k4 = 127; k4 >= 0; k4--)
                {
                    if(!world.isAirBlock(i2, k4, j3))
                    {
                        continue;
                    }
                    for(; k4 > 0 && world.isAirBlock(i2, k4 - 1, j3); k4--) { }
label0:
                    for(int k5 = 0; k5 < 4; k5++)
                    {
                        int l6 = k5 % 2;
                        int i8 = 1 - l6;
                        if(k5 % 4 >= 2)
                        {
                            l6 = -l6;
                            i8 = -i8;
                        }
                        for(int j9 = 0; j9 < 3; j9++)
                        {
                            for(int k10 = 0; k10 < 4; k10++)
                            {
                                for(int l11 = -1; l11 < 4; l11++)
                                {
                                    int j12 = i2 + (k10 - 1) * l6 + j9 * i8;
                                    int l12 = k4 + l11;
                                    int j13 = (j3 + (k10 - 1) * i8) - j9 * l6;
                                    if(l11 < 0 && !world.getBlock(j12, l12, j13).getMaterial().isSolid() || l11 >= 0 && !world.isAirBlock(j12, l12, j13))
                                    {
                                        break label0;
                                    }
                                }

                            }

                        }

                        double d5 = (k4 + 0.5D) - j;
                        double d7 = d1 * d1 + d5 * d5 + d3 * d3;
                        if(d < 0.0D || d7 < d)
                        {
                            d = d7;
                            l = i2;
                            i1 = k4;
                            j1 = j3;
                            k1 = k5 % 4;
                        }
                    }

                }

            }

        }

        if(d < 0.0D)
        {
            for(int j2 = i - byte0; j2 <= i + byte0; j2++)
            {
                double d2 = (j2 + 0.5D) - entity.posX;
                for(int k3 = k - byte0; k3 <= k + byte0; k3++)
                {
                    double d4 = (k3 + 0.5D) - entity.posZ;
                    for(int l4 = 127; l4 >= 0; l4--)
                    {
                        if(!world.isAirBlock(j2, l4, k3))
                        {
                            continue;
                        }
                        for(; l4 > 0 && world.isAirBlock(j2, l4 - 1, k3); l4--) { }
label1:
                        for(int l5 = 0; l5 < 4; l5++)
                        {
                            int i7 = l5 % 2;
                            int j8 = 1 - i7;
                            for(int k9 = 0; k9 < 4; k9++)
                            {
                                for(int l10 = -1; l10 < 4; l10++)
                                {
                                    int i12 = j2 + (k9 - 1) * i7;
                                    int k12 = l4 + l10;
                                    int i13 = k3 + (k9 - 1) * j8;
                                    if(l10 < 0 && !world.getBlock(i12, k12, i13).getMaterial().isSolid() || l10 >= 0 && !world.isAirBlock(i12, k12, i13))
                                    {
                                        break label1;
                                    }
                                }

                            }

                            double d6 = (l4 + 0.5D) - entity.posY;
                            double d8 = d2 * d2 + d6 * d6 + d4 * d4;
                            if(d < 0.0D || d8 < d)
                            {
                                d = d8;
                                l = j2;
                                i1 = l4;
                                j1 = k3;
                                k1 = l5 % 2;
                            }
                        }

                    }

                }

            }

        }
        int k2 = k1;
        int l2 = l;
        int i3 = i1;
        int l3 = j1;
        int i4 = k2 % 2;
        int j4 = 1 - i4;
        if(k2 % 4 >= 2)
        {
            i4 = -i4;
            j4 = -j4;
        }
        if(d < 0.0D)
        {
            if(i1 < 70)
            {
                i1 = 70;
            }
            if(i1 > 118)
            {
                i1 = 118;
            }
            i3 = i1;
            for(int i5 = -1; i5 <= 1; i5++)
            {
                for(int i6 = 0; i6 < 3; i6++)
                {
                    for(int j7 = -1; j7 < 3; j7++)
                    {
                        int k8 = l2 + (i6 - 1) * i4 + i5 * j4;
                        int l9 = i3 + j7;
                        int i11 = (l3 + (i6 - 1) * j4) - i5 * i4;
                        if (!world.isAirBlock(k8, l9, i11))
                        {
                            world.setBlock(k8, l9, i11, Blocks.air, 0, 2);
                        }
                    }

                }
            }

        }

        entity.motionX = entity.motionY = entity.motionZ = 0.0D;
        entity.setLocationAndAngles(l2 + i4 + 0.5D, i3, l3 + j4 + 0.5D, entity.rotationYaw, entity.rotationPitch);
    }

    /**
     * Place an entity in a nearby portal register already exists.
     */
    @Override
    public boolean placeInExistingPortal(Entity par1Entity, double x, double y, double z, float par8)
    {
        par1Entity.setLocationAndAngles(0, 128, 0, par1Entity.rotationYaw, par1Entity.rotationPitch);

        short short1 = 256;
        double d3 = -1.0D;
        int i = 0;
        int j = 0;
        int k = 0;
        long j1 = ChunkCoordIntPair.chunkXZ2Int(0, 0);
        boolean flag = true;
        double d4;
        int k1;

        if (this.destinationCoordinateCache.containsItem(j1))
        {
            PortalPosition portalposition = (PortalPosition)this.destinationCoordinateCache.getValueByKey(j1);
            d3 = 0.0D;
            i = portalposition.posX;
            j = portalposition.posY;
            k = portalposition.posZ;
            portalposition.lastUpdateTime = this.worldServerInstance.getTotalWorldTime();
            flag = false;
        }
        else
        {
            for (k1 = 0; k1 <= short1; ++k1)
            {
                double d5 = k1 + 0.5D;

                for (int l1 = 0; l1 <= short1; ++l1)
                {
                    double d6 = l1 + 0.5D;

                    for (int i2 = this.worldServerInstance.getActualHeight() - 1; i2 >= 0; --i2)
                    {
                        if (this.worldServerInstance.getBlock(k1, i2, l1) == portalBlock)
                        {
                            while (this.worldServerInstance.getBlock(k1, i2 - 1, l1) == portalBlock)
                            {
                                --i2;
                            }

                            d4 = i2 + 0.5D - par1Entity.posY;
                            double d7 = d5 * d5 + d4 * d4 + d6 * d6;

                            if (d3 < 0.0D || d7 < d3)
                            {
                                d3 = d7;
                                i = k1;
                                j = i2;
                                k = l1;
                            }
                        }
                    }
                }
            }
        }

        if (d3 >= 0.0D)
        {
            if (flag)
            {
                this.destinationCoordinateCache.add(j1, new PortalPosition(i, j, k, this.worldServerInstance.getTotalWorldTime()));
                this.destinationCoordinateKeys.add(Long.valueOf(j1));
            }

            double d8 = i + 0.5D;
            double d9 = j + 0.5D;
            d4 = k + 0.5D;
            int j2 = -1;

            if (this.worldServerInstance.getBlock(i - 1, j, k) == portalBlock)
            {
                j2 = 2;
            }

            if (this.worldServerInstance.getBlock(i + 1, j, k) == portalBlock)
            {
                j2 = 0;
            }

            if (this.worldServerInstance.getBlock(i, j, k - 1) == portalBlock)
            {
                j2 = 3;
            }

            if (this.worldServerInstance.getBlock(i, j, k + 1) == portalBlock)
            {
                j2 = 1;
            }

            int k2 = par1Entity.getTeleportDirection();

            if (j2 > -1)
            {
                int l2 = Direction.rotateLeft[j2];
                int i3 = Direction.offsetX[j2];
                int j3 = Direction.offsetZ[j2];
                int k3 = Direction.offsetX[l2];
                int l3 = Direction.offsetZ[l2];
                boolean flag1 = !this.worldServerInstance.isAirBlock(i + i3 + k3, j, k + j3 + l3) || !this.worldServerInstance.isAirBlock(i + i3 + k3, j + 1, k + j3 + l3);
                boolean flag2 = !this.worldServerInstance.isAirBlock(i + i3, j, k + j3) || !this.worldServerInstance.isAirBlock(i + i3, j + 1, k + j3);

                if (flag1 && flag2)
                {
                    j2 = Direction.rotateOpposite[j2];
                    l2 = Direction.rotateOpposite[l2];
                    i3 = Direction.offsetX[j2];
                    j3 = Direction.offsetZ[j2];
                    k3 = Direction.offsetX[l2];
                    l3 = Direction.offsetZ[l2];
                    k1 = i - k3;
                    d8 -= k3;
                    int i4 = k - l3;
                    d4 -= l3;
                    flag1 = !this.worldServerInstance.isAirBlock(k1 + i3 + k3, j, i4 + j3 + l3) || !this.worldServerInstance.isAirBlock(k1 + i3 + k3, j + 1, i4 + j3 + l3);
                    flag2 = !this.worldServerInstance.isAirBlock(k1 + i3, j, i4 + j3) || !this.worldServerInstance.isAirBlock(k1 + i3, j + 1, i4 + j3);
                }

                float f1 = 0.5F;
                float f2 = 0.5F;

                if (!flag1 && flag2)
                {
                    f1 = 1.0F;
                }
                else if (flag1 && !flag2)
                {
                    f1 = 0.0F;
                }
                else if (flag1 && flag2)
                {
                    f2 = 0.0F;
                }

                d8 += (k3 * f1 + f2 * i3);
                d4 += (l3 * f1 + f2 * j3);
                float f3 = 0.0F;
                float f4 = 0.0F;
                float f5 = 0.0F;
                float f6 = 0.0F;

                if (j2 == k2)
                {
                    f3 = 1.0F;
                    f4 = 1.0F;
                }
                else if (j2 == Direction.rotateOpposite[k2])
                {
                    f3 = -1.0F;
                    f4 = -1.0F;
                }
                else if (j2 == Direction.rotateRight[k2])
                {
                    f5 = 1.0F;
                    f6 = -1.0F;
                }
                else
                {
                    f5 = -1.0F;
                    f6 = 1.0F;
                }

                double d10 = par1Entity.motionX;
                double d11 = par1Entity.motionZ;
                par1Entity.motionX = d10 * f3 + d11 * f6;
                par1Entity.motionZ = d10 * f5 + d11 * f4;
                par1Entity.rotationYaw = par8 - (k2 * 90) + (j2 * 90);
            }
            else
            {
                par1Entity.motionX = par1Entity.motionY = par1Entity.motionZ = 0.0D;
            }

            par1Entity.setLocationAndAngles(d8, d9, d4, par1Entity.rotationYaw, par1Entity.rotationPitch);
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean makePortal(Entity par1Entity)
    {
        return true;
    }

    /**
     * called periodically to remove out-of-date portal locations from the cache list. Argument par1 is a
     * WorldServer.getTotalWorldTime() value.
     */
    @Override
    public void removeStalePortalLocations(long par1)
    {
        if (par1 % 100L == 0L)
        {
            Iterator iterator = this.destinationCoordinateKeys.iterator();
            long j = par1 - 600L;

            while (iterator.hasNext())
            {
                Long olong = (Long)iterator.next();
                PortalPosition portalposition = (PortalPosition)this.destinationCoordinateCache.getValueByKey(olong.longValue());

                if (portalposition == null || portalposition.lastUpdateTime < j)
                {
                    iterator.remove();
                    this.destinationCoordinateCache.remove(olong.longValue());
                }
            }
        }
    }
}
