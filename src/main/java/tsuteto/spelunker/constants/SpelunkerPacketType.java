package tsuteto.spelunker.constants;

import cpw.mods.fml.relauncher.Side;

/**
 * Defines packet types for Spelunker Mod
 *
 * @author Tsuteto
 *
 */
public enum SpelunkerPacketType {
	INIT(Side.SERVER),
    INIT_SUCCEEDED(Side.CLIENT),
	INIT_FAILED(Side.CLIENT),
    INIT_SPE_LEVEL(Side.CLIENT),
    TOGGLE_HC(Side.CLIENT),

	ENERGY(Side.CLIENT),
	LIVES(Side.CLIENT),
	DEATHS(Side.CLIENT),
	SCORE(Side.CLIENT),
	ENERGY_DEC(Side.SERVER),
	ENERGY_UP(Side.CLIENT),
    SPAWN_INV(Side.CLIENT),

	IN_CAVE_FALSE(Side.CLIENT),
	IN_CAVE_TRUE(Side.CLIENT),

	OUT_OF_2x(Side.CLIENT),
	GOT_2x(Side.CLIENT),

	OUT_OF_POTION(Side.CLIENT),
	GOT_POTION(Side.CLIENT),

	GOT_INVINCIBLE(Side.CLIENT),
	OUT_OF_INVINCIBLE(Side.CLIENT),

    GHOST_COMING(Side.CLIENT),
    GHOST_GONE(Side.CLIENT),

    SET_HARDCORE(Side.CLIENT),
    GAMEOVER(Side.SERVER),
    ALL_CLEARED(Side.CLIENT),

    SPE_START(Side.CLIENT),
    SPE_CHECKPOINT(Side.CLIENT),
    SPE_CLEARED(Side.CLIENT),
    SPE_CHEATED(Side.CLIENT),

    CHOKED(Side.SERVER),
    HOP(Side.CLIENT),
    ROPE_JUMP(Side.SERVER),
    CHECK_POTION_ID(Side.CLIENT),
    RESET_GS(Side.CLIENT),
    GHOST_TARGETING(Side.CLIENT),
    DEL_LEVEL_BEST(Side.CLIENT),

    DMG_FIREWORKS(Side.SERVER),
    DMG_HARDBLOCK(Side.SERVER),
    DMG_VILLAGER(Side.CLIENT),
    DMG_BLOCKHITTING(Side.SERVER);

    public final Side messageTo;

    SpelunkerPacketType(Side messageTo)
    {
        this.messageTo = messageTo;
    }
}
