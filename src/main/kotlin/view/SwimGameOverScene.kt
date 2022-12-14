package view
import entity.*
import service.RootService

import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color

/**
 * [MenuScene] that is displayed when the game is finished. It shows the final result of the game
 * as well as the players' scores. Also, there are three buttons: one for starting a new game, one
 * for restarting the game with the same players and one for quitting the program.
 */
class SwimGameOverScene(private val rootService: RootService) : MenuScene(400, 1080), Refreshable {

    private val headlineLabel = Label(
        width = 300, height = 50, posX = 55, posY = 50,
        text = "Game Over!",
        font = Font(size = 30, fontWeight = Font.FontWeight.BOLD)
    )

    private val p1Score = Label(width = 300, height = 35, posX = 55, posY = 125,
        font = Font(fontWeight = Font.FontWeight.BOLD))
    private val p2Score = Label(width = 300, height = 35, posX = 55, posY = 170,
        font = Font(fontWeight = Font.FontWeight.BOLD))
    private val p3Score = Label(width = 300, height = 35, posX = 55, posY = 215,
        font = Font(fontWeight = Font.FontWeight.BOLD))
    private val p4Score = Label(width = 300, height = 35, posX = 55, posY = 260,
        font = Font(fontWeight = Font.FontWeight.BOLD))

    private val gameResult = Label(width = 350, height = 35, posX = 30, posY = 305,
        font = Font(size = 18, fontWeight = Font.FontWeight.BOLD))

    private val restartButton = Button(width = 90, height = 35, posX = 65, posY = 375, text = "Restart").apply {
        visual = ColorVisual(Color(136, 221, 136))
        onMouseClicked = {
            val playerNames = mutableListOf<String>()
            for (player in rootService.currentGame.players) {
                playerNames.add(player.name)
            }
            rootService.gameService.startGame(playerNames)
        }
    }

    val quitButton = Button(width = 60, height = 35, posX = restartButton.posX + 100, posY = restartButton.posY,
        text = "Quit").apply {
        visual = ColorVisual(Color(221,136,136))
    }

    val newGameButton = Button(width = 120, height = 35, posX = quitButton.posX + 70, posY = quitButton.posY,
        text = "New Game").apply {
        visual = ColorVisual(Color(136, 221, 136))
    }

    init {
        opacity = .5
        addComponents(headlineLabel, p1Score, p2Score, p3Score, p4Score,
            gameResult, restartButton, quitButton, newGameButton)
    }

    private fun scoreString(player : Player): String {
        return "${player.name} scored ${player.score} points."
    }

    override fun refreshAfterGameOver() {
        val game = rootService.currentGame

        p1Score.text = scoreString(game.players[0])
        p2Score.text = scoreString(game.players[1])
        if (game.players.size >= 3)
            p3Score.text = scoreString(game.players[2])
        if (game.players.size == 4)
            p4Score.text = scoreString(game.players[3])

        val winners = mutableListOf(game.players[0].name)
        for (i in 1 until game.players.size) {
            if (game.players[i].score == game.players[0].score)
                winners.add(game.players[i].name)
            else break
        }

        var winnerString = winners[0]
        for (i in 1 until winners.size) {
            winnerString += if (i < winners.size - 1)
                ", ${winners[i]}" else " and ${winners[i]}"
        }

        gameResult.text = when {
            winners.size > 1 -> "$winnerString win the game!"
            else -> "$winnerString wins the game!"
        }
    }

}