package view
import entity.Player
import service.CardImageLoader

import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ImageVisual

/**
* Represents ComponentView objects that semantically belong to one [player].
 * @param player the player represented by the [PlayerView]
 * @param where the position where the view should be placed on the screen
 * - a choice between "bottom", "top", "left" and "right"
 * @param logString the log text that represents the player's last action in the game
*/
data class PlayerView(private val player: Player, private val where: String, private val logString: String = "") {

    private val cardImageLoader = CardImageLoader()

    val card0 = CardView(
        height = 200,
        width = 130,
        front = ImageVisual(cardImageLoader.frontImageFor(player.cards[0].suit, player.cards[0].value)),
        back = ImageVisual(cardImageLoader.backImage),
        posX = when (where) {
            "bottom", "top" -> 710
            "left" -> 430
            else -> 1250
        },
        posY = when (where) {
            "left", "right" -> 350
            "top" -> 100
            else -> 690
        }
    )
    val card1 = CardView(
        height = 200,
        width = 130,
        front = ImageVisual(cardImageLoader.frontImageFor(player.cards[1].suit, player.cards[1].value)),
        back = ImageVisual(cardImageLoader.backImage),
        posX = when (where) {
            "bottom", "top" -> card0.posX + 130
            else -> card0.posX
        },
        posY = when (where) {
            "left", "right" -> card0.posY + 50
            else -> card0.posY
        }
    )
    val card2 = CardView(
        height = 200,
        width = 130,
        front = ImageVisual(cardImageLoader.frontImageFor(player.cards[2].suit, player.cards[2].value)),
        back = ImageVisual(cardImageLoader.backImage),
        posX = when (where) {
            "bottom", "top" -> card1.posX + 130
            else -> card1.posX
        },
        posY = when (where) {
            "left", "right" -> card1.posY + 50
            else -> card1.posY
        }
    )

    val name = Label(
        text = player.name,
        font = Font(size = 22, color = java.awt.Color.WHITE, fontWeight = Font.FontWeight.BOLD),
        width = 300,
        height = 35,
        posX = when (where) {
            "bottom", "top" -> card0.posX + 45
            else -> card0.posX - 90
        },
        posY = when (where) {
            "bottom" -> card0.posY + 220
            else -> card0.posY - 90
        }
    )

    val log = Label(
        text = logString,
        font = Font(color = java.awt.Color.WHITE),
        width = 300,
        height = 35,
        posX = name.posX,
        posY = name.posY + 35
    )

    init {
        card0.isDisabled = true
        card1.isDisabled = true
        card2.isDisabled = true

        if (where == "bottom") {
            card0.flip()
            card1.flip()
            card2.flip()
        }
    }

    /**
     * Hides this PlayerView on the screen.
     */
    fun hide() {
        name.isVisible = false
        log.isVisible = false
        card0.isVisible = false
        card1.isVisible = false
        card2.isVisible = false
    }
}