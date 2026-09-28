package com.example.emptyapp.core.common.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Base class implementing the MVI contract described in `AGENTS.md`:
 * - [uiState] is a [StateFlow] holding the single source of truth for the UI.
 * - [events] is a [SharedFlow] of user intents.
 * - [effects] is a [Flow] backed by a [Channel] for one-off side effects.
 *
 * Subclasses change state through [setState] and route asynchronous failures
 * through [onError] from the safe [launch] helper.
 */
abstract class BaseMviViewModel<State : UiState, Event : UiEvent, Effect : UiEffect>(
    initialState: State,
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<Event>(extraBufferCapacity = EVENT_BUFFER_CAPACITY)
    val events: SharedFlow<Event> = _events.asSharedFlow()

    private val _effects = Channel<Effect>(Channel.BUFFERED)
    val effects: Flow<Effect> = _effects.receiveAsFlow()

    protected val currentState: State
        get() = _uiState.value

    /** Applies an immutable reducer to the current state. */
    protected fun setState(reducer: State.() -> State) {
        _uiState.update { current -> current.reducer() }
    }

    /** Emits a user intent. Non-suspending: safe to call from UI callbacks. */
    protected fun sendEvent(event: Event) {
        _events.tryEmit(event)
    }

    /** Emits a one-off effect, suspending until the buffer has room. */
    protected suspend fun sendEffect(effect: Effect) {
        _effects.send(effect)
    }

    /**
     * Launches [block] in [viewModelScope] and forwards any non-cancellation
     * [Throwable] to [onError].
     */
    protected fun launch(
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        block: suspend CoroutineScope.() -> Unit,
    ): Job = viewModelScope.launch(dispatcher) {
        try {
            block()
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    /** Override to surface a failure in state or as an effect. */
    protected open fun onError(throwable: Throwable) = Unit

    private companion object {
        const val EVENT_BUFFER_CAPACITY = 64
    }
}
