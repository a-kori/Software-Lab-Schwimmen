package entity

/**
 * Data class representing a single card with a given, immutable suit and value.
 */
data class Card(val suit: CardSuit, val value: CardValue) {
    /**
     * Provides a two character string to represent a card.
     * The string consists of a suit character and a value character,
     * taken from the corresponding enum classes.
     * E.g.: "♦J"
     */
    override fun toString(): String{
        return suit.toString() + value.toString()
    }
}