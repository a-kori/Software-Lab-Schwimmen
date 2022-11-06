package entity
import kotlin.test.*

/**
 * A test class containing tests for
 * the initialization of a Card object
 */
class CardTest {

    /**
     * Tests if the primary constructor of
     * the Card class initializes an object
     * with the passed suit and value
     */
    @Test
    fun primaryCardConstructorTest() {
        val card = Card(CardSuit.DIAMONDS, CardValue.ACE)

        assert(card.suit == CardSuit.DIAMONDS)
        assert(card.value == CardValue.ACE)

        println("primaryConstructorTest() : SUCCESS")
    }

    /**
     * Tests if the secondary constructor of
     * the Card class initializes an object
     * with the clubs suit and the value 7,
     * as per default
     */
    @Test
    fun secondaryCardConstructorTest() {
        val card = Card()
        assert(card.suit == CardSuit.CLUBS)
        assert(card.value == CardValue.SEVEN)

        println("secondaryConstructorTest() : SUCCESS")
    }

    /**
     * Tests if toString() method
     * returns an expected string
     * to represent a card
     */
    @Test
    fun cardStringTest() {
        val suit = CardSuit.CLUBS
        val value = CardValue.SEVEN

        val card = Card(suit, value)
        assert(card.toString() == suit.toString() + value.toString())

        println("cardStringTest() : SUCCESS")
    }

}