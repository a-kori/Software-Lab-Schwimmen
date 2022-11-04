package exceptions

/**
 * An exception type emerging when the user tries to
 * take cards from a pile with not enough cards in it.
 */
class NoCardsLeftException (message: String) : Exception(message)