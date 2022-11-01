package entity
import kotlin.test.*
import entity.Game

class EntityLayerTests {
    private var game: Game? = null
    private val playerNames = arrayListOf("John", "Sansa", "Arya", "Brandon")

    /**
     * Full test of the game object initialization
     * and correct attribute assignment
     */
    @Test
    fun mainInitializationTest() {
        initializationTest()
        initAttributesTest()
    }

    /**
     * Test of the game object initialization
     * with different numbers of players
     */
    private fun initializationTest() {
        // Test initialization with too few players
        initializeTooFewPlayers()
        // Test initialization with too many players
        initializeTooManyPlayers()
        // Test initialization with an acceptable number of players
        initializeCorrectGame()
    }

    /**
     * Test of correct attribute assignment
     */
    private fun initAttributesTest() {
        // Test if the initial passCounter value is correct
        checkIfPassCounterInitCorrect()
        // Test if the initial openCards values are correct
        checkIfOpenCardsInitCorrect()
        // Test if the initial unusedCards values are correct
        checkIfUnusedCardsInitCorrect()
        // Test if the player initialization is correct
        checkIfPlayersInitCorrect()
    }

    /**
     * Test of the game object initialization
     * with an insufficient number of players
     */
    private fun initializeTooFewPlayers() {
        val tooFewPlayers = arrayListOf("John")
        assertFails { game = Game(tooFewPlayers) }
        println("Initialization with tooFewPlayers successfully failed.")

        assertNull(game)
        println("Failed initialization left newGame unchanged.")
    }

    /**
     * Test of the game object initialization
     * with an exceeding number of players
     */
    private fun initializeTooManyPlayers() {
        val tooManyPlayers = arrayListOf("John", "Sansa", "Arya", "Brandon", "Katelyn")
        assertFails { game = Game(tooManyPlayers) }
        println("Initialization with tooManyPlayers successfully failed.")

        assertNull(game)
        println("Failed initialization left newGame unchanged.")
    }

    /**
     * Test of the game object initialization
     * with an acceptable number of players
     */
    private fun initializeCorrectGame() {
        game = Game(playerNames)
        assertNotNull(game)
        println("game successfully initialized with playerNames.")
    }

    /**
     * Test if [passCounter] is set to zero
     */
    private fun checkIfPassCounterInitCorrect() {
        assert(game!!.passCounter == 0)
        println("passCounter == 0")
    }

    /**
     * Test if [openCards] is set to an array of nulls of size 3
     */
    private fun checkIfOpenCardsInitCorrect() {
        assert(game!!.openCards.size == 3)

        for (card in game!!.openCards) {
            assertNull(card)
        }
        println("openCards == arrayOfNulls<Card>(3)")
    }

    /**
     * Test if [unusedCards] is set to a list of
     * size 32 with all possible playing cards
     */
    private fun checkIfUnusedCardsInitCorrect() {
        assert(game!!.unusedCards.size == 32)
        println("unusedCards.size == 32")

        var availableCards = arrayListOf<Card>()
        for ( suit in CardSuit.getAllSuits() ) {
            for ( value in CardValue.getAllValuesReduced() ) {
                availableCards.add(Card(suit, value))
            }
        }
        assertEquals(availableCards, game!!.unusedCards)
        println("all 32 combinations included in unusedCards")
    }

    /**
     * Test if [players] is set to a list of new players with
     * names from playerNames in the correct order and with
     * correctly initialized attributes.
     */
    private fun checkIfPlayersInitCorrect() {
        var actualPlayers = ArrayList<String>()
        for (player in game!!.players) {
            actualPlayers.add(player.name)
            checkPlayerAttributes(player)
        }

        assertEquals(playerNames, actualPlayers)
        println("all ${actualPlayers.size} players included in correct order")
    }

    /**
     * Test if the given player was initialized with correct attributes.
     */
    private fun checkPlayerAttributes(player : Player) {
        assert(player.score == 0.0)
        assert(!player.hasKnocked)
        assert(player.cards.size == 3)
        for (card in player.cards) {
            assertNull(card)
        }
        println("All attributes of player ${player.name} are initialized correctly.")
    }

}