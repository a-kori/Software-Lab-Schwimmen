package entity

/**
 * Data class representing a card game of Schwimmen and displaying its current state.
 */
data class Game(private val playerNames: ArrayList<String>){
    /**
     * Used to count how many players in a row have 'passed',
     * i.e. skipped their turn in the game. When the counter
     * reaches 3, the card stack on the table ([openCards]) is
     * refreshed with cards from the [unusedCards] stack, or,
     * if there are no cards left in [unusedCards], the game is
     * finished (see [GameService] for more).
     * The initial value is always zero.
     */
    var passCounter: Int = 0
        private set
    fun incrementPassCounter() { passCounter++ }

    /**
     * The three open cards on the table that players can
     * exchange for their cards (see [PlayerService] for more).
     * The initial value is an array of nulls that will be
     * overwritten with [Card] objects at the start of the game.
     */
    var openCards = arrayOfNulls<Card>(3)

    /**
     * The stack of unused cards, which initially contains all
     * the cards available in the game. At the start of the game,
     * cards from [unusedCards] will be distributed between the players
     * (3 cards per player), and 3 other cards will be relocated to
     * the [openCards] stack.
     * [unusedCards] can also be used to refresh [openCards] - each time
     * the [passCounter] reaches 3.
     * The initial value is a list of all possible combinations of a
     * card suit and a card value, i.e. a list of 32 different cards.
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
     * An immutable array of 2 to 4 players, participating in
     * the game. Uses the [playerNames] constructor parameter to
     * determine the number of players and their names, with
     * which each player is initialized.
     */
    val players: Array<Player>
    init {
        if (playerNames.size < 2) {
            throw Exception("The minimal number of players (2) is not reached when initializing the game!")
        }
        else if (playerNames.size > 4) {
            throw Exception("The maximal number of players (4) is exceeded when initializing the game!")
        }
        else {
            players = Array(playerNames.size) { i -> Player(playerNames[i]) }
        }
    }
}
