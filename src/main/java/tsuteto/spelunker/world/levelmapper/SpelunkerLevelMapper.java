package tsuteto.spelunker.world.levelmapper;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tsuteto.spelunker.block.BlockBatSpawner;
import tsuteto.spelunker.block.BlockBumpy;
import tsuteto.spelunker.block.SpelunkerBlocks;
import tsuteto.spelunker.entity.EntityFlameHole;
import tsuteto.spelunker.item.SpelunkerItem;
import tsuteto.spelunker.util.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class SpelunkerLevelMapper
{
    private static final SpelunkerLevelMapper instance = new SpelunkerLevelMapper();

    /* === MapPiece Entry === */
    public static final MapPiece startPoint = new MapPieceRespawnPoint(0xff0000)
            .putBlock(1, SpelunkerBlocks.blockStartPoint)
            ;

    public static final MapPiece portal = new MapPieceBlock(0xffbbdd)
            .putBlock(1, SpelunkerBlocks.blockSpelunkerPortal, 5)
            ;

    public static final MapPiece respawnPoint = new MapPieceRespawnPoint(0x555555)
            .putBlock(1, SpelunkerBlocks.blockRespawnPoint)
            .putBlock(0, SpelunkerBlocks.blockRespawnGate)
            .putBlock(2, SpelunkerBlocks.blockRespawnGate)
            ;

    // Terrain
    public static final MapPiece air = new MapPieceBlock(0x000000);

    public static final MapPiece wall = new MapPieceBlock(0xc84e19)
            .fillBlock(SpelunkerBlocks.blockWall);

    public static final MapPiece platform = new MapPieceBlock(0xc2c2c2)
            .putBlock(1, SpelunkerBlocks.blockPlatform);

    // Basic
    public static final MapPiece ladder = new MapPieceBlock(0xc8ba19)
            .putBlock(2, Blocks.ladder, 2);

    public static final MapPiece liftUp = new MapPieceBlock(0x2dff8c)
            .fillBlock(SpelunkerBlocks.blockWall)
            .putBlock(1, SpelunkerBlocks.blockLiftDispatcher, 0);
    public static final MapPiece liftDown = new MapPieceBlock(0x1c9954)
            .fillBlock(SpelunkerBlocks.blockWall)
            .putBlock(1, SpelunkerBlocks.blockLiftDispatcher, 1);

    public static final MapPiece rope = new MapPieceBlock(0x29c819)
            .putBlock(1, SpelunkerBlocks.blockRope);

    public static final MapPiece bumpy = new MapPieceBlock(0xc68163)
            .fillBlock(SpelunkerBlocks.blockBumpy, 0, new TileEntryBlock.IHandler()
            {
                @Override
                public void apply(World world, int x, int y, int z, Block block, int meta, TileEntity tileEntity)
                {
                    ((BlockBumpy) block).initTileEntity(world, x, y, z);
                }
            });

    public static final MapPiece fallingFloor = new MapPieceBlock(0xe424be)
            .fillBlock(SpelunkerBlocks.blockFallingFloor);

    public static final MapPiece rock = new MapPieceBlock(0xf08040)
            .fillBlock(SpelunkerBlocks.blockRock);

    public static final MapPiece wallBreakable = new MapPieceBlock(0xa13f14)
            .fillBlock(SpelunkerBlocks.blockBreakableWall);

    public static final MapPiece hiddenDiamond = new MapPieceBlock(0x0080b5)
            .fillBlock(SpelunkerBlocks.blockBreakableWall)
            .putBlock(1, SpelunkerBlocks.blockHiddenDiamond);

    // Rail and Minecart
    public static final MapPiece rail = new MapPieceBlock(0x19c8c8)
            .putBlock(1, SpelunkerBlocks.blockRailGirder)
            .putBlock(0, 1, 1, SpelunkerBlocks.blockRail);

    public static final MapPiece railPlatform = new MapPieceBlock(0x009c9c)
            .putBlock(1, SpelunkerBlocks.blockRailGirder)
            .putBlock(0, 1, 1, SpelunkerBlocks.blockRail)
            .putBlock(0, SpelunkerBlocks.blockWall)
            .putBlock(2, SpelunkerBlocks.blockWall);

    public static final MapPiece minecart = new MapPieceItem(0x80ffff)
            .setItem(2, new ItemStack(Items.minecart));

    public static final MapPiece flameHole = new MapPieceEntity(0xffff00)
            .addEntity(1, 0, "flameHole", new TileEntryEntity.IHandler()
            {
                @Override
                public void apply(World world, int offsetX, int offsetY, int offsetZ, Entity entity)
                {
                    ((EntityFlameHole)entity).ticksOffset = (int)(Utils.generateRandomFromCoord(offsetX, offsetY, offsetZ) % 60);
                }
            });

    // Elevator
    public static final MapPiece elevatorRight = new MapPieceEntity(0x408000)
            .addEntity(1, 1, "elevator", new TileEntryEntity.IHandler()
            {
                @Override
                public void apply(World world, int offsetX, int offsetY, int offsetZ, Entity entity)
                {
                    entity.rotationYaw = 180.0F;
                }
            });

    public static final MapPiece elevatorLeft = new MapPieceEntity(0x206000)
            .addEntity(1, 1, "elevator", new TileEntryEntity.IHandler()
            {
                @Override
                public void apply(World world, int offsetX, int offsetY, int offsetZ, Entity entity)
                {
                    entity.rotationYaw = 0.0F;
                }
            });

    // Trap
    public static final MapPiece peddleUp = new MapPieceEntity(0xffb137)
            .addEntity(0, 1, "peddle")
            .addEntity(1, 1, "peddle")
            .addEntity(2, 1, "peddle");
    public static final MapPiece peddleDown = new MapPieceEntity(0xe09117)
            .addEntity(0, 0, "peddle")
            .addEntity(1, 0, "peddle")
            .addEntity(2, 0, "peddle");
    public static final MapPiece peddleRight = new MapPieceEntity(0xffd157)
            .addEntity(0, 4, "peddle")
            .addEntity(1, 4, "peddle")
            .addEntity(2, 4, "peddle");
    public static final MapPiece peddleLeft = new MapPieceEntity(0xfff177)
            .addEntity(0, 5, "peddle")
            .addEntity(1, 5, "peddle")
            .addEntity(2, 5, "peddle");

    public static final MapPiece steamHoleUp = new MapPieceEntity(0xc0862a)
            .addEntity(0, 1, "steamHole")
            .addEntity(1, 1, "steamHole")
            .addEntity(2, 1, "steamHole");
    public static final MapPiece steamHoleDown = new MapPieceEntity(0xa0660a)
            .addEntity(0, 0, "steamHole")
            .addEntity(1, 0, "steamHole")
            .addEntity(2, 0, "steamHole");
    public static final MapPiece steamHoleRight = new MapPieceEntity(0xe0a64a)
            .addEntity(0, 4, "steamHole")
            .addEntity(1, 4, "steamHole")
            .addEntity(2, 4, "steamHole");
    public static final MapPiece steamHoleLeft = new MapPieceEntity(0xffc66a)
            .addEntity(0, 5, "steamHole")
            .addEntity(1, 5, "steamHole")
            .addEntity(2, 5, "steamHole");

    public static final MapPiece batSpawner = new MapPieceBlock(0xaa00ff)
            .putBlock(1, SpelunkerBlocks.blockBatSpawner, new TileEntryBlock.IHandler()
            {
                @Override
                public void apply(World world, int x, int y, int z, Block block, int meta, TileEntity tileEntity)
                {
                    ((BlockBatSpawner) block).spawnBats(world, x, y, z);
                }
            });

    // Gate
    public static final MapPiece gateBlue = new MapPieceLockedGate(0x0000c0, 5, 11);
    public static final MapPiece gateRed = new MapPieceLockedGate(0xc00000, 5, 14);

    // Item
    public static final MapPiece energy = new MapPieceItem(0xa0ff80)
            .setItem(new ItemStack(SpelunkerItem.itemEnergy));
    public static final MapPiece dynamite = new MapPieceItem(0x808090)
            .setItem(new ItemStack(SpelunkerItem.itemDynamiteDrop));
    public static final MapPiece flash = new MapPieceItem(0x08b6ff)
            .setItem(new ItemStack(SpelunkerItem.itemFlashDrop));
    public static final MapPiece coin = new MapPieceItem(0xffff80)
            .setItem(new ItemStack(SpelunkerItem.itemCoin));
    public static final MapPiece dollar = new MapPieceItem(0xe6e6e6)
            .setItem(new ItemStack(SpelunkerItem.itemDollar));
    public static final MapPiece miracle = new MapPieceItem(0x8dffff)
            .setItem(new ItemStack(SpelunkerItem.itemMiracle));

    public static final MapPiece gateKeyBlue = new MapPieceItem(0x0000e0)
            .setItem(new ItemStack(SpelunkerItem.itemGateKey, 1, 11));
    public static final MapPiece gateKeyRed = new MapPieceItem(0xe00000)
            .setItem(new ItemStack(SpelunkerItem.itemGateKey, 1, 14));

    /* === Internal Members === */
    private final Map<Integer, MapPiece> colorToTileMapping = Maps.newHashMap();
    private BufferedImage mapImg;

    public static SpelunkerLevelMapper getInstance()
    {
        return instance;
    }

    public static void registerMapPiece(int color, MapPiece mapPiece)
    {
        instance.colorToTileMapping.put(color, mapPiece);
    }

    private SpelunkerLevelMapper()
    {
        try
        {
            this.loadMap("/assets/spelunker/leveldata/testLevel.png");
        }
        catch (IOException e)
        {
           throw new RuntimeException("Failed to load level mapping file", e);
        }
    }

    private void loadMap(String path) throws IOException
    {
        InputStream inputstream = null;

        try
        {
            inputstream = this.getClass().getResource(path).openStream();
            this.mapImg = ImageIO.read(inputstream);
        }
        finally
        {
            if (inputstream != null)
            {
                inputstream.close();
            }
        }
    }

    public MapPiece getMapPiece(int x, int y)
    {
        int color = mapImg.getRGB(x, y) & 0xffffff;
        return colorToTileMapping.get(color);
    }

    public int getHeight()
    {
        return mapImg.getHeight();
    }

    public int getWidth()
    {
        return mapImg.getWidth();
    }

}
