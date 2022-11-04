package view

interface Refreshable {
    fun refreshAfterStartNewGame() : Unit
    fun refreshAfterGameTurn() : Unit
    fun refreshAfterGameOver() : Unit
}