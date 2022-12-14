package entity
import kotlin.test.*

/**
 * A test class containing tests for the
 * initialization of a [Game] object, its
 * available setters and the overridden
 * methods equals() and hashCode().
 */
class GameTest {

    /**
     * Tests if the constructor of the [Game] class
     * initializes an object with a copy of the
     * passed players array and expected attribute values
     */
    @Test
    fun gameInitTest() {
        val players = arrayOf(Player("John"), Player("Sansa"), Player("Arya"))
        val game = Game(players)

        assert(game.unusedCards == arrayListOf<Card>())
        assert(game.openCards.contentEquals(Array(3) { _ -> Card() }))
        assert(game.players.contentEquals(players))
        assert(game.players !== players)
        assert(game.passCounter == 0)

        println("gameInitTest() : SUCCESS")
    }

    /**
     * Tests if the available setters of the
     * [Game] class assign the passed values
     * to the attributes correctly
     */
    @Test
    fun gameSetterTest() {
        val game = Game(arrayOf(Player("John"), Player("Sansa"), Player("Arya")))

        val newPassCounter = 3
        game.passCounter = newPassCounter
        assert(game.passCounter == newPassCounter)

        println("gameSetterTest() : SUCCESS")
    }

    /**
     * Tests if the overridden equals() method
     * returns a correct statement on equality
     * of two [Game] objects.
     */
    @Test
    fun gameEqualsTest() {
        val game1 = Game(arrayOf(Player("John"), Player("Sansa"), Player("Arya")))
        val game2 = Game(arrayOf(Player("John"), Player("Sansa"), Player("Arya")))
        val gameNull : Game? = null

        assert(game1 != gameNull)
        assert(game1 == game1)
        assert(!game1.equals("game1"))
        assertEquals(game1, game2)

        /**
         * Tests equality if one object's
         * unused cards are changed
         */
        fun changeUnusedCards(game : Game) {
            game.unusedCards.add(Card())
            assert(game1 != game)
        }
        changeUnusedCards(game2.copy())

        /**
         * Tests equality if one object's
         * open cards are changed
         */
        fun changeOpenCards(game : Game) {
            game.openCards[0] = Card(CardSuit.CLUBS, CardValue.EIGHT)
            assert(game1 != game)
        }
        changeOpenCards(game2.copy())

        /**
         * Tests equality if one object's
         * players are changed
         */
        fun changePlayers(game : Game) {
            game.players[0] = Player("Mary")
            assert(game1 != game)
        }
        changePlayers(game2.copy())

        /**
         * Tests equality if one object's
         * pass counter is changed
         */
        fun changePassCounter(game : Game) {
            game.passCounter += 1
            assert(game1 != game)
        }
        changePassCounter(game2.copy())

        println("gameEqualsTest() : SUCCESS")
    }

    /**
     * Tests if the overridden hashCode() method
     * calculates equal hash codes for equal
     * [Game] objects.
     */
    @Test
    fun gameHashCodeTest() {
        val game1 = Game(arrayOf(Player("John"), Player("Sansa"), Player("Arya")))
        val game2 = Game(arrayOf(Player("John"), Player("Sansa"), Player("Arya")))

        assertEquals(game1.hashCode(), game1.hashCode())
        assertEquals(game1.hashCode(), game2.hashCode())

        println("gameHashCodeTest() : SUCCESS")
    }

}