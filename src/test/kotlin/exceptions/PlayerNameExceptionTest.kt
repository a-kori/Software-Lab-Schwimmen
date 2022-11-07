package exceptions
import kotlin.test.*

/**
 * A test class containing a test for
 * throwing of a [PlayerNameException]
 */
class PlayerNameExceptionTest {

    /**
     * Tests if the constructor of the
     * [PlayerNameException] class throws an
     * exception with the exact passed message
     */
    @Test
    fun playerNameThrowTest() {
        val message = "This message should be assigned to the exception."

        assertFailsWith<PlayerNameException>(message) {
            throw PlayerNameException(message)
        }

        println("playerNameThrowTest() : SUCCESS")
    }

}
