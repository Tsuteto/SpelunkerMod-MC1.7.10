package tsuteto.spelunker.item;

public enum EnumGunMaterial
{
    WOOD(     3,  2,  2.0F,  8, 8, 30,  59, true),
    IRON(     4,  4,  2.5F,  8, 4, 30, 131, true),
    ORIGINAL( 2,  8, 20.0F, 20, 3, 20, 500, true),
    GOLD(    10, 12,  3.0F,  2, 8, 30,  32, false),
    CLIENT(   0,  1,    0F, 99, 0,  0,   0, false);

    private final int damageVsEntity;
    private final int enchantability;
    private final float bulletSpeed;
    private final int bulletReach;
    private final int fireInterval;
    private final int gunfireCost;
    private final int maxUses;
    private final boolean isAutoFire;

    private EnumGunMaterial(int par3, int par4, float par5, int par6, int par7, int par8, int par9, boolean par10)
    {
        damageVsEntity = par3;
        enchantability = par4;
        bulletSpeed = par5;
        bulletReach = par6;
        fireInterval = par7;
        gunfireCost = par8;
        maxUses = par9;
        isAutoFire = par10;
    }

    public int getDamageVsEntity()
    {
        return damageVsEntity;
    }

    public int getEnchantability()
    {
        return enchantability;
    }

    public float getBulletSpeed()
    {
        return bulletSpeed;
    }

    public int getBulletReach()
    {
        return bulletReach;
    }

    public int getFireInterval()
    {
        return fireInterval;
    }

    public int getGunfireCost()
    {
        return gunfireCost;
    }

    public int getMaxUses()
    {
        return maxUses;
    }

    public boolean isAutoFire()
    {
        return isAutoFire;
    }
}
