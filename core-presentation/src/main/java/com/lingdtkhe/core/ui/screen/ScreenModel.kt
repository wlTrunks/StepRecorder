package com.lingdtkhe.core.ui.screen

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Модель экрана
 * @param state текущее состояние экрана
 * @param event для разовых событий
 */
interface ScreenModel<T : ScreenState, A : ScreenAction, E : ScreenEvent> {
    val state: StateFlow<T>
    val event: Flow<E>

    fun onAction(action: A)

    fun clear()
}

abstract class BaseScreenModel<T : ScreenState, A : ScreenAction, E : ScreenEvent>(initState: T) :
    ScreenModel<T, A, E> {
    protected val _state = MutableStateFlow(initState)
    protected val _event = Channel<E>()
    protected val mainScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate + CoroutineExceptionHandler { _, exception ->
            Timber.e(exception, this::class.simpleName)
        })

    override val state: StateFlow<T> = _state

    override val event: Flow<E> = _event.receiveAsFlow()

    override fun clear() {
        mainScope.cancel()
    }

    protected fun updateState(reduce: T.() -> T) {
        _state.value = reduce(_state.value)
    }

    protected fun sendEvent(vararg events: E) {
        mainScope.launch {
            events.forEach { event ->
                _event.send(event)
            }
        }
    }
}