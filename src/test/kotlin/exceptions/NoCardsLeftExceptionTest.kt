package exceptions
import kotlin.test.*

/**
 * A test class containing a test for
 * throwing of a [NoCardsLeftException]
 */
class NoCardsLeftExceptionTest {

    /**
     * Tests if the constructor of the
     * [NoCardsLeftException] class throws an
     * exception with the exact passed message
     */
    @Test
    fun noCardsLeftThrowTest() {
        val message = "This message should be assigned to the exception."

        assertFailsWith<NoCardsLeftException>(message) {
            throw NoCardsLeftException(message)
        }

        println("noCardsLeftThrowTest() : SUCCESS")
    }

}