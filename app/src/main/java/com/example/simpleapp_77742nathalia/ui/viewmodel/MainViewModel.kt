package com.example.simpleapp_77742nathalia.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.simpleapp_77742nathalia.data.baas.BaasAuthManager
import com.example.simpleapp_77742nathalia.exceptions.ExceededWordLimitException
import com.example.simpleapp_77742nathalia.exceptions.WordTooLongException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Data class representing Exception result logs for UI rendering.
 */
data class ExceptionLog(
    val title: String,
    val exceptionType: String,
    val message: String,
    val isSuccess: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Class-level State Holder (ViewModel) for State Hoisting and application logic.
 */
class MainViewModel : ViewModel() {

    // BaaS Manager
    val baasAuthManager = BaasAuthManager()

    // --- State Hoisting: Class Level Variables ---
    // Counter 1 (Demonstrates state managed at ViewModel level vs Composables)
    private val _hoistedCounter1 = MutableStateFlow(0)
    val hoistedCounter1: StateFlow<Int> = _hoistedCounter1.asStateFlow()

    // Counter 2
    private val _hoistedCounter2 = MutableStateFlow(0)
    val hoistedCounter2: StateFlow<Int> = _hoistedCounter2.asStateFlow()

    fun incrementHoistedCounters() {
        _hoistedCounter1.value += 1
        _hoistedCounter2.value += 1
    }

    fun resetHoistedCounters() {
        _hoistedCounter1.value = 0
        _hoistedCounter2.value = 0
    }

    // --- Exceptions 1: Preconditions ---
    // Scenario A: Light is turned on or off
    private val _isLightOn = MutableStateFlow(false)
    val isLightOn: StateFlow<Boolean> = _isLightOn.asStateFlow()

    private val _lightExceptionLog = MutableStateFlow<ExceptionLog?>(null)
    val lightExceptionLog: StateFlow<ExceptionLog?> = _lightExceptionLog.asStateFlow()

    fun toggleLight(isOn: Boolean) {
        _isLightOn.value = isOn
        _lightExceptionLog.value = null
    }

    /**
     * Executes action requiring light. Uses Kotlin precondition function [check].
     */
    fun executeLightAction() {
        try {
            // Precondition Function 1: check() throws IllegalStateException if expression is false
            check(_isLightOn.value) { "Precondition Failed (check): A light is not turned on!" }

            _lightExceptionLog.value = ExceptionLog(
                title = "Light Action Success",
                exceptionType = "None",
                message = "Action executed successfully because light is ON!",
                isSuccess = true
            )
        } catch (e: IllegalStateException) {
            _lightExceptionLog.value = ExceptionLog(
                title = "Light Action Caught Exception",
                exceptionType = e.javaClass.simpleName,
                message = e.message ?: "IllegalStateException occurred.",
                isSuccess = false
            )
        }
    }

    // Scenario B: User input for a number must not be negative
    private val _numberInputText = MutableStateFlow("-5")
    val numberInputText: StateFlow<String> = _numberInputText.asStateFlow()

    private val _numberExceptionLog = MutableStateFlow<ExceptionLog?>(null)
    val numberExceptionLog: StateFlow<ExceptionLog?> = _numberExceptionLog.asStateFlow()

    fun updateNumberInput(input: String) {
        _numberInputText.value = input
        _numberExceptionLog.value = null
    }

    /**
     * Validates number input. Uses Kotlin precondition function [require].
     */
    fun validateNumberInput() {
        try {
            val num = _numberInputText.value.toIntOrNull()
                ?: throw IllegalArgumentException("Invalid input: '${_numberInputText.value}' is not a valid integer.")

            // Precondition Function 2: require() throws IllegalArgumentException if expression is false
            require(num >= 0) { "Precondition Failed (require): User input number ($num) is negative!" }

            _numberExceptionLog.value = ExceptionLog(
                title = "Number Input Validated",
                exceptionType = "None",
                message = "Success! Input number $num is non-negative (>= 0).",
                isSuccess = true
            )
        } catch (e: IllegalArgumentException) {
            _numberExceptionLog.value = ExceptionLog(
                title = "Number Precondition Exception Caught",
                exceptionType = e.javaClass.simpleName,
                message = e.message ?: "IllegalArgumentException occurred.",
                isSuccess = false
            )
        }
    }

    // --- Exceptions 2: Custom Exception ---
    private val _wordInputText = MutableStateFlow("Supercalifragilistic")
    val wordInputText: StateFlow<String> = _wordInputText.asStateFlow()

    private val _wordExceptionLog = MutableStateFlow<ExceptionLog?>(null)
    val wordExceptionLog: StateFlow<ExceptionLog?> = _wordExceptionLog.asStateFlow()

    private val _useSubclassException = MutableStateFlow(true)
    val useSubclassException: StateFlow<Boolean> = _useSubclassException.asStateFlow()

    fun updateWordInput(word: String) {
        _wordInputText.value = word
        _wordExceptionLog.value = null
    }

    fun toggleUseSubclassException(useSubclass: Boolean) {
        _useSubclassException.value = useSubclass
    }

    /**
     * Checks word length and provokes custom extendable exception if length > 10 characters.
     */
    fun checkWordLength() {
        val word = _wordInputText.value
        try {
            if (word.length > 10) {
                if (_useSubclassException.value) {
                    val overflow = word.length - 10
                    // Throw custom subclass extending WordTooLongException
                    throw ExceededWordLimitException(word = word, overflowCount = overflow)
                } else {
                    // Throw base custom exception
                    throw WordTooLongException(word = word)
                }
            }

            _wordExceptionLog.value = ExceptionLog(
                title = "Word Validation Success",
                exceptionType = "None",
                message = "Success! Word '$word' length is ${word.length} characters (<= 10).",
                isSuccess = true
            )
        } catch (e: WordTooLongException) {
            // Catches WordTooLongException and any of its subclasses (demonstrating open/extendable exception hierarchy)
            val isSubclass = e is ExceededWordLimitException
            val typeDesc = if (isSubclass) {
                "${e.javaClass.simpleName} (Subclass of WordTooLongException)"
            } else {
                "${e.javaClass.simpleName} (Base Custom Exception)"
            }

            _wordExceptionLog.value = ExceptionLog(
                title = "Custom Exception Caught",
                exceptionType = typeDesc,
                message = e.message ?: "WordTooLongException occurred.",
                isSuccess = false
            )
        }
    }
}
