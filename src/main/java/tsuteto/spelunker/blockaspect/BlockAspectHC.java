package tsuteto.spelunker.blockaspect;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import tsuteto.spelunker.damage.SpelunkerDamageSource;

/**
 * For hardcore spelunker for now
 *
 * @author Tsuteto
 */
public class BlockAspectHC<T>
{
    public enum Aspect
    {
        /** Give damage when left clicking */
        UnclickableLeft,
        /** Give damage when right clicking */
        UnclickableRight,
        /** Give damage when digging */
        Undiggable,
        /** Give damage when destroying */
        Unbreakable,
        /** Give damage in collision */
        Untouchable
    }

    public static final Map<Aspect, List<BlockAspectHC>> registry = new EnumMap<Aspect, List<BlockAspectHC>>(Aspect.class)
    {
        {
            for (Aspect a : EnumSet.allOf(Aspect.class))
            {
                put(a, Lists.<BlockAspectHC>newArrayList());
            }
        }
    };

    public static void init()
    {
        create(Material.glass, SpelunkerDamageSource.glass, 1.5F)
                .setAspects(Aspect.Unbreakable)
                .register();

        create(Blocks.cactus, DamageSource.cactus, 1.0F)
                .setAspects(Aspect.UnclickableLeft)
                .addAllowEfficientTool(Aspect.UnclickableLeft)
                .register();

        create(Blocks.mycelium, SpelunkerDamageSource.mycelium, 1.0F)
                .setAspects(Aspect.Unbreakable)
                .register();

        create(Blocks.lit_furnace, DamageSource.onFire, 2.0F)
                .setAspects(Aspect.UnclickableLeft, Aspect.UnclickableRight, Aspect.Untouchable)
                .addAllowEfficientTool(Aspect.UnclickableLeft)
                .register();

        create(Blocks.lit_pumpkin, DamageSource.onFire, 0.5F)
                .setAspects(Aspect.UnclickableLeft, Aspect.Untouchable)
                .addAllowEfficientTool(Aspect.UnclickableLeft)
                .register();

        create(Blocks.soul_sand, SpelunkerDamageSource.soulSand, 1.0F)
                .setAspects(Aspect.UnclickableLeft, Aspect.Untouchable)
                .register();

        create(Blocks.leaves, SpelunkerDamageSource.grass, 0.5F)
                .setAspects(Aspect.Untouchable)
                .register();
    }

    public static BlockAspectClass create(Class material, DamageSource dmgsrc, float amount)
    {
        return new BlockAspectClass(material, dmgsrc, amount);
    }

    public static BlockAspectMaterial create(Material material, DamageSource dmgsrc, float amount)
    {
        return new BlockAspectMaterial(material, dmgsrc, amount);
    }

    public static BlockAspectBlock create(Block block, DamageSource dmgsrc, float amount)
    {
        return new BlockAspectBlock(block, dmgsrc, amount);
    }

    public static boolean matches(EntityPlayer player, Block block, Aspect aspect)
    {
        for (BlockAspectHC entry : registry.get(aspect))
        {
            if (entry.matchesCondition(block, player, aspect))
            {
                return true;
            }
        }
        return false;
    }

    public static boolean applyDamage(EntityPlayer player, Block block, Aspect aspect)
    {
        for (BlockAspectHC entry : registry.get(aspect))
        {
            if (entry.matchesCondition(block, player, aspect))
            {
                entry.applyDamage(player);
                return true;
            }
        }
        return false;
    }

    protected T target;
    public EnumSet<Aspect> aspects = EnumSet.noneOf(Aspect.class);
    protected EnumSet<Aspect> allowEfficientTool = EnumSet.noneOf(Aspect.class);

    protected DamageSource dmgsrc;
    protected float dmgAmount;

    public BlockAspectHC(T target, DamageSource dmgsrc, float amount)
    {
        this.target = target;
        this.dmgsrc = dmgsrc;
        this.dmgAmount = amount;
    }

    public boolean matchesCondition(Block block, EntityPlayer player, Aspect aspect)
    {
        ItemStack equipped = player.getCurrentEquippedItem();
        boolean isBreakable = equipped != null && equipped.func_150997_a(block) > 1.0;

        if (allowEfficientTool.contains(aspect) && isBreakable) return false;
        return matches(block);
    }

    protected boolean matches(Block block)
    {
        return target.equals(block);
    }

    public void applyDamage(EntityPlayer player)
    {
        player.attackEntityFrom(dmgsrc, dmgAmount);
    }

    public BlockAspectHC<T> setAspects(Aspect... aspects)
    {
        this.aspects.addAll(Lists.newArrayList(aspects));
        return this;
    }

    public BlockAspectHC<T> addAllowEfficientTool(Aspect tool)
    {
        allowEfficientTool.add(tool);
        return this;
    }

    public void register()
    {
        for (Aspect a : aspects)
        {
            registry.get(a).add(this);
        }
    }
}
