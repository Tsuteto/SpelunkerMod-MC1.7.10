package tsuteto.spelunker.achievement;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import tsuteto.spelunker.SpelunkerMod;
import tsuteto.spelunker.init.SpeAchievementList;

import java.util.List;

public class SpeAchievement extends Achievement
{
    public final SpeAchievementList.Key key;
    private List<AchievementTrigger> trigger = Lists.newArrayList();

    public static SpeAchievement create(SpeAchievementList.Key key, int par3, int par4, Object obj, SpeAchievementList.Key relation)
    {
        if (obj instanceof Block)
        {
            return new SpeAchievement(key, par3, par4, (Block)obj, relation);
        }
        else if (obj instanceof Item)
        {
            return new SpeAchievement(key, par3, par4, (Item)obj, relation);
        }
        else if (obj instanceof ItemStack)
        {
            return new SpeAchievement(key, par3, par4, (ItemStack)obj, relation);
        }

        throw new RuntimeException("Caught an error in achievement registration.");
    }

    private SpeAchievement(SpeAchievementList.Key key, int par3, int par4, Block block, SpeAchievementList.Key relation)
    {
        super(getId(key.name()), getId(key.name()), par3, par4, block, AchievementMgr.get(relation));
        this.key = key;
    }

    private SpeAchievement(SpeAchievementList.Key key, int par3, int par4, Item item, SpeAchievementList.Key relation)
    {
        super(getId(key.name()), getId(key.name()), par3, par4, item, AchievementMgr.get(relation));
        this.key = key;
    }

    private SpeAchievement(SpeAchievementList.Key key, int par3, int par4, ItemStack itemstack, SpeAchievementList.Key relation)
    {
        super(getId(key.name()), getId(key.name()), par3, par4, itemstack, AchievementMgr.get(relation));
        this.key = key;
    }

    private static String getId(String name)
    {
        return SpelunkerMod.resourceDomain + name;
    }

    public SpeAchievement setTriggerItemPickup(ItemStack item)
    {
        this.trigger.add(new TriggerItem(item));
        if (!AchievementMgr.itemPickupMap.contains(this))
        {
            AchievementMgr.itemPickupMap.add(this);
        }
        return this;
    }

    public SpeAchievement setTriggerItemCrafting(ItemStack item)
    {
        this.trigger.add(new TriggerItem(item));
        if (!AchievementMgr.itemCraftingMap.contains(this))
        {
            AchievementMgr.itemCraftingMap.add(this);
        }
        return this;
    }

    public SpeAchievement setTriggerSmelting(ItemStack item)
    {
        this.trigger.add(new TriggerItem(item));
        if (!AchievementMgr.itemSmeltingMap.contains(this))
        {
            AchievementMgr.itemSmeltingMap.add(this);
        }
        return this;
    }

    public SpeAchievement setTriggerScore(int score)
    {
        this.trigger.add(new TriggerNumber(score));
        if (!AchievementMgr.scoreMap.contains(this))
        {
            AchievementMgr.scoreMap.add(this);
        }
        return this;
    }

    public SpeAchievement setTriggerDeaths(int deaths)
    {
        this.trigger.add(new TriggerNumber(deaths));
        if (!AchievementMgr.deathMap.contains(this))
        {
            AchievementMgr.deathMap.add(this);
        }
        return this;
    }

    @Override
    public SpeAchievement initIndependentStat()
    {
        super.initIndependentStat();
        return this;
    }

    @Override
    public SpeAchievement setSpecial()
    {
        super.setSpecial();
        return this;
    }

    @Override
    public SpeAchievement registerStat()
    {
        super.registerStat();
        AchievementMgr.add(this);
        return this;
    }

    public List<AchievementTrigger> getTrigger()
    {
        return this.trigger;
    }

    public boolean triggerMatches(Object obj)
    {
        for (AchievementTrigger trigger : this.trigger)
        {
            if (trigger.equals(obj))
            {
                return true;
            }
        }
        return false;
    }
}

