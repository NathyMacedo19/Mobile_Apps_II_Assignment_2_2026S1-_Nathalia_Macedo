package com.example.simpleapp_77742nathalia

import com.example.simpleapp_77742nathalia.exceptions.ExceededWordLimitException
import com.example.simpleapp_77742nathalia.exceptions.WordTooLongException
import com.example.simpleapp_77742nathalia.ui.viewmodel.MainViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AssignmentUnitTest {

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel()
    }

    // --- 1. State Hoisting Tests ---
    @Test
    fun testHoistedCountersIncrementAndReset() {
        assertEquals(0, viewModel.hoistedCounter1.value)
        assertEquals(0, viewModel.hoistedCounter2.value)

        viewModel.incrementHoistedCounters()
        assertEquals(1, viewModel.hoistedCounter1.value)
        assertEquals(1, viewModel.hoistedCounter2.value)

        viewModel.incrementHoistedCounters()
        assertEquals(2, viewModel.hoistedCounter1.value)
        assertEquals(2, viewModel.hoistedCounter2.value)

        viewModel.resetHoistedCounters()
        assertEquals(0, viewModel.hoistedCounter1.value)
        assertEquals(0, viewModel.hoistedCounter2.value)
    }

    // --- 2. Preconditions Tests (check & require) ---
    @Test
    fun testLightPreconditionFailureWhenOff() {
        viewModel.toggleLight(false)
        viewModel.executeLightAction()

        val log = viewModel.lightExceptionLog.value
        assertNotNull(log)
        assertFalse(log!!.isSuccess)
        assertEquals("IllegalStateException", log.exceptionType)
        assertTrue(log.message.contains("A light is not turned on"))
    }

    @Test
    fun testLightPreconditionSuccessWhenOn() {
        viewModel.toggleLight(true)
        viewModel.executeLightAction()

        val log = viewModel.lightExceptionLog.value
        assertNotNull(log)
        assertTrue(log!!.isSuccess)
        assertEquals("None", log.exceptionType)
    }

    @Test
    fun testNegativeNumberPreconditionFailure() {
        viewModel.updateNumberInput("-10")
        viewModel.validateNumberInput()

        val log = viewModel.numberExceptionLog.value
        assertNotNull(log)
        assertFalse(log!!.isSuccess)
        assertEquals("IllegalArgumentException", log.exceptionType)
        assertTrue(log.message.contains("is negative"))
    }

    @Test
    fun testNonNegativeNumberPreconditionSuccess() {
        viewModel.updateNumberInput("42")
        viewModel.validateNumberInput()

        val log = viewModel.numberExceptionLog.value
        assertNotNull(log)
        assertTrue(log!!.isSuccess)
    }

    // --- 3. Custom Exception Tests ---
    @Test
    fun testCustomWordTooLongExceptionProvoked() {
        viewModel.toggleUseSubclassException(false)
        viewModel.updateWordInput("Supercalifragilistic") // 20 chars (> 10)
        viewModel.checkWordLength()

        val log = viewModel.wordExceptionLog.value
        assertNotNull(log)
        assertFalse(log!!.isSuccess)
        assertTrue(log.exceptionType.contains("WordTooLongException"))
    }

    @Test
    fun testCustomExtendedSubclassExceptionProvoked() {
        viewModel.toggleUseSubclassException(true)
        viewModel.updateWordInput("Supercalifragilistic") // 20 chars (> 10)
        viewModel.checkWordLength()

        val log = viewModel.wordExceptionLog.value
        assertNotNull(log)
        assertFalse(log!!.isSuccess)
        assertTrue(log.exceptionType.contains("ExceededWordLimitException"))
    }

    @Test
    fun testShortWordNoException() {
        viewModel.updateWordInput("Kotlin") // 6 chars (<= 10)
        viewModel.checkWordLength()

        val log = viewModel.wordExceptionLog.value
        assertNotNull(log)
        assertTrue(log!!.isSuccess)
    }

    @Test
    fun testCustomExceptionHierarchyInheritance() {
        val ex = ExceededWordLimitException("TestWordExceeds", 5)
        assertTrue(WordTooLongException::class.java.isAssignableFrom(ex.javaClass))
    }
}
