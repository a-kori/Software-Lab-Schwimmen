package view

/**
 * This interface provides a mechanism for the service layer classes to communicate
 * to the view classes that certain changes have been made to the entity
 * layer, so that the user interface can be updated accordingly.
 *
 * Default (empty) implementations are provided for all methods, so that implementing
 * UI classes only need to react to events relevant to them.
 *
 * @see service.AbstractRefreshingService
 */
interface Refreshable {
    /**
     * Perform refreshes that are necessary after a new game started
     */
    fun refreshAfterStartNewGame() {}
    /**
     * Perform refreshes that are necessary after a player has taken their turn
     */
    fun refreshAfterGameTurn() {}
    /**
     * Perform refreshes that are necessary after the last round was played
     */
    fun refreshAfterGameOver() {}
}