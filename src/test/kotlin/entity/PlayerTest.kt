package entity
import kotlin.test.*

/**
 * A test class containing tests for
 * the initialization of a Player object,
 * as well as its available setters.
 */
class PlayerTest {

    /**
     * Tests if the constructor of the Player
     * class initializes an object with the
     * passed name and expected attribute values
     */
    @Test
    fun playerInitTest() {
        val name = "Bob"
        val player = Player(name)

        assert(player.name == name)
        assert(player.score == 0.0)
        assert(!player.hasKnocked)
        assert(player.cards.contentEquals(Array(3) { _ -> Card() }))

        println("playerInitTest() : SUCCESS")
    }

    /**
     * Tests if the available setters of the
     * Player class assign passed values to
     * the attributes correctly
     */
    @Test
    fun playerSetterTest() {
        val player = Player("Bob")

        val newScore = 30.5
        player.score = newScore
        assert(player.score == newScore)

        player.hasKnocked = true
        assert(player.hasKnocked)

        println("playerSetterTest() : SUCCESS")
    }

}