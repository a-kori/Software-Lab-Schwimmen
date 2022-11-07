package exceptions

/**
 * An exception type emerging when the user tries to
 * initialize a game with an invalid number of players.
 */
class PlayerCountException (message: String) : Exception(message)