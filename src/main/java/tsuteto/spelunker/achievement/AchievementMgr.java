package tsuteto.spelunker.achievement;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import tsuteto.spelunker.init.SpeAchievementList;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Map;

public class AchievementMgr
{
    public static Map<SpeAchievementList.Key, SpeAchievement> achievementList = Maps.newEnumMap(SpeAchievementList.Key.class);
    public static ArrayList<SpeAchievement> itemPickupMap = Lists.newArrayList();
    public static ArrayList<SpeAchievement> itemCraftingMap = Lists.newArrayList();
    public static ArrayList<SpeAchievement> itemSmeltingMap = Lists.newArrayList();

    public static ArrayList<SpeAchievement> scoreMap = Lists.newArrayList();
    public static ArrayList<SpeAchievement> deathMap = Lists.newArrayList();

    public static EnumSet<SpeAchievementList.Key> unregisteredKeys = EnumSet.allOf(SpeAchievementList.Key.class);

    public static void add(SpeAchievement ach)
    {
        achievementList.put(ach.key, ach);
        unregisteredKeys.remove(ach.key);
    }

    public static SpeAchievement[] getAllAsArray()
    {
        return achievementList.values().toArray(new SpeAchievement[0]);
    }

    public static SpeAchievement get(SpeAchievementList.Key key)
    {
        return achievementList.get(key);
    }

    public static void achieveCraftingItem(ItemStack itemstack, EntityPlayer player)
    {
        for (SpeAchievement ach : itemCraftingMap)
        {
            if (ach.triggerMatches(itemstack))
            {
                player.triggerAchievement(ach);
            }
        }
    }

    public static void achieveItemPickup(ItemStack itemstack, EntityPlayer player)
    {
        for (SpeAchievement ach : itemPickupMap)
        {
            if (ach.triggerMatches(itemstack))
            {
                player.triggerAchievement(ach);
            }
        }
    }

    public static void achieveSmeltingItem(ItemStack itemstack, EntityPlayer player)
    {
        for (SpeAchievement ach : itemSmeltingMap)
        {
            if (ach.triggerMatches(itemstack))
            {
                player.triggerAchievement(ach);
            }
        }
    }

    public static void achieveScore(int score, EntityPlayer player)
    {
        for (SpeAchievement ach : scoreMap)
        {
            if (ach.triggerMatches(score))
            {
                player.triggerAchievement(ach);
            }
        }
    }

    public static void achieveDeaths(int deaths, EntityPlayer player)
    {
        for (SpeAchievement ach : deathMap)
        {
            if (ach.triggerMatches(deaths))
            {
                player.triggerAchievement(ach);
            }
        }
    }

    public static void achieve(EntityPlayer player, SpeAchievementList.Key key)
    {
        player.triggerAchievement(get(key));
    }
}
