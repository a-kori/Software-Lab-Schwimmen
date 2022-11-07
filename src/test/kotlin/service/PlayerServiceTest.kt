package service
import entity.*
import kotlin.test.*

/**
 * A test class containing tests for
 * the methods of [PlayerService] class.
 */
class PlayerServiceTest {

    /**
     * Tests if the knock() method
     * correctly marks down that the
     * player has knocked and moves
     * to the next player.
     */
    @Test
    fun knockTest() {
        val rs = RootService()

        val playerNames = listOf("John", "Sansa", "Arya")
        rs.gameService.startGame(playerNames)
        val game = rs.currentGame
        game.passCounter = 5

        assert(rs.playerIndex == 0)
        rs.playerService.knock(game.players[rs.playerIndex])

        assert(rs.playerIndex == 1)
        assert(game.players[rs.playerIndex - 1].hasKnocked)
        assert(game.passCounter == 0)

        println("knockTest() : SUCCESS")
    }

    /**
     * Tests if the exchangeOneCard() method
     * correctly swaps a card from the player's
     * hand and an open card of their choice.
     */
    @Test
    fun exchangeOneCardTest() {
        val rs = RootService()

        val playerNames = listOf("John", "Sansa", "Arya")
        rs.gameService.startGame(playerNames)
        val game = rs.currentGame
        game.passCounter = 5

        rs.playerIndex = 2
        val playerCard = game.players[2].cards[0]
        val openCard = game.openCards[1]

        rs.playerService.exchangeOneCard(game.players[2], 0, 1)
        assert(game.players[2].cards[0] == openCard)
        assert(game.openCards[1] == playerCard)

        assert(rs.playerIndex == 0)
        assert(game.passCounter == 0)

        // Checks if updateScore() was called
        val scoreClone = Player("")
        for (i in 0 until game.players[2].cards.size) {
            scoreClone.cards[i] = game.players[2].cards[i]
        }
        rs.playerService.updateScore(scoreClone)
        assert(scoreClone.score == game.players[2].score)

        println("exchangeOneCardTest() : SUCCESS")
    }

    /**
     * Tests if the exchangeAllCards() method
     * correctly swaps cards from the player's
     * hand and open cards on the table.
     */
    @Test
    fun exchangeAllCardsTest() {
        val rs = RootService()

        val playerNames = listOf("John", "Sansa", "Arya")
        rs.gameService.startGame(playerNames)
        val game = rs.currentGame
        game.passCounter = 5

        rs.playerIndex = 1
        val playerCards = game.players[1].cards.clone()
        val openCards = game.openCards.clone()

        rs.playerService.exchangeAllCards(game.players[1])
        assert(game.players[1].cards.contentEquals(openCards))
        assert(game.openCards.contentEquals(playerCards))

        assert(rs.playerIndex == 2)
        assert(game.passCounter == 0)

        // Checks if updateScore() was called
        val scoreClone = Player("")
        for (i in 0 until game.players[1].cards.size) {
            scoreClone.cards[i] = game.players[1].cards[i]
        }
        rs.playerService.updateScore(scoreClone)
        assert(scoreClone.score == game.players[1].score)

        println("exchangeAllCardsTest() : SUCCESS")
    }

    /**
     * Tests if the pass() method correctly
     * lets the player skip their turn and
     * correctly reacts when passCounter
     * reaches the number of players.
     */
    @Test
    fun passTest() {
        val rs = RootService()

        val playerNames = listOf("John", "Sansa", "Arya")
        rs.gameService.startGame(playerNames)
        val game = rs.currentGame

        // 20 cards available
        val toBeAssigned = arrayOf(game.unusedCards[0], game.unusedCards[1], game.unusedCards[2])
        for (i in game.players.indices) {
            rs.playerService.pass()

            if (i != game.players.size - 1)  {
                assert(rs.playerIndex == i + 1)
                assert(game.passCounter == i + 1)
            }
        }
        assert(rs.playerIndex == 0)
        assert(game.passCounter == 0)
        assert(game.openCards.contentEquals(toBeAssigned))

        // 17 cards available
        for (i in 1..15) {
            rs.currentGame.unusedCards.removeLast()
        }

        // 2 cards available
        val winners = game.players.clone()
        winners.sortByDescending { player -> player.score }

        for (i in game.players.indices) {
            rs.playerService.pass()
        }
        assert(rs.playerIndex == 2)
        assert(game.passCounter == 3)
        assert(game.players.contentEquals(winners))

        println("passTest() : SUCCESS")
    }

    /**
     * Tests if updateScore() correctly
     * recalculates the passed
     * player's current score.
     */
    @Test
    fun updateScoreTest() {
        val rs = RootService()

        val playerNames = listOf("John", "Sansa", "Arya")
        rs.gameService.startGame(playerNames)
        val game = rs.currentGame

        // All values same
        val cardSuits = CardSuit.getAllSuits().toMutableList()
        for (i in 0..2) {
            game.players[0].cards[i] = Card(cardSuits.last(), CardValue.JACK)
            cardSuits.removeLast()
        }
        rs.playerService.updateScore(game.players[0])
        assert(game.players[0].score == 30.5)

        // All suits different, different values
        game.players[0].cards[2] = Card(CardSuit.CLUBS, CardValue.SEVEN)
        rs.playerService.updateScore(game.players[0])
        assert(game.players[0].score == 10.0)

        // Two same suits
        game.players[0].cards[1] = Card(CardSuit.CLUBS, CardValue.JACK)
        rs.playerService.updateScore(game.players[0])
        assert(game.players[0].score == 17.0)

        // Three same suits
        game.players[0].cards[0] = Card(CardSuit.CLUBS, CardValue.JACK)
        rs.playerService.updateScore(game.players[0])
        assert(game.players[0].score == 27.0)

        println("updateScoreTest() : SUCCESS")
    }

    /**
     * Tests if the overridden equals() method
     * returns a correct statement on equality
     * of two [PlayerService] objects.
     */
    @Test
    fun playerServiceEqualsTest() {
        val rs = RootService()

        val playerService1 = PlayerService(rs)
        val playerService2 = PlayerService(rs)
        val playerService3 = PlayerService(RootService())
        val playerServiceNull : PlayerService? = null

        assert(playerService1 != playerServiceNull)
        assert(playerService1 == playerService1)
        assert(!playerService1.equals("gameService1"))

        assert(playerService1 != playerService3)
        assert(playerService1 == playerService2)

        println("playerServiceEqualsTest() : SUCCESS")
    }

    /**
     * Tests if the overridden hashCode() method
     * calculates equal hash codes for equal
     * [PlayerService] objects.
     */
    @Test
    fun playerServiceHashCodeTest() {
        val rs = RootService()
        val playerService1 = PlayerService(rs)
        val playerService2 = PlayerService(rs)

        assertEquals(playerService1.hashCode(), playerService1.hashCode())
        assertEquals(playerService1.hashCode(), playerService2.hashCode())

        println("playerServiceHashCodeTest() : SUCCESS")
    }

}