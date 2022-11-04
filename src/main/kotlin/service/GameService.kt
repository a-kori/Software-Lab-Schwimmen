package service
import exceptions.*
import entity.*
import view.Refreshable

/**
 * A service class, used to describe the
 * logic of the game's flow control.
 */
class GameService (private val rs : RootService) : AbstractRefreshingService() {

    /**
     * Initializes a new game, adds a list of
     * players with passed [playerNames] and
     * randomly distributes cards between the
     * players and the card stack on the table.
     */
    fun startGame (playerNames : List<String>) {
        initializePlayers(playerNames)

        rs.currentGame.unusedCards.shuffle()
        distributeCardsToPlayers()
        renewOpenCards()

        onAllRefreshables(Refreshable::refreshAfterStartNewGame)
    }

    /**
     * Initializes a player list with passed
     * [playerNames], if [playerNames] is valid,
     * and initializes a new game with the list.
     */
    private fun initializePlayers (playerNames : List<String>) {
        if (playerNames.size < 2)
            throw PlayerCountException("The minimal number of players (2) is not reached!")

        if (playerNames.size > 4)
            throw PlayerCountException("The maximal number of players (4) is exceeded!")

        for (name in playerNames) {
            if (name.isBlank())
                throw PlayerNameException("One or more of the assigned player names are blank!")
        }
        if (playerNames != playerNames.distinct())
            throw PlayerNameException("One or more of the assigned player names are duplicated!")

        rs.currentGame = Game( Array(playerNames.size) { i -> Player(playerNames[i]) } )
    }

    /**
     * Distributes cards from [unusedCards] between
     * the players, if there are enough cards.
     */
    private fun distributeCardsToPlayers() {
        val game = rs.currentGame
        if (game.unusedCards.size < 3)
            throw NoCardsLeftException("There not enough cards in the draw pile to distribute to players!")

        for (player in game.players) {
            for(i in 0..2) {
                player.cards[i] = game.unusedCards[0]
                game.unusedCards.removeAt(0)
            }
        }
    }

    /**
     * Checks if there are at least 3 cards
     * left in the draw stack ([unusedCards]).
     */
    fun enoughCardsLeft() : Boolean {
        return rs.currentGame.unusedCards.size >= 3
    }

    /**
     * Puts 3 cards from [unusedCards] into the open card
     * stack on the table, if there are enough cards.
     */
    fun renewOpenCards() {
        val game = rs.currentGame
        if (!enoughCardsLeft())
            throw NoCardsLeftException("There not enough cards in the draw pile to renew the card stack on the table!")

        for(i in 0..2) {
            game.openCards[i] = game.unusedCards[0]
            game.unusedCards.removeAt(0)
        }
    }

    /**
     * Sets [playerIndex] to the index of the next player,
     * whose turn it is to choose a game action. But, if
     * this player has knocked in the previous round of
     * the game, the game is finished.
     */
    fun nextPlayer() {
        val game = rs.currentGame

        rs.playerIndex += 1
        if (rs.playerIndex == game.players.size)
            rs.playerIndex = 0

        if (game.players[rs.playerIndex].hasKnocked) {
            endGame()
        }
        else onAllRefreshables(Refreshable::refreshAfterGameTurn)
    }

    /**
     * Ends the game by breaking the game loop and sorting [players]
     * in the descending order as to the scores they achieved in the game.
     */
    fun endGame() {
        rs.currentGame.players.sortByDescending { player -> player.score }
        onAllRefreshables(Refreshable::refreshAfterGameOver)
    }
}