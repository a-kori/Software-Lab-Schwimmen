package service
import entity.*
import exceptions.NoCardsLeftException
import exceptions.PlayerCountException
import exceptions.PlayerNameException
import kotlin.test.*

/**
 * A test class containing tests for
 * the methods of [GameService] class.
 */
class GameServiceTest {

    // TODO: add onAllRefreshables call check, where applicable

    /**
     * Checks if the distribution of cards to
     * the passed [cards] array was correct
     */
    private fun uniqueAndLegal(cards : Array<Card>) : Boolean {
        // Each card is unique
        if (!cards.contentEquals(cards.distinct().toTypedArray())) return false
        // Each card is one of the available in the game
        for (card in cards) {
            if (!CardValue.getAllValuesReduced().contains(card.value)) return false
            if (!CardSuit.getAllSuits().contains(card.suit)) return false
        }
        return true
    }

    /**
     * Tests if the startGame() method
     * correctly initializes the players
     * and distributes cards to unusedCards,
     * openCards and each player's cards
     */
    @Test
    fun startGameTest() {
        val rs = RootService()

        /**
         * Tests startGame() method
         * with possible bad cases
         */
        fun badCasesTest() {
            // Test with too few players
            val tooFewPlayers = listOf("John")
            assertFailsWith<PlayerCountException> ("The minimal number of players (2) is not reached!") {
                rs.gameService.startGame(tooFewPlayers)
            }

            // Test with too many players
            val tooManyPlayers = listOf("John", "Sansa", "Arya", "Brandon", "Geralt")
            assertFailsWith<PlayerCountException> ("The maximal number of players (4) is exceeded!") {
                rs.gameService.startGame(tooManyPlayers)
            }

            // Test with at least one empty / whitespace player name
            val playersWithBlanks1 = listOf("John", "Sansa", "Arya", "")
            assertFailsWith<PlayerNameException> ("One or more of the assigned player names are blank!") {
                rs.gameService.startGame(playersWithBlanks1)
            }
            val playersWithBlanks2 = listOf("John", "Sansa", "Arya", "   ")
            assertFailsWith<PlayerNameException> ("One or more of the assigned player names are blank!") {
                rs.gameService.startGame(playersWithBlanks2)
            }

            // Test with duplicated player names
            val playersWithDuplicates = listOf("John", "Sansa", "Sansa")
            assertFailsWith<PlayerNameException> ("One or more of the assigned player names are duplicated!") {
                rs.gameService.startGame(playersWithDuplicates)
            }
        }
        badCasesTest()

        val playerNames = listOf("John", "Sansa", "Arya")
        rs.gameService.startGame(playerNames)
        val game = rs.currentGame

        assert(uniqueAndLegal(game.openCards))
        val usedCards = game.openCards.toMutableSet()


        val actualNames = arrayListOf<String>()
        for (player in game.players) {
            actualNames.add(player.name)
        }
        assert(actualNames == playerNames)

        for (player in game.players) {
            assert(uniqueAndLegal(player.cards))
            assert(player.score > 0.0)

            for (card in player.cards) {
                assert(!usedCards.contains(card))
                usedCards.add(card)
            }
        }

        assert(game.unusedCards.size == 29 - (3 * playerNames.size))
        assert(uniqueAndLegal(game.unusedCards.toTypedArray()))
        for (card in game.unusedCards) {
            assert(!usedCards.contains(card))
        }

        println("startGameTest() : SUCCESS")
    }

    /**
     * Tests if the enoughCardsLeft() method
     * correctly checks if the size of the
     * draw pile is sufficient to fill
     * the open card stack
     */
    @Test
    fun enoughCardsLeftTest() {
        val rs = RootService()

        val playerNames = listOf("John", "Sansa", "Arya")
        rs.gameService.startGame(playerNames)

        // 20 cards available
        assert(rs.gameService.enoughCardsLeft())

        for (i in 1..17) {
            rs.currentGame.unusedCards.removeLast()
        }
        // 3 cards available
        assert(rs.gameService.enoughCardsLeft())

        // 2 cards available
        rs.currentGame.unusedCards.removeLast()
        assert(!rs.gameService.enoughCardsLeft())

        println("enoughCardsLeftTest() : SUCCESS")
    }

    /**
     * Tests if the renewOpenCards() method
     * correctly refreshes cards in openCards
     * with cards from unusedCards.
     */
    @Test
    fun renewOpenCardsTest () {
        val rs = RootService()

        val playerNames = listOf("John", "Sansa", "Arya")
        rs.gameService.startGame(playerNames)
        val game = rs.currentGame

        // 20 cards available
        for (i in 1..6) {
            val toBeAssigned = arrayOf(game.unusedCards[0], game.unusedCards[1], game.unusedCards[2])
            rs.gameService.renewOpenCards()

            assert(game.openCards.contentEquals(toBeAssigned))

            assert(uniqueAndLegal(game.unusedCards.toTypedArray()))
            for (card in game.unusedCards) {
                assert(!toBeAssigned.toSet().contains(card))
            }
        }
        // 2 cards available
        assertFailsWith<NoCardsLeftException>("There are not " +
                "enough cards in the draw pile to renew the card stack on the table!") {
            rs.gameService.renewOpenCards()
        }

        println("renewOpenCardsTest() : SUCCESS")
    }

    /**
     * Tests if the nextPlayer() method
     * correctly iterates through the
     * players array using playerIndex
     */
    @Test
    fun nextPlayerTest() {
        val rs = RootService()

        val playerNames = listOf("John", "Sansa", "Arya")
        rs.gameService.startGame(playerNames)
        val game = rs.currentGame

        for (i in 0 until game.players.size) {
            assert(rs.playerIndex == i)
            rs.gameService.nextPlayer()
        }
        assert(rs.playerIndex == 0)

        game.players[1].hasKnocked = true
        val winners = game.players.clone()
        winners.sortByDescending { player -> player.score }

        rs.gameService.nextPlayer()
        assert(game.players.contentEquals(winners))

        println("nextPlayerTest() : SUCCESS")
    }

    /**
     * Tests if the endGame() method
     * correctly ends the game by
     * sorting the players array in
     * descending order by their score
     */
    @Test
    fun endGameTest() {
        val rs = RootService()
        val playerNames = listOf("John", "Sansa", "Arya")
        rs.gameService.startGame(playerNames)

        val winners = rs.currentGame.players.clone()
        winners.sortByDescending { player -> player.score }

        rs.gameService.endGame()
        assert(rs.currentGame.players.contentEquals(winners))

        println("endGameTest() : SUCCESS")
    }

    /**
     * Tests if the overridden equals() method
     * returns a correct statement on equality
     * of two [GameService] objects.
     */
    @Test
    fun gameServiceEqualsTest() {
        val rs = RootService()

        val gameService1 = GameService(rs)
        val gameService2 = GameService(rs)
        val gameService3 = GameService(RootService())
        val gameServiceNull : GameService? = null

        assert(gameService1 != gameServiceNull)
        assert(gameService1 == gameService1)
        assert(!gameService1.equals("gameService1"))

        assert(gameService1 != gameService3)
        assert(gameService1 == gameService2)

        gameService2.addRefreshable(view.SwimApplication())
        assert(gameService1 != gameService2)

        println("gameServiceEqualsTest() : SUCCESS")
    }

    /**
     * Tests if the overridden hashCode() method
     * calculates equal hash codes for equal
     * [GameService] objects.
     */
    @Test
    fun gameServiceHashCodeTest() {
        val rs = RootService()
        val gameService1 = GameService(rs)
        val gameService2 = GameService(rs)

        assertEquals(gameService1.hashCode(), gameService1.hashCode())
        assertEquals(gameService1.hashCode(), gameService2.hashCode())

        println("gameServiceHashCodeTest() : SUCCESS")
    }

}