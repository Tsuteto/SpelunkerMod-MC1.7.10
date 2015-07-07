package tsuteto.spelunker.block;

import com.google.common.collect.Lists;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.entity.EntityFlashBullet;
import tsuteto.spelunker.entity.EntityStillBat;

import java.util.List;
import java.util.Random;

public class BlockBatSpawner extends BlockInvisible
{
    public static final int MAX_BAT_SPAWN = 2;

    public BlockBatSpawner(Material p_i45394_1_)
    {
        super(p_i45394_1_);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        if (!world.isRemote)
        {
            boolean canSpawn = true;
            List list = world.getEntitiesWithinAABB(EntityFlashBullet.class,
                    AxisAlignedBB.getBoundingBox(x - 10.0D, y - 10.0D, z - 10.0D, x + 10.0D, y + 10.0D, z + 10.0D));
            for (Object o : list)
            {
                Entity e = (Entity)o;
                if (world.rayTraceBlocks(Vec3.createVectorHelper(x + 0.5D, y + 0.5D, z + 0.5D), Vec3.createVectorHelper(e.posX, e.posY, e.posZ)) == null);
                {
                    canSpawn = false;
                    break;
                }
            }
            if (canSpawn)
            {
                // Spawn bats to the max if there's no flash that can be seen
                this.spawnBats(world, x, y, z);
                world.scheduleBlockUpdate(x, y, z, this, 200);
            }
            else
            {
                // Wait 1 sec
                world.scheduleBlockUpdate(x, y, z, this, 20);
            }
        }
    }

    public void spawnBats(World world, int x, int y, int z)
    {
        ChunkCoordinates blockCoord = new ChunkCoordinates(x, y, z);
        int batsAlive = this.getBatsBounded(world, x, y, z).size();

        while (batsAlive < MAX_BAT_SPAWN)
        {
            EntityStillBat bat = new EntityStillBat(world);
            bat.setPosition(x + 0.5D, y + 0.5D, z + 0.5D);
            bat.homePos = blockCoord;
            world.spawnEntityInWorld(bat);
            batsAlive++;
        }
    }

    public void eliminateBats(World world, int x, int y, int z, DamageSource damageSource)
    {
        List<EntityStillBat> bats = this.getBatsBounded(world, x, y, z);
        for (EntityStillBat bat : bats)
        {
            bat.attackEntityFrom(damageSource, bat.getHealth());
        }
    }

    public List<EntityStillBat> getBatsBounded(World world, int x, int y, int z)
    {
        ChunkCoordinates blockCoord = new ChunkCoordinates(x, y, z);
        List<EntityStillBat> list = Lists.newArrayList();
        for (Object o : world.loadedEntityList)
        {
            if (o instanceof EntityStillBat && blockCoord.equals(((EntityStillBat) o).homePos))
            {
                list.add((EntityStillBat)o);
            }
        }
        return list;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack)
    {
        if (!world.isRemote)
        {
            this.spawnBats(world, x, y, z);
        }
    }

    @Override
    public String getItemIconName()
    {
        return SpelunkerMod.resourceDomain + "batSpawner";
    }

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon(SpelunkerMod.resourceDomain + "transparent");
    }
}
