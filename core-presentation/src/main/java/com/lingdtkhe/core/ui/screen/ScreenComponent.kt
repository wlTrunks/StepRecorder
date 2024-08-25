package com.lingdtkhe.core.ui.screen

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import java.util.UUID

/**
 * Компонент экрана отрисовка UI
 */
interface ScreenComponent<T : ScreenState> : ScreenLifeCycle, RegisterLifeCycle {
    @Composable
    fun renderScreen(state: T)

    @Composable
    fun onEvent(event: ScreenEvent)

    fun screenState(): Flow<T>

    fun screenEvent(): Flow<Pair<UUID, ScreenEvent>>
}

/**
 * Базовая реализация с Koin компонентом для DI,
 * Регистрация коллбеков на жизненый цикл
 */
abstract class BaseScreenComponent<
    T : ScreenState,
    A : ScreenAction,
    E : ScreenEvent,
    M : ScreenModel<T, A, E>
    > : ScreenComponent<T>, KoinComponent {
    protected val lifeCycleObservers = mutableListOf<ScreenLifeCycle>()

    abstract val screenModel: M

    override fun screenState(): Flow<T> = screenModel.state

    override fun screenEvent(): Flow<Pair<UUID, ScreenEvent>> = screenModel.event
        .map { event -> UUID.randomUUID() to event }

    @Composable
    override fun onEvent(event: ScreenEvent) {
    }

    override fun addObserver(observer: ScreenLifeCycle) {
        lifeCycleObservers.add(observer)
    }

    override fun removeObserver(observer: ScreenLifeCycle) {
        lifeCycleObservers.add(observer)
    }

    override fun onDestroy() {
        lifeCycleObservers.forEach { observer -> observer.onDestroy() }
        lifeCycleObservers.clear()
        screenModel.clear()
    }

    override fun onCreate() {
        lifeCycleObservers.forEach { observer -> observer.onCreate() }
    }

    override fun onPause() {
        lifeCycleObservers.forEach { observer -> observer.onPause() }
    }

    override fun onResume() {
        lifeCycleObservers.forEach { observer -> observer.onResume() }
    }
}

interface RegisterLifeCycle {
    fun addObserver(observer: ScreenLifeCycle)
    fun removeObserver(observer: ScreenLifeCycle)
}