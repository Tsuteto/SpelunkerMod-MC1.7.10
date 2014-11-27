package tsuteto.spelunker.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import tsuteto.spelunker.util.Coordinate;
import tsuteto.spelunker.util.HelmetUtil;

import java.util.*;
import java.util.Map.Entry;

/**
 * Spelunker's Helmet, originally "Light Helmet" by ayashige
 *
 * @author ayashige, Tsuteto
 *
 */
public class ItemHelmet extends ItemArmor
{
    private int lightRange;
    private int targetLightValue;
    private int headLightValue;

    public ItemHelmet(ArmorMaterial var2, int var3, int var4)
    {
        super(var2, var3, var4);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        return "spelunker:textures/helmet.png";
    }

    public Coordinate[] readFromNBT(EntityLivingBase var1)
    {
        NBTTagCompound var2 = var1.getEntityData();
        Coordinate[] var3 = new Coordinate[2];

        try
        {
            int var4 = var2.getInteger("spelunkerhelmet.LightUp");

            if ((var4 & 2) != 0)
            {
                var3[1] = new Coordinate();
                var3[1].setPosX(var2.getInteger("spelunkerhelmet.prevXTileHead"));
                var3[1].setPosY(var2.getInteger("spelunkerhelmet.prevYTileHead"));
                var3[1].setPosZ(var2.getInteger("spelunkerhelmet.prevZTileHead"));

                if ((var4 & 1) != 0)
                {
                    var3[0] = new Coordinate();
                    var3[0].setPosX(var2.getInteger("spelunkerhelmet.prevXTileTarget"));
                    var3[0].setPosY(var2.getInteger("spelunkerhelmet.prevYTileTarget"));
                    var3[0].setPosZ(var2.getInteger("spelunkerhelmet.prevZTileTarget"));
                }
                else
                {
                    var3[0] = null;
                }

                return var3;
            }
            else
            {
                var3[0] = null;
                var3[1] = null;
                return var3;
            }
        }
        catch (ClassCastException var5)
        {
            var3[0] = null;
            var3[1] = null;
            return var3;
        }
    }

    public void writeToNBT(EntityLivingBase var1, Coordinate[] var2)
    {
        NBTTagCompound var3 = var1.getEntityData();
        int var4 = 0;

        if (var2[0] != null)
        {
            var4 |= 1;
            var3.setInteger("spelunkerhelmet.prevXTileTarget", var2[0].getPosX());
            var3.setInteger("spelunkerhelmet.prevYTileTarget", var2[0].getPosY());
            var3.setInteger("spelunkerhelmet.prevZTileTarget", var2[0].getPosZ());
        }

        if (var2[1] != null)
        {
            var4 |= 2;
            var3.setInteger("spelunkerhelmet.prevXTileHead", var2[1].getPosX());
            var3.setInteger("spelunkerhelmet.prevYTileHead", var2[1].getPosY());
            var3.setInteger("spelunkerhelmet.prevZTileHead", var2[1].getPosZ());
        }

        var3.setInteger("spelunkerhelmet.LightUp", var4);
    }

    public void removeNBT(EntityLivingBase var1)
    {
        NBTTagCompound var2 = var1.getEntityData();
        var2.setInteger("spelunkerhelmet.LightUp", 0);
    }

    public Coordinate getLightTarget(World var1, EntityLivingBase var2, int var3)
    {
        Entity var4 = HelmetUtil.getPointedEntity(var1, var2, var3);
        Coordinate var5 = null;

        if (var4 != null)
        {
            var5 = new Coordinate((int)var4.posX, (int)var4.posY, (int)var4.posZ);
        }
        else
        {
            MovingObjectPosition var6 = HelmetUtil.getPointedBlock(var1, var2, var3);

            if (var6 != null)
            {
                int var7 = var6.blockX;
                int var8 = var6.blockY;
                int var9 = var6.blockZ;

                switch (var6.sideHit)
                {
                    case 0:
                        --var8;
                        break;

                    case 1:
                        ++var8;
                        break;

                    case 2:
                        --var9;
                        break;

                    case 3:
                        ++var9;
                        break;

                    case 4:
                        --var7;
                        break;

                    case 5:
                        ++var7;
                }

                var5 = new Coordinate(var7, var8, var9);
            }
        }

        return var5;
    }

    public void setLightValue(World var1, Coordinate var2, int var3)
    {
        int var4 = var2.getPosX();
        int var5 = var2.getPosY();
        int var6 = var2.getPosZ();
        int var7 = var1.getSavedLightValue(EnumSkyBlock.Block, var4, var5, var6);

        if (var7 < var3 || var3 == 0)
        {
            var1.setLightValue(EnumSkyBlock.Block, var4, var5, var6, var3);
            var1.updateLightByType(EnumSkyBlock.Block, var4 - 1, var5, var6);
            var1.updateLightByType(EnumSkyBlock.Block, var4 + 1, var5, var6);
            var1.updateLightByType(EnumSkyBlock.Block, var4, var5 - 1, var6);
            var1.updateLightByType(EnumSkyBlock.Block, var4, var5 + 1, var6);
            var1.updateLightByType(EnumSkyBlock.Block, var4, var5, var6 - 1);
            var1.updateLightByType(EnumSkyBlock.Block, var4, var5, var6 + 1);
        }
    }

    public void removeLight(World var1, EntityLivingBase var2)
    {
        Coordinate[] var3 = this.readFromNBT(var2);
        boolean var4 = false;

        if (var3[0] != null)
        {
            this.setLightValue(var1, var3[0], 0);
            var4 = true;
        }

        if (var3[1] != null)
        {
            this.setLightValue(var1, var3[1], 0);
            var4 = true;
        }

        if (var4)
        {
            this.removeNBT(var2);
        }
    }

    public boolean lightTarget(World var1, EntityLivingBase var2)
    {
        Coordinate[] var6 = this.readFromNBT(var2);
        Coordinate[] var7 = new Coordinate[] {this.getLightTarget(var1, var2, lightRange), null};
        int var8 = (int)var2.posX;
        int var9 = (int)(var2.posY + var2.getEyeHeight());
        int var10 = (int)var2.posZ;
        var7[1] = new Coordinate(var8, var9, var10);
        HashMap var11 = new HashMap();

        if (var6[0] != null && var6[0] != var7[0])
        {
            var11.put(var6[0], Integer.valueOf(0));
        }

        if (var6[1] != null && var6[1] != var7[1])
        {
            var11.put(var6[1], Integer.valueOf(0));
        }

        if (var7[0] != null && (var6[0] == null || var6[0] != var7[0]))
        {
            var11.put(var7[0], Integer.valueOf(targetLightValue));
        }

        if (var11.containsKey(var7[1]))
        {
            if (((Integer)var11.get(var7[1])).intValue() < headLightValue)
            {
                var11.put(var7[1], Integer.valueOf(headLightValue));
            }
        }
        else if (var6[1] == null || var6[1] != var7[1])
        {
            var11.put(var7[1], Integer.valueOf(headLightValue));
        }

        ArrayList var12 = new ArrayList(var11.entrySet());
        Collections.sort(var12, new Comparator()
        {
            public int compare(Entry var1, Entry var2)
            {
                return ((Integer)var1.getValue()).compareTo((Integer)var2.getValue());
            }
            @Override
            public int compare(Object var1, Object var2)
            {
                return this.compare((Entry)var1, (Entry)var2);
            }
        });
        Iterator var13 = var12.iterator();

        while (var13.hasNext())
        {
            Entry var14 = (Entry)var13.next();
            this.setLightValue(var1, (Coordinate)var14.getKey(), ((Integer)var14.getValue()).intValue());
        }

        this.writeToNBT(var2, var7);
        return true;
    }

    public ItemHelmet setLightRange(int lightRange)
    {
        this.lightRange = lightRange;
        return this;
    }

    public ItemHelmet setTargetLightValue(int targetLightValue)
    {
        this.targetLightValue = targetLightValue;
        return this;
    }

    public ItemHelmet setHeadLightValue(int headLightValue)
    {
        this.headLightValue = headLightValue;
        return this;
    }
}
