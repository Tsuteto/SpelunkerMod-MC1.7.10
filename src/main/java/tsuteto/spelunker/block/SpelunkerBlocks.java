package tsuteto.spelunker.block;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.block.tileentity.TileEntityLiftLauncher;

public class SpelunkerBlocks
{
    public static Block blockLiftDispatcher;
    public static Block blockRope;

    public static void load()
    {
        blockLiftDispatcher = $("liftLauncher", new BlockLiftLauncher())
                .withTileEntity(TileEntityLiftLauncher.class, "liftLauncher")
                .registerBlock()
                .setCreativeTab(SpelunkerMod.tabLevelComponents);
        blockRope = $("rope", new BlockRope())
                .registerBlock()
                .setCreativeTab(SpelunkerMod.tabLevelComponents);
    }

    public static <T extends Block> BlockRegister<T> $(String name, T block)
    {
        return new BlockRegister<T>(name, block);
    }

    @SideOnly(Side.CLIENT)
    public static void registerBlockRenderer()
    {
        RenderingRegistry.registerBlockHandler(new BlockRopeRenderer());
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
