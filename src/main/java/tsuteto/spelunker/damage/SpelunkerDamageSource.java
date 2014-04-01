package tsuteto.spelunker.damage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

/**
 * DamageSource wrapper for Spelunker
 *
 * @author Tsuteto
 *
 */
public class SpelunkerDamageSource extends DamageSource
{
    public static DamageSource drained = new SpelunkerDamageSource("drained").setDamageBypassesArmor();
    public static DamageSource droppings = new SpelunkerDamageSource("droppings").setTerrainDamage().setDamageBypassesArmor();
    public static DamageSource headHitting = new SpelunkerDamageSource("headHitting").setDamageBypassesArmor();
    public static DamageSource neckTwisting = new SpelunkerDamageSource("neckTwisting").setDamageBypassesArmor();
    public static DamageSource darkness = new SpelunkerDamageSource("darkness").setTerrainDamage().setDamageBypassesArmor();
    public static DamageSource hardBlock = new SpelunkerDamageSource("blockHitting").setDamageBypassesArmor();
    public static DamageSource falldown = new SpelunkerDamageSource("falldown");
    public static DamageSource choked = new SpelunkerDamageSource("choked").setDamageBypassesArmor();
    public static DamageSource hardBed = new SpelunkerDamageSource("bed");
    public static DamageSource sleepy = new SpelunkerDamageSource("sleepy");
    public static DamageSource overload = new SpelunkerDamageSource("overload").setDamageBypassesArmor();
    public static DamageSource sideEffect = new SpelunkerDamageSource("sideEffect").setDamageBypassesArmor();
    public static DamageSource sunstroke = new SpelunkerDamageSource("sunstroke").setDamageBypassesArmor();
    public static DamageSource itemDestroy = new SpelunkerDamageSource("itemDestroy");
    public static DamageSource grass = new SpelunkerDamageSource("grass").setTerrainDamage();
    public static DamageSource soulSand = new SpelunkerDamageSource("soulSand").setTerrainDamage().setDamageBypassesArmor();
    public static DamageSource enderGaze = new SpelunkerDamageSource("enderGaze").setTerrainDamage().setDamageBypassesArmor();
    public static DamageSource cursedWords = new SpelunkerDamageSource("cursedWords").setDamageBypassesArmor();
    public static DamageSource water = new SpelunkerDamageSource("water").setTerrainDamage().setDamageBypassesArmor();
    public static DamageSource door = new SpelunkerDamageSource("door");
    public static DamageSource thunderclap = new SpelunkerDamageSource("thunderclap").setTerrainDamage().setDamageBypassesArmor();
    public static DamageSource villager = new SpelunkerDamageSource("villager").setTerrainDamage().setDamageBypassesArmor();
    public static DamageSource worthyItemDropped = new SpelunkerDamageSource("worthyItemDropped").setDamageBypassesArmor();
    public static DamageSource glass = new SpelunkerDamageSource("glass");
    public static DamageSource mycelium = new SpelunkerDamageSource("mycelium").setDamageBypassesArmor();
    public static DamageSource web = new SpelunkerDamageSource("web").setTerrainDamage();

    private boolean isTerrainDamage = false;

    protected SpelunkerDamageSource(String par1Str)
    {
        super(par1Str);
    }

    public SpelunkerDamageSource setTerrainDamage()
    {
        isTerrainDamage = true;
        return this;
    }

    public boolean isTerrainDamage()
    {
        return isTerrainDamage;
    }

    @Override
    public SpelunkerDamageSource setDamageBypassesArmor()
    {
        super.setDamageBypassesArmor();
        return this;
    }

    public static DamageSource causeFallingItemDamage(Entity par0Entity)
    {
        return (new EntityDamageSourceFallingItem("fallingItem", par0Entity)).setProjectile();
    }

    public static DamageSource causeDamageOfPetKilled(EntityLivingBase pet)
    {
        return new EntityDamageSourcePetKilled("petKilled", pet);
    }
}
