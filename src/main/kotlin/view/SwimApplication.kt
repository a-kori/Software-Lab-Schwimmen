package view
import service.RootService
import tools.aqua.bgw.core.BoardGameApplication

/**
 * Implementation of the BGW [BoardGameApplication] for the card game "Schwimmen"
 */
class SwimApplication : BoardGameApplication("Schwimmen"), Refreshable {

    // Central service from which all others are created /
    // accessed, holds the game being currently played
    private val rootService = RootService()
    private var newGameScene: SwimNewGameScene? = null
    private var gameScene: SwimGameScene? = null
    private var gameOverScene: SwimGameOverScene? = null

    init {
        // This is only done so that the blurred background when
        // showing the new game menu has content and looks nicer
        rootService.gameService.startGame(listOf("John", "Arya", "Sansa", "Brandon"))

        // This menu scene is shown after application start and if the "new game" button
        // is clicked in the gameOverScene
        newGameScene = SwimNewGameScene(rootService).apply {
            quitButton.onMouseClicked = {
                exit()
            }
        }

        // This is where the actual game takes place
        gameScene = SwimGameScene(rootService)

        // This menu scene is shown after each finished game
        gameOverScene = SwimGameOverScene(rootService).apply {
            newGameButton.onMouseClicked = {
                showMenuScene(newGameScene!!)
            }
            quitButton.onMouseClicked = {
                exit()
            }
        }

        // all scenes and the application itself need to
        // react to changes done in the service layer
        rootService.gameService.addRefreshable(this)
        rootService.gameService.addRefreshable(gameScene!!)
        rootService.gameService.addRefreshable(gameOverScene!!)
        rootService.gameService.addRefreshable(newGameScene!!)

        // Show the new game menu scene
        this.showGameScene(gameScene!!)
        this.showMenuScene(newGameScene!!, 0)
    }

    override fun refreshAfterStartNewGame() {
        gameScene = SwimGameScene(rootService)
        rootService.gameService.replaceRefreshableAt(1, gameScene!!)

        this.showGameScene(gameScene!!)
        this.hideMenuScene()
    }

    override fun refreshAfterGameOver() {
        this.showMenuScene(gameOverScene!!)
    }

}