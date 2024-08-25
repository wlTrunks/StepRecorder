package com.lingdtkhe.core.ui.navigation

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.lingdtkhe.core.ui.activity.InitComponentActivity
import com.lingdtkhe.core.ui.screen.ScreenComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import timber.log.Timber

interface NavigationComponent {
    fun push(screen: ScreenComponent<*>)
    fun replaceAll(screen: ScreenComponent<*>)
    fun pop()
    val screen: Flow<ScreenComponent<*>>
    val currentScreen: ScreenComponent<*>?
}

interface NavigationComponentInitialize : InitComponentActivity

class NavigationComponentImpl : NavigationComponent, NavigationComponentInitialize {
    private val _screen: MutableStateFlow<ScreenComponent<*>?> = MutableStateFlow(null)
    private val query = ArrayDeque<ScreenComponent<*>>()
    private var _activity: ComponentActivity? = null
    private val backCallBack = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            Timber.d("handleOnBackPressed ${query.size}")
            if (query.size > 1) {
                pop()
            } else {
                _activity?.finish()
            }
        }
    }

    override val screen: Flow<ScreenComponent<*>> = _screen.filterNotNull()
    override val currentScreen: ScreenComponent<*>? = _screen.value

    override fun push(screen: ScreenComponent<*>) {
        _screen.value = screen
        query.firstOrNull()?.onPause()
        query.add(screen)
        Timber.d("push $query")
    }

    override fun replaceAll(screen: ScreenComponent<*>) {
        Timber.d("replaceAll $query")
        query.forEach { screen -> screen.onDestroy() }
        query.clear()
        push(screen)
    }

    override fun pop() {
        if (query.size > 0) {
            val screen = query.removeLast()
            screen.onDestroy()
            Timber.d("pop $screen")
            Timber.d("query $query")
            _screen.value = query.lastOrNull()?.apply {
                onResume()
            }
        }
    }

    override fun setActivity(activity: ComponentActivity) {
        _activity = activity
        /**
         * Передаем жизненые циклы активити в ScreenComponent
         */
        activity.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                Timber.d("onStateChanged $event")
                when (event) {
                    Lifecycle.Event.ON_CREATE -> _screen.value?.onCreate()
                    Lifecycle.Event.ON_RESUME -> _screen.value?.onResume()
                    Lifecycle.Event.ON_PAUSE -> _screen.value?.onPause()
                    Lifecycle.Event.ON_DESTROY -> clear()
                    else -> Unit
                }
            }
        })
        activity.onBackPressedDispatcher.addCallback(backCallBack)
    }

    override fun clear() {
        _activity = null
    }
}