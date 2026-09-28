package com.example.emptyapp.core.common.mvi

import app.cash.turbine.test
import com.example.emptyapp.core.common.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class BaseMviViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `initial state is exposed`() = runTest {
        val viewModel = TestViewModel()

        assertEquals(TestState(), viewModel.uiState.value)
    }

    @Test
    fun `setState applies the reducer`() = runTest {
        val viewModel = TestViewModel()

        viewModel.increment()
        viewModel.increment()

        assertEquals(TestState(count = 2), viewModel.uiState.value)
        assertEquals(2, viewModel.currentStateExposed.count)
    }

    @Test
    fun `sendEvent emits to the events flow`() = runTest {
        val viewModel = TestViewModel()

        viewModel.events.test {
            viewModel.emitEvent("hello")
            assertEquals(TestEvent.Message("hello"), awaitItem())
        }
    }

    @Test
    fun `sendEffect emits to the effects flow`() = runTest {
        val viewModel = TestViewModel()

        viewModel.effects.test {
            viewModel.emitEffect("gps_detail/1")
            assertEquals(TestEffect.Navigate("gps_detail/1"), awaitItem())
        }
    }

    @Test
    fun `launch forwards exceptions to onError`() = runTest {
        val viewModel = TestViewModel()

        viewModel.failWith(IllegalStateException("boom"))

        assertEquals("boom", viewModel.lastError?.message)
    }

    @Test
    fun `launch does not call onError on success`() = runTest {
        val viewModel = TestViewModel()

        viewModel.succeed()

        assertNull(viewModel.lastError)
        assertEquals(TestState(done = true), viewModel.uiState.value)
    }

    private data class TestState(
        val count: Int = 0,
        val done: Boolean = false,
    ) : UiState

    private sealed interface TestEvent : UiEvent {
        data class Message(val value: String) : TestEvent
    }

    private sealed interface TestEffect : UiEffect {
        data class Navigate(val route: String) : TestEffect
    }

    private class TestViewModel :
        BaseMviViewModel<TestState, TestEvent, TestEffect>(TestState()) {

        var lastError: Throwable? = null
        val currentStateExposed: TestState get() = currentState

        fun increment() = setState { copy(count = count + 1) }

        fun emitEvent(value: String) = sendEvent(TestEvent.Message(value))

        suspend fun emitEffect(route: String) = sendEffect(TestEffect.Navigate(route))

        fun failWith(throwable: Throwable) = launch { throw throwable }

        fun succeed() = launch { setState { copy(done = true) } }

        override fun onError(throwable: Throwable) {
            lastError = throwable
        }
    }
}
