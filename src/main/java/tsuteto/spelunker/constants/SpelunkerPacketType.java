package tsuteto.spelunker.constants;

/**
 * Defines packet types for Spelunker Mod
 *
 * @author Tsuteto
 *
 */
public enum SpelunkerPacketType {
	INIT,
	INIT_FAILED,

	ENERGY,
	LIVES,
	DEATHS,
	SCORE,
	ENERGY_DEC,
	ENERGY_UP,

	IN_CAVE_FALSE,
	IN_CAVE_TRUE,

	OUT_OF_2x,
	GOT_2x,

	OUT_OF_POTION,
	GOT_POTION,

	GOT_INVINCIBLE,
	OUT_OF_INVINCIBLE,

    GAMEOVER,
    ALL_CLEARED,

    CHOKED,
    HOP,
    CHECK_POTION_ID,
    RESET_GS,

    DMG_FIREWORKS,
    DMG_HARDBLOCK,
    DMG_VILLAGER,
    DMG_BLOCKHITTING,
    ;
}
