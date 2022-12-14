package entity
import java.util.*

/**
 * Enum to distinguish between the 13 possible values in a French-suited card game:
 * 2-10, Jack, Queen, King, and Ace.
 *
 * The values are ordered according to their most common ordering:
 * 2 < 3 < ... < 10 == Jack == Queen == King < Ace (11).
 */
enum class CardValue {
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING,
    ACE;

    /**
     * Provides an integer corresponding to this value.
     * Returns one of: 2/3/4/5/6/7/8/9/10/10/10/10/11
     */
    fun numeric() =
        when(this) {
            TWO -> 2
            THREE -> 3
            FOUR -> 4
            FIVE -> 5
            SIX -> 6
            SEVEN -> 7
            EIGHT -> 8
            NINE -> 9
            TEN -> 10
            JACK -> 10
            QUEEN -> 10
            KING -> 10
            ACE -> 11
        }

    /**
     * Provides a single character to represent this value.
     * Returns one of: 2/3/4/5/6/7/8/9/10/J/Q/K/A
     */
    override fun toString() =
        when(this) {
            TWO -> "2"
            THREE -> "3"
            FOUR -> "4"
            FIVE -> "5"
            SIX -> "6"
            SEVEN -> "7"
            EIGHT -> "8"
            NINE -> "9"
            TEN -> "10"
            JACK -> "J"
            QUEEN -> "Q"
            KING -> "K"
            ACE -> "A"
        }

    companion object {
        /**
         * Returns a set of values for a reduced set of 4x8=32 cards (starting with the 7)
         */
        fun getAllValuesReduced(): Set<CardValue> {
            return EnumSet.of(SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE)
        }

        /**
         * Returns a set of values for a full set of 4x13=52 cards
         */
        fun getAllValuesFull(): Set<CardValue> {
            return EnumSet.of(TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE)
        }
    }
}