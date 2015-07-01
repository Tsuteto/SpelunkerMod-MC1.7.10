package tsuteto.spelunker.world.levelmapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tsuteto.spelunker.block.BlockBatSpawner;
import tsuteto.spelunker.block.BlockBumpy;
import tsuteto.spelunker.block.BlockCheckpoint;
import tsuteto.spelunker.entity.EntityFlameHole;
import tsuteto.spelunker.init.SpelunkerBlocks;
import tsuteto.spelunker.init.SpelunkerItems;
import tsuteto.spelunker.util.Utils;
import tsuteto.spelunker.world.gen.WorldGenSpelunkerLevel;

import java.util.List;
import java.util.Map;

public class MapPieces
{
    public static final Map<Integer, MapPiece> colorMapping = Maps.newHashMap();
    public static final List<MapPieceEntry> mapPieceList = Lists.newArrayList();

    /* === MapPiece Entry === */
    public static final MapPiece startPoint = new MapPieceRespawnPoint("startPoint", 0xff0000)
            .putBlock(1, SpelunkerBlocks.blockStartPoint);
    public static final MapPiece portal = new MapPieceBlock("portal", 0xffbbdd)
            .putBlock(0, 0, 1, SpelunkerBlocks.blockSpelunkerPortal2, 5)
            .putBlock(0, 1, 1, SpelunkerBlocks.blockSpelunkerPortal2, 5 | 8);
    public static final MapPiece respawnPoint = new MapPieceRespawnPoint("respawnPoint", 0x555555)
            .putBlock(1, SpelunkerBlocks.blockRespawnPoint)
            .putBlock(0, SpelunkerBlocks.blockRespawnGate)
            .putBlock(2, SpelunkerBlocks.blockRespawnGate);

    // Terrain
    public static final MapPiece air = new MapPieceBlock("air", 0x000000);
    public static final MapPiece wall = new MapPieceBlock("wall", 0xc84e19)
            .fillBlock(SpelunkerBlocks.blockWall);
    public static final MapPiece platform = new MapPieceBlock("platform", 0xc2c2c2)
            .putBlock(1, SpelunkerBlocks.blockPlatform);
    public static final MapPiece rock = new MapPieceBlock("rock", 0xf08040)
            .fillBlock(SpelunkerBlocks.blockRock);
    public static final MapPiece wallBreakable = new MapPieceBlock("wallBreakable", 0xa13f14)
            .fillBlock(SpelunkerBlocks.blockBreakableWall);
    public static final MapPiece hiddenDiamond = new MapPieceBlock("hiddenDiamond", 0x0080b5)
            .fillBlock(SpelunkerBlocks.blockBreakableWall)
            .putBlock(1, SpelunkerBlocks.blockHiddenDiamond);
    public static final MapPiece fallingFloor = new MapPieceBlock("fallingFloor", 0xe424be)
            .fillBlock(SpelunkerBlocks.blockFallingFloor);
    public static final MapPiece bumpy = new MapPieceBlock("bumpy", 0xc68163)
            .fillBlock(SpelunkerBlocks.blockBumpy, 0, new TileEntryBlock.IHandler()
            {
                @Override
                public void apply(WorldGenSpelunkerLevel gen, World world, int x, int y, int z, Block block, int meta, TileEntity tileEntity)
                {
                    ((BlockBumpy) block).initTileEntity(world, x, y, z);
                }
            });
    public static final MapPiece water = new MapPieceBlock("water", 0x0030ff)
            .fillBlock(Blocks.flowing_water);
    public static final MapPiece waterfall = new MapPieceBlock("waterfall", 0x3060ff)
            .putBlock(3, Blocks.flowing_water);

    // Basic
    public static final MapPiece ladder = new MapPieceBlock("ladder", 0xc8ba19)
            .putBlock(2, SpelunkerBlocks.blockLadder, 2);
    public static final MapPiece liftUp = new MapPieceBlock("liftUp", 0x2dff8c)
            .fillBlock(SpelunkerBlocks.blockWall)
            .putBlock(1, SpelunkerBlocks.blockLiftDispatcher, 0);
    public static final MapPiece liftDown = new MapPieceBlock("liftDown", 0x1c9954)
            .fillBlock(SpelunkerBlocks.blockWall)
            .putBlock(1, SpelunkerBlocks.blockLiftDispatcher, 1);
    public static final MapPiece rope = new MapPieceBlock("rope", 0x29c819)
            .putBlock(1, SpelunkerBlocks.blockRope);
    public static final MapPiece waterLift = new MapPieceWaterLift("waterLift", 0xdfbb3d);
    public static final MapPiece waterLiftPath = new MapPieceBlank("waterLiftPath", 0x9a812b);

    // Rail and Minecart
    public static final MapPiece rail = new MapPieceBlock("rail", 0x19c8c8)
            .putBlock(1, SpelunkerBlocks.blockRailGirder)
            .putBlock(0, 1, 1, SpelunkerBlocks.blockRail);
    public static final MapPiece pillar = new MapPieceBlock("pillar", 0x0f317e)
            .putBlock(1, SpelunkerBlocks.blockRailGirder);
    public static final MapPiece railPlatform = new MapPieceBlock("railPlatform", 0x009c9c)
            .putBlock(1, SpelunkerBlocks.blockRailGirder)
            .putBlock(0, 1, 1, SpelunkerBlocks.blockRail)
            .putBlock(0, SpelunkerBlocks.blockWall)
            .putBlock(2, SpelunkerBlocks.blockWall);
    public static final MapPiece minecart = new MapPieceItem("minecart", 0x80ffff)
            .setItem(2, new ItemStack(SpelunkerItems.itemSpelunkart));
    public static final MapPiece flameHole = new MapPieceEntity("flameHole", 0xffff00)
            .addEntity(1, 0, "flameHole", new TileEntryEntity.IHandler()
            {
                @Override
                public void apply(World world, int offsetX, int offsetY, int offsetZ, Entity entity)
                {
                    ((EntityFlameHole) entity).ticksOffset = (int) (Utils.generateRandomFromCoord(offsetX, offsetY, offsetZ) % 60);
                }
            });

    // Boat
    public static final MapPiece boat = new MapPieceItem("boat", 0x4ca2d8)
            .setItem(2, new ItemStack(SpelunkerItems.itemSpeBoat));

    // Elevator
    public static final MapPiece elevatorRight = new MapPieceEntity("elevatorRight", 0x408000)
            .addEntity(1, 1, "elevator", new TileEntryEntity.IHandler()
            {
                @Override
                public void apply(World world, int offsetX, int offsetY, int offsetZ, Entity entity)
                {
                    entity.rotationYaw = 180.0F;
                }
            });
    public static final MapPiece elevatorLeft = new MapPieceEntity("elevatorLeft", 0x206000)
            .addEntity(1, 1, "elevator", new TileEntryEntity.IHandler()
            {
                @Override
                public void apply(World world, int offsetX, int offsetY, int offsetZ, Entity entity)
                {
                    entity.rotationYaw = 0.0F;
                }
            });

    // Trap
    public static final MapPiece peddleUp = new MapPieceEntity("peddleUp", 0xffb137)
            .addEntity(0, 1, "peddle")
            .addEntity(1, 1, "peddle")
            .addEntity(2, 1, "peddle");
    public static final MapPiece peddleDown = new MapPieceEntity("peddleDown", 0xe09117)
            .addEntity(0, 0, "peddle")
            .addEntity(1, 0, "peddle")
            .addEntity(2, 0, "peddle");
    public static final MapPiece peddleRight = new MapPieceEntity("peddleRight", 0xffd157)
            .addEntity(0, 4, "peddle")
            .addEntity(1, 4, "peddle")
            .addEntity(2, 4, "peddle");
    public static final MapPiece peddleLeft = new MapPieceEntity("peddleLeft", 0xfff177)
            .addEntity(0, 5, "peddle")
            .addEntity(1, 5, "peddle")
            .addEntity(2, 5, "peddle");
    public static final MapPiece steamHoleUp = new MapPieceEntity("steamHoleUp", 0xc0862a)
            .addEntity(0, 1, "steamHole")
            .addEntity(1, 1, "steamHole")
            .addEntity(2, 1, "steamHole");
    public static final MapPiece steamHoleDown = new MapPieceEntity("steamHoleDown", 0xa0660a)
            .addEntity(0, 0, "steamHole")
            .addEntity(1, 0, "steamHole")
            .addEntity(2, 0, "steamHole");
    public static final MapPiece steamHoleRight = new MapPieceEntity("steamHoleRight", 0xe0a64a)
            .addEntity(0, 4, "steamHole")
            .addEntity(1, 4, "steamHole")
            .addEntity(2, 4, "steamHole");
    public static final MapPiece steamHoleLeft = new MapPieceEntity("steamHoleLeft", 0xffc66a)
            .addEntity(0, 5, "steamHole")
            .addEntity(1, 5, "steamHole")
            .addEntity(2, 5, "steamHole");
    public static final MapPiece batSpawner = new MapPieceBlock("batSpawner", 0xaa00ff)
            .putBlock(1, SpelunkerBlocks.blockBatSpawner, new TileEntryBlock.IHandler()
            {
                @Override
                public void apply(WorldGenSpelunkerLevel gen, World world, int x, int y, int z, Block block, int meta, TileEntity tileEntity)
                {
                    ((BlockBatSpawner) block).spawnBats(world, x, y, z);
                }
            });

    // Gate
    public static final MapPiece gateBlue = new MapPieceLockedGate("gateBlue", 0x0000c0, 5, 11);
    public static final MapPiece gateRed = new MapPieceLockedGate("gateRed", 0xc00000, 5, 14);

    // Item
    public static final MapPiece energy = new MapPieceItem("energy", 0xa0ff80)
            .setItem(new ItemStack(SpelunkerItems.itemEnergy));
    public static final MapPiece dynamite = new MapPieceItem("dynamite", 0x808090)
            .setItem(new ItemStack(SpelunkerItems.itemDynamiteDrop));
    public static final MapPiece flash = new MapPieceItem("flash", 0x08b6ff)
            .setItem(new ItemStack(SpelunkerItems.itemFlashDrop));
    public static final MapPiece coin = new MapPieceItem("coin", 0xffff80)
            .setItem(new ItemStack(SpelunkerItems.itemCoin));
    public static final MapPiece dollar = new MapPieceItem("dollar", 0xe6e6e6)
            .setItem(new ItemStack(SpelunkerItems.itemDollar));
    public static final MapPiece miracle = new MapPieceItem("miracle", 0x8dffff)
            .setItem(new ItemStack(SpelunkerItems.itemMiracle));
    public static final MapPiece gateKeyBlue = new MapPieceItem("gateKeyBlue", 0x0000e0)
            .setItem(new ItemStack(SpelunkerItems.itemGateKey, 1, 11));
    public static final MapPiece gateKeyRed = new MapPieceItem("gateKeyRed", 0xe00000)
            .setItem(new ItemStack(SpelunkerItems.itemGateKey, 1, 14));

    // Hidden Item
    public static final MapPiece itemSpawnPoint = new MapPieceItemSpawnPoint("itemSpawnPoint", 0x336650);
    public static final MapPiece hiddenItemBeforePyramid = new MapPieceHiddenItemPyramid("hiddenItemPyramid", 0x7d5a20);
    public static final MapPiece hiddenItem = new MapPieceItemHidden("hiddenItem", 0x663040);

    // Check point
    public static final MapPiece checkpoint = new MapPieceSingle("checkpoint", 0x00ff00)
    {
        @Override
        public void place(WorldGenSpelunkerLevel gen, World world, int x, int y, int z, SpelunkerLevelMapper mapper, int mapX, int mapY)
        {
            for (int i = 0; i < 3; i++)
            {
                gen.setBlockAndNotifyAdequately(
                        world, x, y, z + i, SpelunkerBlocks.blockCheckpoint, 0);
                gen.setBlockAndNotifyAdequately(
                        world, x, y + 1, z + i, SpelunkerBlocks.blockCheckpoint, 0);

                BlockCheckpoint.setCheckpointNo(world, x, y, z + i, gen.checkpointCounter);
                BlockCheckpoint.setCheckpointNo(world, x, y + 1, z + i, gen.checkpointCounter);
            }
            gen.checkpointCounter++;
        }
    };

    public static final MapPiece checkpointStatue = new MapPieceBlock("cpStatue", 0x4fb100)
            .putBlock( 0, 4, 3, SpelunkerBlocks.blockCheckpointStatue1, 0)
            .putBlock(-1, 4, 3, SpelunkerBlocks.blockCheckpointStatue1, 1)
            .putBlock(-2, 4, 3, SpelunkerBlocks.blockCheckpointStatue1, 2)
            .putBlock(-3, 4, 3, SpelunkerBlocks.blockCheckpointStatue1, 3)
            .putBlock( 0, 3, 3, SpelunkerBlocks.blockCheckpointStatue1, 4)
            .putBlock(-1, 3, 3, SpelunkerBlocks.blockCheckpointStatue1, 5)
            .putBlock(-2, 3, 3, SpelunkerBlocks.blockCheckpointStatue1, 6)
            .putBlock(-3, 3, 3, SpelunkerBlocks.blockCheckpointStatue1, 7)
            .putBlock( 0, 2, 3, SpelunkerBlocks.blockCheckpointStatue1, 8)
            .putBlock(-1, 2, 3, SpelunkerBlocks.blockCheckpointStatue1, 9)
            .putBlock(-2, 2, 3, SpelunkerBlocks.blockCheckpointStatue1, 10)
            .putBlock(-3, 2, 3, SpelunkerBlocks.blockCheckpointStatue1, 11)
            .putBlock( 0, 1, 3, SpelunkerBlocks.blockCheckpointStatue1, 12)
            .putBlock(-1, 1, 3, SpelunkerBlocks.blockCheckpointStatue1, 13)
            .putBlock(-2, 1, 3, SpelunkerBlocks.blockCheckpointStatue1, 14)
            .putBlock(-3, 1, 3, SpelunkerBlocks.blockCheckpointStatue1, 15)
            .putBlock( 0, 0, 3, SpelunkerBlocks.blockCheckpointStatue2, 0)
            .putBlock(-1, 0, 3, SpelunkerBlocks.blockCheckpointStatue2, 1)
            .putBlock(-2, 0, 3, SpelunkerBlocks.blockCheckpointStatue2, 2)
            .putBlock(-3, 0, 3, SpelunkerBlocks.blockCheckpointStatue2, 3);

    // Pyramid & goal
    public static final MapPiece pyramid1 = new MapPieceBlock("pyramid1", 0x958500)
            .putBlock(2, SpelunkerBlocks.blockPyramid)
            .putBlock(3, Blocks.air)
            .putBlock(4, Blocks.air)
            .putBlock(5, Blocks.air)
            .putBlock(6, SpelunkerBlocks.blockPyramid);

    public static final MapPiece pyramid1Wall = new MapPieceBlock("pyramid1w", 0x956d00)
            .putBlock(2, SpelunkerBlocks.blockPyramid)
            .putBlock(3, SpelunkerBlocks.blockPyramid)
            .putBlock(4, SpelunkerBlocks.blockPyramid)
            .putBlock(5, SpelunkerBlocks.blockPyramid)
            .putBlock(6, SpelunkerBlocks.blockPyramid);

    public static final MapPiece pyramid1Ent = new MapPieceBlock("pyramid1e", 0x8400af)
            .putBlock(2, Blocks.air)
            .putBlock(3, Blocks.air)
            .putBlock(4, Blocks.air)
            .putBlock(5, Blocks.air)
            .putBlock(6, SpelunkerBlocks.blockPyramid);

    public static final MapPiece pyramid2 = new MapPieceBlock("pyramid2", 0xb7a400)
            .putBlock(3, SpelunkerBlocks.blockPyramid)
            .putBlock(4, Blocks.air)
            .putBlock(5, Blocks.air)
            .putBlock(6, SpelunkerBlocks.blockPyramid);

    public static final MapPiece pyramid2Wall = new MapPieceBlock("pyramid2w", 0xb78500)
            .putBlock(3, SpelunkerBlocks.blockPyramid)
            .putBlock(4, SpelunkerBlocks.blockPyramid)
            .putBlock(5, SpelunkerBlocks.blockPyramid)
            .putBlock(6, SpelunkerBlocks.blockPyramid);

    public static final MapPiece pyramid2Ent = new MapPieceBlock("pyramid2e", 0xa000d4)
            .putBlock(3, Blocks.air)
            .putBlock(4, Blocks.air)
            .putBlock(5, Blocks.air)
            .putBlock(6, SpelunkerBlocks.blockPyramid);

    public static final MapPiece pyramid2Goal = new MapPieceBlock("pyramid2g", 0xef0098)
            .putBlock(3, SpelunkerBlocks.blockCheckpoint, 1)
            .putBlock(4, Blocks.air)
            .putBlock(0, 0, 5, SpelunkerBlocks.blockSpelunkerPortal2, 3)
            .putBlock(0, 1, 5, SpelunkerBlocks.blockSpelunkerPortal2, 3 | 8)
            .putBlock(6, SpelunkerBlocks.blockPyramid);

    public static final MapPiece pyramid3 = new MapPieceBlock("pyramid3", 0xdac300)
            .putBlock(3, Blocks.air)
            .putBlock(4, SpelunkerBlocks.blockPyramid)
            .putBlock(5, SpelunkerBlocks.blockPyramid)
            .putBlock(6, SpelunkerBlocks.blockPyramid);

    public static final MapPiece pyramidRoom = new MapPieceBlock("pyramidRoom", 0x2b0fa1)
            .putBlock(3, Blocks.air)
            .putBlock(4, Blocks.air)
            .putBlock(5, Blocks.air);


    public static void registerMapPiece(int color, MapPiece mapPiece)
    {
        if (colorMapping.containsKey(color))
        {
            throw new IllegalArgumentException(
                    String.format("MapPiece color duplicated: %06x, %s", color, mapPiece.getName(color)));
        }
        colorMapping.put(color, mapPiece);
        mapPieceList.add(new MapPieceEntry(color, mapPiece));
    }

    public static class MapPieceEntry
    {
        public final int color;
        public final MapPiece mapPiece;

        public MapPieceEntry(int color, MapPiece mapPiece)
        {
            this.color = color;
            this.mapPiece = mapPiece;
        }
    }
}
