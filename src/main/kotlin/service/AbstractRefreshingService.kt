package service
import view.Refreshable

/**
 * An abstract service class, used to store refreshable
 * objects used in the game as well as to execute
 * methods, applicable to all the objects together.
 */
abstract class AbstractRefreshingService {

    /**
     * A list of refreshable objects used in the game.
     */
    protected val refreshables : ArrayList<Refreshable> = arrayListOf()

    /**
     * Adds a new refreshable object to [refreshables]
     */
    fun addRefreshable(newRefreshable : Refreshable) {
        refreshables.add(newRefreshable)
    }

    /**
     * Replaces the refreshable at [index] with a [newRefreshable]
     */
    fun replaceRefreshableAt(index : Int, newRefreshable : Refreshable) {
        refreshables[index] = newRefreshable
    }

    /**
     * Executes the passed [method] on all [refreshables]
     */
    fun onAllRefreshables(method : Refreshable.() -> Unit) {
        for (refreshable in refreshables) {
            refreshable.method()
        }
    }

}