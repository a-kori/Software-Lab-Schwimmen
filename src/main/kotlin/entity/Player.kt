package entity

/**
 * Data class representing a game's player with a given, immutable name,
 * a variable score,
 */
data class Player(private val player_name: String) {
    /**
     * The player's name given to the constructor.
     */
    val name: String = player_name

    /**
     * Numerical value used to compare the player's
     * performance in the game with that of others.
     * Based on the suits and values of the player's
     * current cards (see [PlayerService].updateScore()
     * for more). The initial value is always zero.
     */
    var score: Double = 0.0

    /**
     * Used to indicate if the player has once knocked in the
     * course of the game. The initial value is always false.
     */
    var hasKnocked: Boolean = false

    /**
     * The three cards that the player currently has on the hand, that
     * determine his current score. The initial value is an array of nulls
     * that will be overwritten with [Card] objects at the start of the game.
     */
    var cards = arrayOfNulls<Card>(3)
}