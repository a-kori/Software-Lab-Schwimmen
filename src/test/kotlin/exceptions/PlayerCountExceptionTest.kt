package exceptions
import kotlin.test.*

/**
 * A test class containing a test for
 * throwing of a [PlayerCountException]
 */
class PlayerCountExceptionTest {

    /**
     * Tests if the constructor of the
     * [PlayerCountException] class throws an
     * exception with the exact passed message
     */
    @Test
    fun playerCountThrowTest() {
        val message = "This message should be assigned to the exception."

        assertFailsWith<PlayerCountException>(message) {
            throw PlayerCountException(message)
        }

        println("playerCountThrowTest() : SUCCESS")
    }

}
