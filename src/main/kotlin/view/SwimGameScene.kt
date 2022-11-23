package view
import service.RootService
import service.CardImageLoader

import tools.aqua.bgw.util.Font
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

/**
 * This is the main scene for the Schwimmen game.
 * Players are placed at the 4 edges of the screen, the current player is placed at the bottom.
 * Only the current player's cards are shown and are changed accordingly after each player's turn.
 * The current player has four actions of choice: pass, knock, swap one card or swap all cards.
 * After the player has made a choice, the continue button is enabled, which moves the game to the next player.
 */
class SwimGameScene(private val rootService: RootService) : BoardGameScene(1920, 1080), Refreshable {

    private var bottomPlayer = PlayerView(rootService.currentGame.players[0], "bottom")

    private var topPlayer = if (rootService.currentGame.players.size < 3)
        PlayerView(rootService.currentGame.players[1], "top")
        else PlayerView(rootService.currentGame.players[2], "top")

    private var leftPlayer = if (rootService.currentGame.players.size < 3) null
        else PlayerView(rootService.currentGame.players[1], "left")

    private var rightPlayer = if (rootService.currentGame.players.size < 4) null
        else PlayerView(rootService.currentGame.players[3], "right")

    private var score = Label(
        text = "Current score: ${rootService.currentGame.players[0].score}",
        font = Font(size = 22),
        width = 300,
        height = 35,
        posX = 355,
        posY = 780
    )

    private val cardImageLoader = CardImageLoader()
    private val openCards = rootService.currentGame.openCards

    private var middleCard0 = CardView(
        height = 200, width = 130,
        front = ImageVisual(cardImageLoader.frontImageFor(openCards[0].suit, openCards[0].value)),
        back = ImageVisual(cardImageLoader.backImage),
        posX = 710, posY = 400
    )
    private var middleCard1 = CardView(
        height = 200, width = 130,
        front = ImageVisual(cardImageLoader.frontImageFor(openCards[1].suit, openCards[1].value)),
        back = ImageVisual(cardImageLoader.backImage),
        posX = middleCard0.posX + 130, posY = middleCard0.posY
    )
    private var middleCard2 = CardView(
        height = 200, width = 130,
        front = ImageVisual(cardImageLoader.frontImageFor(openCards[2].suit, openCards[2].value)),
        back = ImageVisual(cardImageLoader.backImage),
        posX = middleCard1.posX + 130, posY = middleCard1.posY
    )
    private val prompt = Label(
        font = Font(size = 20),
        width = 700,
        height = 35,
        posX = 560,
        posY = 945
    )


    private val passButton = Button(width = 130, height = 50, posX = 1250, posY = 690,
        font = Font(size = 20), text = "Pass").apply {
        visual = ColorVisual(Color(136, 221, 136))
        onMouseClicked = {
            bottomPlayer.log.text = "has passed."
            rootService.playerService.pass()
        }
        isDisabled = false
    }

    private val knockButton = Button(width = 130, height = 50, posX = passButton.posX, posY = passButton.posY + 55,
        font = Font(size = 20), text = "Knock").apply {
        visual = ColorVisual(Color(136, 221, 136))
        onMouseClicked = {
            rootService.let { rs ->
                bottomPlayer.log.text = "has knocked."
                val currentPlayer = rs.currentGame.players[rs.playerIndex]
                rs.playerService.knock(currentPlayer)
            }
        }
        isDisabled = false
    }

    private val swapOneButton = Button(width = 130, height = 50, posX = passButton.posX, posY = knockButton.posY + 55,
        text = "Swap one card").apply {
        visual = ColorVisual(Color(136, 221, 136))
        onMouseClicked = {
            rootService.let { rs ->

                var playerCardToSwap = -1

                val currentPlayer = rs.currentGame.players[rs.playerIndex]
                val playerCards = listOf(bottomPlayer.card0, bottomPlayer.card1, bottomPlayer.card2)
                val middleCards = listOf(middleCard0, middleCard1, middleCard2)

                for (card in playerCards) {
                    card.onMouseClicked = {
                        playerCardToSwap = playerCards.indexOf(card)
                        for (i in 0..2) {
                            playerCards[i].isDisabled = true
                            middleCards[i].isDisabled = false
                        }
                        prompt.text = "Choose one of the cards in the middle to replace your card with."
                    }
                }
                for (card in middleCards) {
                    card.onMouseClicked = {
                        val middleCardToSwap = middleCards.indexOf(card)
                        for (otherCard in middleCards) {
                            otherCard.isDisabled = true
                        }
                        prompt.text = ""
                        bottomPlayer.log.text = "has swapped one card."
                        rs.playerService.exchangeOneCard(currentPlayer, playerCardToSwap, middleCardToSwap)
                    }
                }
                turnOffButton(passButton)
                turnOffButton(knockButton)
                turnOffButton(this)
                turnOffButton(swapAllButton)
                for (card in playerCards) {
                    card.isDisabled = false
                }
                prompt.text = "Choose one of your cards to swap."
            }
        }
        isDisabled = false
    }

    private val swapAllButton = Button(width = 130, height = 50, posX = passButton.posX, posY = knockButton.posY + 110,
        text = "Swap all cards").apply {
        visual = ColorVisual(Color(136, 221, 136))
        onMouseClicked = {
            rootService.let { rs ->
                bottomPlayer.log.text = "has swapped all cards."
                val currentPlayer = rs.currentGame.players[rs.playerIndex]
                rs.playerService.exchangeAllCards(currentPlayer)
            }
        }
        isDisabled = false
    }

    var continueClicked = false
    private val continueButton = Button(width = 130, height = 50, posX = passButton.posX, posY = swapAllButton.posY + 55,
        font = Font(size = 20), text = "Continue").apply {
        visual = ColorVisual(Color(221,136,136))
        onMouseClicked = {
            rootService.let { rs ->
                continueClicked = true
                movePlayers(rs)

                turnOnButton(passButton)
                turnOnButton(knockButton)
                turnOnButton(swapOneButton)
                turnOnButton(swapAllButton)
                turnOffButton(this)
            }
        }
        isDisabled = true
    }


    private fun turnOffButton (button : Button) {
        button.isDisabled = true
        button.visual = ColorVisual(Color(221,136,136))
    }

    private fun turnOnButton (button : Button) {
        button.isDisabled = false
        button.visual = ColorVisual(Color(136, 221, 136))
    }

    private fun refreshMiddleCards (rs : RootService) {
        val openCards = rs.currentGame.openCards

        middleCard0.isVisible = false
        middleCard0 = CardView(
            height = 200, width = 130,
            front = ImageVisual(cardImageLoader.frontImageFor(openCards[0].suit, openCards[0].value)),
            back = ImageVisual(cardImageLoader.backImage),
            posX = 710, posY = 400
        )
        middleCard1.isVisible = false
        middleCard1 = CardView(
            height = 200, width = 130,
            front = ImageVisual(cardImageLoader.frontImageFor(openCards[1].suit, openCards[1].value)),
            back = ImageVisual(cardImageLoader.backImage),
            posX = middleCard0.posX + 130, posY = middleCard0.posY
        )
        middleCard2.isVisible = false
        middleCard2 = CardView(
            height = 200, width = 130,
            front = ImageVisual(cardImageLoader.frontImageFor(openCards[2].suit, openCards[2].value)),
            back = ImageVisual(cardImageLoader.backImage),
            posX = middleCard1.posX + 130, posY = middleCard1.posY
        )

        middleCard0.flip()
        middleCard1.flip()
        middleCard2.flip()

        addComponents(middleCard0, middleCard1, middleCard2)
    }

    private fun movePlayers (rs : RootService) {
        when (rs.currentGame.players.size) {
            2 -> {
                topPlayer.name.text = bottomPlayer.name.text
                topPlayer.log.text = bottomPlayer.log.text
            }
            3 -> {
                leftPlayer!!.name.text = topPlayer.name.text
                leftPlayer!!.log.text = topPlayer.log.text

                topPlayer.name.text = bottomPlayer.name.text
                topPlayer.log.text = bottomPlayer.log.text
            }
            else -> {
                leftPlayer!!.name.text = topPlayer.name.text
                leftPlayer!!.log.text = topPlayer.log.text

                topPlayer.name.text = rightPlayer!!.name.text
                topPlayer.log.text = rightPlayer!!.log.text

                rightPlayer!!.name.text = bottomPlayer.name.text
                rightPlayer!!.log.text = bottomPlayer.log.text
            }
        }

        bottomPlayer.hide()
        bottomPlayer = PlayerView(rs.currentGame.players[rs.playerIndex], "bottom")
        addComponents(
            bottomPlayer.name, bottomPlayer.log, bottomPlayer.card0, bottomPlayer.card1, bottomPlayer.card2
        )

        score.text = "Current score: ${rootService.currentGame.players[rs.playerIndex].score}"
    }

    override fun refreshAfterGameTurn() {
        val prev = if (rootService.playerIndex == 0) rootService.currentGame.players.size - 1
                   else rootService.playerIndex - 1

        // Refresh current player's cards and middle cards only
        bottomPlayer.hide()
        bottomPlayer = PlayerView(rootService.currentGame.players[prev], "bottom", bottomPlayer.log.text)
        addComponents(
            bottomPlayer.name, bottomPlayer.log, bottomPlayer.card0, bottomPlayer.card1, bottomPlayer.card2
        )
        score.text = "Current score: ${rootService.currentGame.players[prev].score}"
        refreshMiddleCards(rootService)

        turnOffButton(passButton)
        turnOffButton(knockButton)
        turnOffButton(swapOneButton)
        turnOffButton(swapAllButton)
        turnOnButton(continueButton)
    }

    override fun refreshAfterGameOver() {
        refreshAfterGameTurn()
        turnOffButton(continueButton)

        //Thread.sleep(3000)
    }

    init {
        middleCard0.isDisabled = true
        middleCard1.isDisabled = true
        middleCard2.isDisabled = true

        middleCard0.flip()
        middleCard1.flip()
        middleCard2.flip()

        if (rootService.currentGame.players.size >= 3)
            leftPlayer = PlayerView(rootService.currentGame.players[1], "left")
        if (rootService.currentGame.players.size == 4)
            rightPlayer = PlayerView(rootService.currentGame.players[3], "right")

        // dark green for "Casino table" flair
        background = ColorVisual(108, 168, 59)

        addComponents(
            bottomPlayer.name, bottomPlayer.log, bottomPlayer.card0, bottomPlayer.card1, bottomPlayer.card2,
            topPlayer.name, topPlayer.log, topPlayer.card0, topPlayer.card1, topPlayer.card2, score, prompt,
            middleCard0, middleCard1, middleCard2, passButton, knockButton, swapOneButton, swapAllButton, continueButton
        )
        if (leftPlayer != null) {
            addComponents(
                leftPlayer!!.name, leftPlayer!!.log, leftPlayer!!.card0, leftPlayer!!.card1, leftPlayer!!.card2
            )
        }
        if (rightPlayer != null) {
            addComponents(
                rightPlayer!!.name, rightPlayer!!.log, rightPlayer!!.card0, rightPlayer!!.card1, rightPlayer!!.card2
            )
        }
    }

}

