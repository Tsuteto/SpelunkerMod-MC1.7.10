package tsuteto.spelunker.potion;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.config.Configuration;
import tsuteto.spelunker.damage.SpelunkerDamageSource;
import tsuteto.spelunker.util.ModLog;

public class SpelunkerPotion
{
    public static Potion choked;
    public static Potion heatStroke;
    public static List<Potion> disabledPotionList = new ArrayList<Potion>();

    public static void register(Configuration conf)
    {
        int id;

        try
        {

            id = conf.get("potion", "choked", SpelunkerPotion.assignId()).getInt();
            choked = getPotionChoked(id);

            id = conf.get("potion", "sunstroke", SpelunkerPotion.assignId()).getInt();
            heatStroke = getPotionSunstroke(id);
        }
        catch (Exception e)
        {
            ModLog.warn(e, e.getLocalizedMessage());
        }

        conf.save();
    }

    public static Potion getPotionChoked(int id)
    {
        return new PotionDamageTrigger(id, false, 0xffffff)
                .setDamageSource(SpelunkerDamageSource.choked, 10)
                .setPotionName("potion.choked")
                .setIconIndex(1, 0)
                .func_111184_a(SharedMonsterAttributes.movementSpeed, "7107DE5E-7CE8-4030-940E-514C1F160890", -0.15, 2);
    }

    public static Potion getPotionSunstroke(int id)
    {
        return new PotionDamageTrigger(id, false, 0xd71f00)
                .setDamageSource(SpelunkerDamageSource.sunstroke, 10)
                .setPotionName("potion.heatstroke")
                .setIconIndex(1, 0);
    }

    public static int assignId() throws Exception
    {
        // Find an undefined entry
        for (int i = Potion.potionTypes.length - 1; i >= 0; i--)
        {
            Potion potion = Potion.potionTypes[i];
            if (potion == null)
            {
                return i;
            }
        }

        // Unable to find entry
        throw new Exception("Failed to register a potion effect. Seems to be running out of potion ID.");
    }

    public static void setPotionDisabled(Potion potion)
    {
        disabledPotionList.add(potion);
        Potion.potionTypes[potion.id] = null;
    }

    public static void restorePotionList()
    {
        Potion.potionTypes[choked.id] = null;
        Potion.potionTypes[heatStroke.id] = null;

        for (Potion potion : disabledPotionList)
        {
            Potion.potionTypes[potion.id] = potion;
        }
    }
}
