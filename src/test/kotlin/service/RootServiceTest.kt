package service
import entity.Game
import entity.Player
import kotlin.test.*

/**
 * A test class containing tests for
 * the initialization of a [RootService] object,
 * as well as its available setters.
 */
class RootServiceTest {

    /**
     * Tests if the constructor of the
     * [RootService] class initializes
     * an object with expected attribute
     * values
     */
    @Test
    fun rootServiceInitTest() {
        val rs = RootService()

        assertEquals(rs.currentGame, Game(emptyArray()))
        assertEquals(rs.playerIndex, 0)
        assertEquals(rs.gameService, GameService(rs))
        assertEquals(rs.playerService, PlayerService(rs))

        println("rootServiceInitTest() : SUCCESS")
    }

    /**
     * Tests if the available setters of the
     * [RootService] class assign passed values
     * to the attributes correctly
     */
    @Test
    fun rootServiceSetterTest() {
        val rs = RootService()

        val game = Game(arrayOf(Player("John"), Player("Sansa"), Player("Arya")))
        rs.currentGame = game
        assertEquals(rs.currentGame, game)

        val index = 5
        rs.playerIndex = index
        assertEquals(rs.playerIndex, index)

        println("rootServiceSetterTest() : SUCCESS")
    }

}