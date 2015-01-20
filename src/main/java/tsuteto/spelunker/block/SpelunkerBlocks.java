package tsuteto.spelunker.block;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;
import net.minecraft.tileentity.TileEntity;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.block.render.*;
import tsuteto.spelunker.block.tileentity.*;
import tsuteto.spelunker.item.ItemBlockInvisible;
import tsuteto.spelunker.item.ItemSpelunkerPortal;

public class SpelunkerBlocks
{
    public static Block blockSpelunkerPortal;
    public static Block blockWall;
    public static Block blockMagicWall;
    public static Block blockLiftDispatcher;
    public static Block blockRope;
    public static Block blockFallingFloor;
    public static Block blockLockedGate;
    public static BlockBumpy blockBumpy;
    public static Block blockItemBox;
    public static Block blockPlatform;
    public static Block blockRail;
    public static Block blockRailGirder;
    public static Block blockLight;
    public static Block blockBatSpawner;
    public static Block blockStartPoint;
    public static Block blockRespawnPoint;
    public static Block blockRespawnGate;
    public static Block blockRock;
    public static Block blockBreakableWall;
    public static Block blockHiddenDiamond;

    public static void load()
    {
        blockSpelunkerPortal = $("spelunkerPortal", new BlockSpelunkerPortal(Material.iron))
                .withTileEntity(TileEntitySpelunkerPortal.class)
                .wrappedBy(ItemSpelunkerPortal.class)
                .registerBlock()
                .setHardness(2.0F)
                .setCreativeTab(SpelunkerMod.tabLevelComponents)
                ;

        blockStartPoint = $("startPoint", new BlockRespawnPoint(SpelunkerMaterial.invisible))
                .withTileEntity(TileEntityRespawnPoint.class)
                .wrappedBy(ItemBlockInvisible.class)
                .registerBlock()
                .setBlockTextureName(SpelunkerMod.resourceDomain + "transparent")
                .setBlockUnbreakable()
                .setResistance(6000000.0F)
                ;

        blockRespawnGate = $("respawnGate", new BlockRespawnPoint(SpelunkerMaterial.invisible))
                .withTileEntity(TileEntityRespawnPoint.class)
                .wrappedBy(ItemBlockInvisible.class)
                .registerBlock()
                .setBlockTextureName(SpelunkerMod.resourceDomain + "transparent")
                .setBlockUnbreakable()
                .setResistance(6000000.0F)
                ;

        blockWall = $("wall", new BlockWall(Material.rock))
                .registerBlock()
                .setBlockUnbreakable()
                .setResistance(6000000.0F)
                .setCreativeTab(SpelunkerMod.tabLevelComponents)
                ;

        blockMagicWall = $("magicWall", new BlockMagicWall(Material.rock))
                .registerBlock()
                .setBlockUnbreakable()
                .setResistance(6000000.0F)
                .setCreativeTab(SpelunkerMod.tabLevelComponents)
                ;

        blockLiftDispatcher = $("liftLauncher", new BlockLiftLauncher())
                .withTileEntity(TileEntityLiftLauncher.class)
                .registerBlock()
                .setBlockUnbreakable()
                .setResistance(6000000.0F)
                .setCreativeTab(SpelunkerMod.tabLevelComponents)
                ;
        blockRope = $("rope", new BlockRope())
                .registerBlock()
                .setBlockUnbreakable()
                .setResistance(6000000.0F)
                .setCreativeTab(SpelunkerMod.tabLevelComponents)
                ;
        blockFallingFloor = $("fallingFloor", new BlockFallingFloor())
                .registerBlock()
                .setBlockTextureName(SpelunkerMod.resourceDomain + "wall")
                .setBlockUnbreakable()
                .setResistance(6000000.0F)
                .setCreativeTab(SpelunkerMod.tabLevelComponents)
                ;
        blockLockedGate = $("lockedGate", new BlockLockedGate())
                .withTileEntity(TileEntityLockedGate.class)
                .wrappedBy(ItemCloth.class)
                .registerBlock()
                .setCreativeTab(SpelunkerMod.tabLevelComponents)
                ;
        blockBumpy = (BlockBumpy)$("bumpyBlock", new BlockBumpy(Material.rock))
                .withTileEntity(TileEntityBlockBumpy.class)
                .registerBlock()
                .setBlockUnbreakable()
                .setResistance(6000000.0F)
                .setCreativeTab(SpelunkerMod.tabLevelComponents)
                ;
        blockRock = $("rock", new BlockRock(Material.rock))
                .registerBlock()
                .setBlockUnbreakable()
                .setResistance(0.0F)
                .setCreativeTab(SpelunkerMod.tabLevelComponents)
                ;
        blockBreakableWall = $("breakableWall", new BlockBreakable(Material.rock))
                .registerBlock()
                .setBlockUnbreakable()
                .setResistance(0.0F)
                .setCreativeTab(SpelunkerMod.tabLevelComponents)
                ;
        blockHiddenDiamond = $("hiddenDiamond", new BlockHiddenDiamond(Material.rock))
                .registerBlock()
                .setBlockUnbreakable()
                .setResistance(0.0F)
                .setCreativeTab(SpelunkerMod.tabLevelComponents)
                ;
        blockItemBox = $("itemBox", new BlockItemBox(SpelunkerMaterial.invisible))
                .withTileEntity(TileEntityItemBox.class)
                .registerBlock()
                .setBlockUnbreakable()
                .setResistance(6000000.0F)
                .setCreativeTab(SpelunkerMod.tabLevelComponents)
                ;

        blockPlatform = $("platform", new BlockPlatform(Material.circuits))
                .registerBlock()
                .setBlockUnbreakable()
                .setResistance(6000000.0F)
                .setCreativeTab(SpelunkerMod.tabLevelComponents)
                ;

        blockRail = $("rail", new BlockSpeRail(false))
                .registerBlock()
                .setBlockUnbreakable()
                .setResistance(6000000.0F)
                .setCreativeTab(SpelunkerMod.tabLevelComponents)
                ;

        blockRailGirder = $("railGirder", new BlockRailGirder(Material.circuits))
                .registerBlock()
                .setBlockUnbreakable()
                .setResistance(6000000.0F)
                .setCreativeTab(SpelunkerMod.tabLevelComponents)
                ;
        blockLight = $("light", new BlockInvisible(SpelunkerMaterial.invisible))
                .wrappedBy(ItemBlockInvisible.class)
                .registerBlock()
                .setLightLevel(1.0F)
                .setBlockUnbreakable()
                .setResistance(6000000.0F)
                ;
        blockBatSpawner = $("batSpawner", new BlockBatSpawner(SpelunkerMaterial.invisible))
                .registerBlock()
                .setBlockTextureName(SpelunkerMod.resourceDomain + "transparent")
                .setBlockUnbreakable()
                .setResistance(6000000.0F)
                .setCreativeTab(SpelunkerMod.tabLevelComponents)
                ;
    }

    public static <T extends Block> BlockRegister<T> $(String name, T block)
    {
        return new BlockRegister<T>(name, block);
    }

    @SideOnly(Side.CLIENT)
    public static void registerBlockRenderer()
    {
        RenderingRegistry.registerBlockHandler(new BlockRopeRenderer());
        RenderingRegistry.registerBlockHandler(new BlockLockedGateRenderer());
        RenderingRegistry.registerBlockHandler(new BlockBumpyRenderer());

        ClientRegistry.registerTileEntity(TileEntityItemBox.class, SpelunkerMod.resourceDomain + "itemContainerRen", new TileEntityItemContainerRenderer());
        ClientRegistry.registerTileEntity(TileEntitySpelunkerPortal.class, SpelunkerMod.resourceDomain + "spelunkerPortalRen", new TileEntitySpelunkerPortalRenderer());
    }

    private static class BlockRegister<T extends Block>
    {
        private T block;
        private Class<? extends ItemBlock> itemBlock = ItemBlock.class;
        private Object[] itemCtorArgs;
        private String uniqueName;
        private String resourceName;

        public BlockRegister(String name, T block)
        {
            this.block = block;
            this.uniqueName = name;
            this.resourceName = name;
        }

        public BlockRegister<T> withResource(String name)
        {
            this.resourceName = name;
            return this;
        }

        public BlockRegister<T> wrappedBy(Class<? extends ItemBlock> itemBlock)
        {
            this.itemBlock = itemBlock;
            return this;
        }

        public BlockRegister<T> havingArgs(Object... itemCtorArgs)
        {
            this.itemCtorArgs = itemCtorArgs;
            return this;
        }

        public BlockRegister<T> setHarvestLevel(String tool, int level)
        {
            block.setHarvestLevel(tool, level);
            return this;
        }

        public BlockRegister<T> withTileEntity(Class<? extends TileEntity> tileEntity)
        {
            return this.withTileEntity(tileEntity, uniqueName);
        }

        public BlockRegister<T> withTileEntity(Class<? extends TileEntity> tileEntity, String name)
        {
            GameRegistry.registerTileEntity(tileEntity, SpelunkerMod.resourceDomain + name);
            return this;
        }

        public T registerBlock()
        {
            block.setBlockName(SpelunkerMod.resourceDomain + resourceName);
            block.setBlockTextureName(SpelunkerMod.resourceDomain + resourceName);
            if (itemCtorArgs != null)
            {
                GameRegistry.registerBlock(block, itemBlock, uniqueName, itemCtorArgs);
            }
            else
            {
                GameRegistry.registerBlock(block, itemBlock, uniqueName);
            }
            return block;
        }
    }
}
