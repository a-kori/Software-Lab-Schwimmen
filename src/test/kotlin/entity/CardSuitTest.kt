package entity
import kotlin.test.*

/**
 * A test class containing tests for
 * the methods of CardSuit class
 */
class CardSuitTest {

    /**
     * Tests if toString() method
     * returns expected strings
     * to represent a card suit
     */
    @Test
    fun suitStringsTest() {
        val suits = CardSuit.getAllSuits()
        var suitsString = ""
        for (elem in suits) {
            suitsString += elem.toString()
        }
        assert(suitsString == "♣♠♥♦")
        println("suitStringsTest() : SUCCESS")
    }

}