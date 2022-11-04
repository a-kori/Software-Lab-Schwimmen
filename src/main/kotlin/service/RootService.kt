package service
import entity.Game

/**
 * A central service class, providing access to the [currentGame],
 * the current [playerIndex], as well as the methods
 * from [gameService] and [playerService] classes.
 */
class RootService () {
    /**
     * The current game, being played. The initial value
     * is a new game with an empty list of players, which
     * will be overwritten at the start of the game.
     */
    var currentGame : Game = Game(emptyArray())

    /**
     * The index of a player in the game's player list,
     * whose turn it is to choose a game action. The
     * initial value is 0 (it's the first player's turn).
     */
    var playerIndex : Int = 0

    /**
     * The [GameService] of the current game, where the
     * logic of the game's flow control is described.
     */
    val gameService : GameService = GameService(this)

    /**
     * The [PlayerService] of the current game, where the
     * logic of processing players' actions is described.
     */
    val playerService : PlayerService = PlayerService(this)
}