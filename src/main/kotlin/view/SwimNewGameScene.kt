package view

import service.RootService
import exceptions.*

import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color

/**
 * [MenuScene] that is used for starting a new game. It is displayed directly at program start or reached
 * when "new game" is clicked in [SwimGameOverScene]. After providing the names of both players,
 * [startButton] can be pressed. There is also a [quitButton] to end the program.
 */
class SwimNewGameScene(private val rootService: RootService) : MenuScene(400, 1080), Refreshable {

    private val headlineLabel = Label(
        width = 300, height = 50,
        posX = 50, posY = 50,
        text = "New Game",
        font = Font(size = 22)
    )

    // Checks if the user has entered less than
    // 2 player names in the available 4 fields
    private fun lessThanTwoPlayers(): Boolean {
        val fields = listOf(p1Input, p2Input, p3Input, p4Input)
        var counter = 0

        for (field in fields) {
            if (field.text.isNotBlank()) counter++
            if (counter >= 2) return false
        }
        return true
    }

    // Returns a list of non-empty field texts with player names
    private fun nonEmptyFieldTexts() : List<String> {
        if (lessThanTwoPlayers())
            throw PlayerCountException("The minimal number of players (2) is not reached!")

        val fields = listOf(p1Input, p2Input, p3Input, p4Input)
        val nonEmpty = mutableListOf<String>()

        for (field in fields) {
            if (field.text.isNotBlank())
                nonEmpty.add(field.text.trim())
        }

        return nonEmpty.toList()
    }

    // Player 1

    private val p1Label = Label(
        width = 100, height = 35,
        posX = 50, posY = 125,
        text = "Player 1:"
    )
    // type inference fails here, so explicit  ": TextField" is required
    // see https://discuss.kotlinlang.org/t/unexpected-type-checking-recursive-problem/6203/14
    private val p1Input: TextField = TextField(
        width = 200, height = 35,
        posX = 150, posY = 125,
        text = listOf("John", "Arya", "Sansa", "Brandon", "Robb", "Rickon").random()
    ).apply {
        onKeyTyped = {
            startButton.isDisabled = lessThanTwoPlayers()
        }
    }

    // Player 2

    private val p2Label = Label(
        width = 100, height = 35,
        posX = 50, posY = 170,
        text = "Player 2:"
    )
    private val p2Input: TextField = TextField(
        width = 200, height = 35,
        posX = 150, posY = 170,
        text = listOf("Rhaenyra", "Daemon", "Viserys", "Alicent", "Otto", "Rhaenys").random()
    ).apply {
        onKeyTyped = {
            startButton.isDisabled = lessThanTwoPlayers()
        }
    }

    // Player 3

    private val p3Label = Label(
        width = 100, height = 35,
        posX = 50, posY = 215,
        text = "Player 3:"
    )
    private val p3Input: TextField = TextField(
        width = 200, height = 35,
        posX = 150, posY = 215,
        text = ""
    ).apply {
        onKeyTyped = {
            startButton.isDisabled = lessThanTwoPlayers()
        }
    }

    // Player 4

    private val p4Label = Label(
        width = 100, height = 35,
        posX = 50, posY = 260,
        text = "Player 4:"
    )
    private val p4Input: TextField = TextField(
        width = 200, height = 35,
        posX = 150, posY = 260,
        text = ""
    ).apply {
        onKeyTyped = {
            startButton.isDisabled = lessThanTwoPlayers()
        }
    }

    val quitButton = Button(
        width = 140, height = 35,
        posX = 50, posY = 310,
        text = "Quit"
    ).apply {
        visual = ColorVisual(Color(221, 136, 136))
    }

    private val startButton = Button(
        width = 140, height = 35,
        posX = 210, posY = 310,
        text = "Start"
    ).apply {
        visual = ColorVisual(Color(136, 221, 136))
        onMouseClicked = {
            rootService.gameService.startGame(nonEmptyFieldTexts())
        }
    }

    init {
        opacity = .5
        addComponents(
            headlineLabel,
            p1Label, p1Input,
            p2Label, p2Input,
            p3Label, p3Input,
            p4Label, p4Input,
            startButton, quitButton
        )
    }

}