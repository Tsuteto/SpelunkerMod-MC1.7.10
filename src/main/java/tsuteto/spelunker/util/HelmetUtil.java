package tsuteto.spelunker.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

/**
 * Light Helmet utilities originally by ayashige
 *
 * @author ayashige, Tsuteto
 *
 */
public class HelmetUtil
{
    public static void log(String var0)
    {
        ModLog.debug("util: \"" + var0 + "\"");
    }

    public static MovingObjectPosition getPointedBlock(World var0, EntityLivingBase var1, double var2)
    {
        Object var4 = null;
        Vec3 var7 = Vec3.createVectorHelper(var1.posX, var1.posY, var1.posZ);
        Vec3 var8 = var1.getLook(1.0F);
        Vec3 var9 = var7.addVector(var8.xCoord * var2, var8.yCoord * var2, var8.zCoord * var2);
        MovingObjectPosition var10 = var0.func_147447_a(var7, var9, false, true, false);
        return var10 == null ? null : var10;
    }

    public static Entity getPointedEntity(World var0, EntityLivingBase var1, double var2)
    {
        Entity var4 = null;
        Vec3 var7 = Vec3.createVectorHelper(var1.posX, var1.posY, var1.posZ);
        Vec3 var8 = var1.getLook(1.0F);
        Vec3 var9 = var7.addVector(var8.xCoord * var2, var8.yCoord * var2, var8.zCoord * var2);
        float var10 = 1.0F;
        List var11 = var0.getEntitiesWithinAABBExcludingEntity(var1, var1.boundingBox.addCoord(var8.xCoord * var2, var8.yCoord * var2, var8.zCoord * var2).expand(var10, var10, var10));
        double var12 = 0.0D;

        for (int var14 = 0; var14 < var11.size(); ++var14)
        {
            Entity var15 = (Entity)var11.get(var14);

            if (var15.canBeCollidedWith())
            {
                float var16 = var15.getCollisionBorderSize();
                AxisAlignedBB var17 = var15.boundingBox.expand(var16, var16, var16);
                MovingObjectPosition var18 = var17.calculateIntercept(var7, var9);

                if (var17.isVecInside(var7))
                {
                    if (0.0D < var12 || var12 == 0.0D)
                    {
                        var4 = var15;
                        var12 = 0.0D;
                    }
                }
                else if (var18 != null)
                {
                    double var19 = var7.distanceTo(var18.hitVec);

                    if (var19 < var12 || var12 == 0.0D)
                    {
                        var4 = var15;
                        var12 = var19;
                    }
                }
            }
        }

        return var4;
    }
}
