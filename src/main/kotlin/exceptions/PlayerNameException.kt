package exceptions

/**
 * An exception type emerging when the user tries to
 * assign a blank or an already taken name to a player.
 */
class PlayerNameException (message: String) : PlayersInitException(message)