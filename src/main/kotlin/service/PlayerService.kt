package service
import entity.Player

/**
 * A service class, used to describe the
 * logic of processing players' actions.
*/
class PlayerService (private val rs : RootService) : AbstractRefreshingService() {

    /**
     * Marks down that the player [hasKnocked]
     * and moves to the next player.
     */
    fun knock (player : Player) {
        player.hasKnocked = true

        rs.currentGame.passCounter = 0
        rs.gameService.nextPlayer()
    }

    /**
     * Swaps a card from the player's hand
     * and an open card of their choice,
     * updates the player's score and moves
     * to the next player.
     */
    fun exchangeOneCard (player : Player, playerCardIndex : Int, openCardIndex : Int) {
        val game = rs.currentGame

        val temp = player.cards[playerCardIndex].copy()
        player.cards[playerCardIndex] = game.openCards[openCardIndex].copy()
        game.openCards[openCardIndex] = temp

        updateScore(player)
        game.passCounter = 0
        rs.gameService.nextPlayer()
    }

    /**
     * Swaps cards from the player's hand
     * and the open cards on the table,
     * updates the player's score and moves
     * to the next player.
     */
    fun exchangeAllCards(player : Player){
        val game = rs.currentGame

        val temp = game.openCards.clone()
        game.openCards = player.cards.clone()
        player.cards = temp

        updateScore(player)
        game.passCounter = 0
        rs.gameService.nextPlayer()
    }

    /**
     * Lets the current player skip their turn
     * and refills the open cards stack with
     * new cards, if all players have skipped
     * their turns in a row. If there are no new
     * cards available, the game is finished.
     */
    fun pass() {
        val game = rs.currentGame
        game.passCounter += 1

        if (game.passCounter == game.players.size) {
            if (!rs.gameService.enoughCardsLeft()) {
                rs.gameService.endGame()
                return
            }
            rs.gameService.renewOpenCards()
            game.passCounter = 0
        }
        rs.gameService.nextPlayer()
    }

    /**
     * Recalculates the passed
     * player's current score.
     */
    private fun updateScore (player : Player){
        // If all three cards have different suits
        if (player.cards == player.cards.distinctBy { card -> card.suit }) {

            // and the same values, set the score to 30.5.
            if (player.cards.distinctBy { card -> card.value }.size == 1)
                player.score = 30.5

            // Else set the score to the largest value among the cards.
            else {
                player.score = player.cards.maxOf{ card -> card.value.numeric() }.toDouble()
            }
            return
        }

        // If there are at least 2 cards with the same suit,
        // add up the values of cards with the same suit and
        // set the score to the largest sum.
        val sums = IntArray(4) { 0 }
        for (card in player.cards) {
            sums[card.suit.ordinal] += card.value.numeric()
        }
        player.score = sums.maxOrNull()!!.toDouble()!!
    }
}