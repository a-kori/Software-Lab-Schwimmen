package entity

/**
 * Data class representing a card game of Schwimmen and displaying its current state.
 */
data class Game(private val newPlayers : Array<Player>){
    /**
     * The stack of unused cards, which initially contains all
     * the cards available in the game. At the start of the game,
     * cards from [unusedCards] will be distributed between the players
     * (3 cards per player), and 3 other cards will be relocated to
     * the [openCards] stack.
     * [unusedCards] can also be used to refill [openCards].
     */
    var unusedCards: ArrayList<Card> = arrayListOf()
    init {
        for ( suit in CardSuit.getAllSuits() ) {
            for ( value in CardValue.getAllValuesReduced() ) {
                unusedCards.add( Card(suit, value) )
            }
        }
    }

    /**
     * The three open cards on the table that players can
     * exchange their cards with (see PlayerService for more).
     * The initial value is an array of default cards that will be
     * overwritten at the start of the game.
     */
    var openCards = Array<Card>(3) { _ -> Card() }

    /**
     * An immutable array of 2 to 4 players, participating in
     * the game. Is initialized with the value of [newPlayers].
     * At the end of the game, the array will be sorted in the
     * order that the players will appear on the winners board in.
     */
    val players: Array<Player> = newPlayers

    /**
     * Used to count how many players in a row have 'passed',
     * i.e. skipped their turn in the game. When the counter
     * reaches 3, the card stack on the table ([openCards]) is
     * refreshed with cards from the [unusedCards] stack, or,
     * if there are no cards left in [unusedCards], the game is
     * finished (see GameService for more).
     * The initial value is always zero.
     */
    var passCounter: Int = 0
}
