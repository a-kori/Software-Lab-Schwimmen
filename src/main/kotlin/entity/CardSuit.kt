package entity
import java.util.*

/**
 * Enum to distinguish between the four possible suits in a French-suited card game:
 * clubs, spades, hearts, or diamonds
 */
enum class CardSuit {
    CLUBS,
    SPADES,
    HEARTS,
    DIAMONDS,
    ;

    /**
     * Provides a single character to represent this suit.
     * Returns one of: ♣/♠/♥/♦
     */
    override fun toString() = when(this) {
        CLUBS -> "♣"
        SPADES -> "♠"
        HEARTS -> "♥"
        DIAMONDS -> "♦"
    }
    companion object{
        /**
         * Returns a set of suits for a regular set cards
         */
        fun getAllSuits(): Set<CardSuit> {
            return EnumSet.of(CLUBS, SPADES, HEARTS, DIAMONDS)
        }
    }
}
