package tsuteto.spelunker.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import tsuteto.spelunker.achievement.AchievementMgr;
import tsuteto.spelunker.achievement.SpeAchievement;
import tsuteto.spelunker.util.ModLog;

public class SpeAchievementList
{
    public enum Key
    {
        death, deaths50, deaths100, deaths200, deaths300, deaths400, deaths500, deaths600, deaths700, deaths800, deaths900,
        deaths1000, deaths2000, deaths3000, deaths4000, deaths5000, deaths6000, deaths7000, deaths8000, deaths9000,
        deaths10000, deaths20000, deaths30000, deaths40000, deaths50000, deaths100000, deaths1m,
        scored, score5000, score10000, score50000, score100000, score200000, score300000, score400000, score500000,
        score1m, score2m, score3m, score4m, score5m, scoreHalf, score10m, score1cycled, score2cycled, score3cycled, score4cycled, score5cycled, score100m, score6cycled,
        speWorld, speWorldCleared, ghost, airBlaster, machineBlaster, speBlaster, bomberMan,
        mcSpelunker, defeatDragon, defeatWither
    }

    public static void register()
    {

        SpeAchievement.create(Key.death, -2, -2, SpelunkerItems.item1up, null)
                .setTriggerDeaths(1)
                .registerStat();
        SpeAchievement.create(Key.deaths50, 0, -2, SpelunkerItems.item1up, Key.death)
                .setTriggerDeaths(50)
                .registerStat();
        SpeAchievement.create(Key.deaths100, 2, -2, SpelunkerItems.item1up, Key.deaths50)
                .setTriggerDeaths(100)
                .registerStat();
        SpeAchievement.create(Key.deaths200, 4, -2, SpelunkerItems.item1up, Key.deaths100)
                .setTriggerDeaths(200)
                .registerStat();
        SpeAchievement.create(Key.deaths300, 6, -2, SpelunkerItems.item1up, Key.deaths200)
                .setTriggerDeaths(300)
                .registerStat();
        SpeAchievement.create(Key.deaths400, 8, -2, SpelunkerItems.item1up, Key.deaths300)
                .setTriggerDeaths(400)
                .registerStat();
        SpeAchievement.create(Key.deaths500, 10, -2, SpelunkerItems.item1up, Key.deaths400)
                .setTriggerDeaths(500)
                .registerStat();
        SpeAchievement.create(Key.deaths600, 12, -2, SpelunkerItems.item1up, Key.deaths500)
                .setTriggerDeaths(600)
                .registerStat();
        SpeAchievement.create(Key.deaths700, 14, -2, SpelunkerItems.item1up, Key.deaths600)
                .setTriggerDeaths(700)
                .registerStat();
        SpeAchievement.create(Key.deaths800, 16, -2, SpelunkerItems.item1up, Key.deaths700)
                .setTriggerDeaths(800)
                .registerStat();
        SpeAchievement.create(Key.deaths900, 18, -2, SpelunkerItems.item1up, Key.deaths800)
                .setTriggerDeaths(900)
                .registerStat();
        SpeAchievement.create(Key.deaths1000, 18, 0, SpelunkerItems.item1up, Key.deaths900)
                .setTriggerDeaths(1000)
                .setSpecial()
                .registerStat();
        SpeAchievement.create(Key.deaths2000, 16, 0, SpelunkerItems.item1up, Key.deaths1000)
                .setTriggerDeaths(2000)
                .registerStat();
        SpeAchievement.create(Key.deaths3000, 14, 0, SpelunkerItems.item1up, Key.deaths2000)
                .setTriggerDeaths(3000)
                .registerStat();
        SpeAchievement.create(Key.deaths4000, 14, 2, SpelunkerItems.item1up, Key.deaths3000)
                .setTriggerDeaths(4000)
                .registerStat();
        SpeAchievement.create(Key.deaths5000, 16, 2, SpelunkerItems.item1up, Key.deaths4000)
                .setTriggerDeaths(5000)
                .registerStat();
        SpeAchievement.create(Key.deaths6000, 18, 2, SpelunkerItems.item1up, Key.deaths5000)
                .setTriggerDeaths(6000)
                .registerStat();
        SpeAchievement.create(Key.deaths7000, 18, 4, SpelunkerItems.item1up, Key.deaths6000)
                .setTriggerDeaths(7000)
                .registerStat();
        SpeAchievement.create(Key.deaths8000, 16, 4, SpelunkerItems.item1up, Key.deaths7000)
                .setTriggerDeaths(8000)
                .registerStat();
        SpeAchievement.create(Key.deaths9000, 14, 4, SpelunkerItems.item1up, Key.deaths8000)
                .setTriggerDeaths(9000)
                .registerStat();
        SpeAchievement.create(Key.deaths10000, 14, 6, SpelunkerItems.item1up, Key.deaths9000)
                .setTriggerDeaths(10000)
                .setSpecial()
                .registerStat();
        SpeAchievement.create(Key.deaths20000, 16, 6, SpelunkerItems.item1up, Key.deaths10000)
                .setTriggerDeaths(20000)
                .setSpecial()
                .registerStat();
        SpeAchievement.create(Key.deaths30000, 18, 6, SpelunkerItems.item1up, Key.deaths20000)
                .setTriggerDeaths(30000)
                .setSpecial()
                .registerStat();
        SpeAchievement.create(Key.deaths40000, 18, 8, SpelunkerItems.item1up, Key.deaths30000)
                .setTriggerDeaths(40000)
                .setSpecial()
                .registerStat();
        SpeAchievement.create(Key.deaths50000, 16, 8, SpelunkerItems.item1up, Key.deaths40000)
                .setTriggerDeaths(50000)
                .setSpecial()
                .registerStat();
        SpeAchievement.create(Key.deaths100000, 14, 8, SpelunkerItems.item1up, Key.deaths50000)
                .setTriggerDeaths(100000)
                .setSpecial()
                .registerStat();
        SpeAchievement.create(Key.deaths1m, 14, 10, SpelunkerItems.item1up, Key.deaths100000)
                .setTriggerDeaths(1000000)
                .setSpecial()
                .registerStat();

        SpeAchievement.create(Key.scored, -2, 0, SpelunkerItems.itemDollar, null)
                .setTriggerScore(10)
                .registerStat();
        SpeAchievement.create(Key.score5000, 0, 0, SpelunkerItems.itemDollar, Key.scored)
                .setTriggerScore(5000)
                .registerStat();
        SpeAchievement.create(Key.score10000, 2, 0, SpelunkerItems.itemDollar, Key.score5000)
                .setTriggerScore(10000)
                .registerStat();
        SpeAchievement.create(Key.score50000, 4, 0, SpelunkerItems.itemDollar, Key.score10000)
                .setTriggerScore(50000)
                .registerStat();
        SpeAchievement.create(Key.score100000, 6, 0, SpelunkerItems.itemDollar, Key.score50000)
                .setTriggerScore(100000)
                .registerStat();
        SpeAchievement.create(Key.score200000, 8, 0, SpelunkerItems.itemDollar, Key.score100000)
                .setTriggerScore(200000)
                .registerStat();
        SpeAchievement.create(Key.score300000, 10, 0, SpelunkerItems.itemDollar, Key.score200000)
                .setTriggerScore(300000)
                .registerStat();
        SpeAchievement.create(Key.score400000, 12, 0, SpelunkerItems.itemDollar, Key.score300000)
                .setTriggerScore(400000)
                .registerStat();
        SpeAchievement.create(Key.score500000, 12, 2, SpelunkerItems.itemDollar, Key.score400000)
                .setTriggerScore(500000)
                .registerStat();
        SpeAchievement.create(Key.score1m, 10, 2, SpelunkerItems.itemCoin, Key.score500000)
                .setTriggerScore(1000000)
                .setSpecial()
                .registerStat();
        SpeAchievement.create(Key.score2m, 8, 2, SpelunkerItems.itemCoin, Key.score1m)
                .setTriggerScore(2000000)
                .registerStat();
        SpeAchievement.create(Key.score3m, 8, 4, SpelunkerItems.itemCoin, Key.score2m)
                .setTriggerScore(3000000)
                .registerStat();
        SpeAchievement.create(Key.score4m, 10, 4, SpelunkerItems.itemCoin, Key.score3m)
                .setTriggerScore(4000000)
                .registerStat();
        SpeAchievement.create(Key.score5m, 12, 4, SpelunkerItems.itemCoin, Key.score4m)
                .setTriggerScore(5000000)
                .registerStat();
        SpeAchievement.create(Key.scoreHalf, 12, 6, SpelunkerItems.itemCoin, Key.score5m)
                .setTriggerScore(0x80000)
                .registerStat();
        SpeAchievement.create(Key.score10m, 10, 6, Items.diamond, Key.scoreHalf)
                .setTriggerScore(10000000)
                .registerStat();
        SpeAchievement.create(Key.score1cycled, 8, 6, Items.diamond, Key.score10m)
                .setTriggerScore(0x100000)
                .setSpecial()
                .registerStat();
        SpeAchievement.create(Key.score2cycled, 8, 8, Items.diamond, Key.score1cycled)
                .setTriggerScore(0x200000)
                .setSpecial()
                .registerStat();
        SpeAchievement.create(Key.score3cycled, 10, 8, Items.diamond, Key.score2cycled)
                .setTriggerScore(0x300000)
                .setSpecial()
                .registerStat();
        SpeAchievement.create(Key.score4cycled, 12, 8, Items.diamond, Key.score3cycled)
                .setTriggerScore(0x400000)
                .setSpecial()
                .registerStat();
        SpeAchievement.create(Key.score5cycled, 12, 10, Items.diamond, Key.score4cycled)
                .setTriggerScore(0x500000)
                .setSpecial()
                .registerStat();
        SpeAchievement.create(Key.score100m, 10, 10, Items.diamond, Key.score5cycled)
                .setTriggerScore(100000000)
                .setSpecial()
                .registerStat();
        SpeAchievement.create(Key.score6cycled, 8, 10, Items.diamond, Key.score100m)
                .setTriggerScore(0x600000)
                .setSpecial()
                .registerStat();

        SpeAchievement.create(Key.speWorld, -2, 2, SpelunkerItems.itemHelmet, null)
                .registerStat();
        SpeAchievement.create(Key.speWorldCleared, -2, 4, SpelunkerItems.itemHelmet, Key.speWorld)
                .registerStat();

        SpeAchievement.create(Key.ghost, 0, 2, SpelunkerItems.itemGunSpelunker, null)
                .registerStat();
        SpeAchievement.create(Key.bomberMan, 2, 2, SpelunkerItems.itemDynamite, null)
                .registerStat();

        SpeAchievement.create(Key.airBlaster, 0, 4, SpelunkerItems.itemGunWood, null)
                .setTriggerItemCrafting(new ItemStack(SpelunkerItems.itemGunWood))
                .registerStat();
        SpeAchievement.create(Key.machineBlaster, 2, 4, SpelunkerItems.itemGunSteel, null)
                .setTriggerItemCrafting(new ItemStack(SpelunkerItems.itemGunSteel))
                .registerStat();
        SpeAchievement.create(Key.speBlaster, 4, 4, SpelunkerItems.itemGunSpelunker, null)
                .setTriggerItemCrafting(new ItemStack(SpelunkerItems.itemGunSpelunker))
                .registerStat();

        SpeAchievement.create(Key.mcSpelunker, 4, 2, SpelunkerItems.itemGunSpelunker, null)
                .registerStat();
        SpeAchievement.create(Key.defeatDragon, 6, 2, Blocks.dragon_egg, null)
                .registerStat();
        SpeAchievement.create(Key.defeatWither, 6, 4, Items.nether_star, null)
                .registerStat();


        // Add a new achievement page for the mod
        Achievement[] array = AchievementMgr.getAllAsArray();
        AchievementPage.registerAchievementPage(new AchievementPage("Spelunker", array));

        ModLog.info("%d achievements for SpelunkerMod has been registered.", array.length);
        if (!AchievementMgr.unregisteredKeys.isEmpty())
        {
            throw new IllegalStateException(String.format("Unregistered Achievements Found: %s", AchievementMgr.unregisteredKeys));
        }

    }
}
