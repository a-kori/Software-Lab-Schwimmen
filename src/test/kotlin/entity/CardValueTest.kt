package entity
import kotlin.test.*

/**
 * A test class containing tests for
 * the methods of CardValue class
 */
class CardValueTest {

    /**
     * Tests if getAllValuesReduced() method
     * returns an expected full reduced set
     * of card values.
     */
    @Test
    fun valuesFullTest() {
        val values = CardValue.getAllValuesFull()
        val expectedValues = listOf(CardValue.TWO, CardValue.THREE, CardValue.FOUR, CardValue.FIVE, CardValue.SIX,
            CardValue.SEVEN, CardValue.EIGHT, CardValue.NINE, CardValue.TEN,
            CardValue.JACK, CardValue.QUEEN, CardValue.KING, CardValue.ACE).toSet()

        assert(values == expectedValues)
        println("valuesFullTest() : SUCCESS")
    }

    /**
     * Tests if getAllValuesReduced() method
     * returns an expected reduced set of
     * card values.
     */
    @Test
    fun valuesReducedTest() {
        val values = CardValue.getAllValuesReduced()
        val expectedValues = listOf(CardValue.SEVEN, CardValue.EIGHT, CardValue.NINE, CardValue.TEN,
            CardValue.JACK, CardValue.QUEEN, CardValue.KING, CardValue.ACE).toSet()

        assert(values == expectedValues)
        println("valuesReducedTest() : SUCCESS")
    }

    /**
     * Tests if toString() method
     * returns expected strings
     * to represent a card value
     */
    @Test
    fun valueStringsTest() {
        val values = CardValue.getAllValuesFull()
        var valuesString = ""
        for (elem in values) {
            valuesString += elem.toString()
        }
        assert(valuesString == "2345678910JQKA")
        println("valueStringsTest() : SUCCESS")
    }

    /**
     * Tests if numeric() method
     * returns expected integers
     * to represent a card value
     */
    @Test
    fun valueNumericTest() {
        val values = CardValue.getAllValuesFull()
        var i = 2
        for (elem in values) {
            if (elem == CardValue.JACK || elem == CardValue.QUEEN || elem == CardValue.KING) {
                assert(elem.numeric() == 10)
            }
            else {
                assert(elem.numeric() == i)
                i += 1
            }
        }
        println("valueNumericTest() : SUCCESS")
    }

}