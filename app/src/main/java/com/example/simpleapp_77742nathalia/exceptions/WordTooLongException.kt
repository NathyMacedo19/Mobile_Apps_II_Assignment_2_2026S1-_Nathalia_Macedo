package com.example.simpleapp_77742nathalia.exceptions

/**
 * Custom exception provoked when a word exceeds the maximum allowed length (10 characters).
 * Marked as `open` so it can be extended by other specialized exception classes.
 */
open class WordTooLongException(
    val word: String,
    val maxLength: Int = 10,
    message: String = "Word '$word' (length: ${word.length}) exceeds maximum limit of $maxLength characters."
) : Exception(message)

/**
 * Example subclass extending [WordTooLongException] to demonstrate inheritance/extension.
 */
class ExceededWordLimitException(
    word: String,
    val overflowCount: Int
) : WordTooLongException(
    word = word,
    maxLength = 10,
    message = "ExceededWordLimitException: Word '$word' exceeded maximum length of 10 by $overflowCount characters!"
)
