package com.lingdtkhe.sgooglefitsaver.ui

import android.content.ActivityNotFoundException
import androidx.activity.ComponentActivity
import com.lingdtkhe.core.ui.activity.ActivityProvider
import com.lingdtkhe.core.ui.activity.InitComponentActivity
import com.lingdtkhe.core.ui.navigation.NavigationComponentInitialize
import com.lingdtkhe.sgooglefitsaver.ui.common.IntentLauncherInitializer

/**
 * Реализация компонетa активити иницилизация компонентов
 * @param intentLauncher запроса пермишенов
 * @param navigationComponentInitialize компонет навигации
 */
class InitComponentActivityImpl(
    private val intentLauncher: IntentLauncherInitializer,
    private val navigationComponentInitialize: NavigationComponentInitialize
) : InitComponentActivity, ActivityProvider {
    private var _activity: ComponentActivity? = null

    override fun setActivity(activity: ComponentActivity) {
        _activity = activity
        intentLauncher.setActivity(activity)
        navigationComponentInitialize.setActivity(activity)
    }

    override fun clear() {
        _activity = null
    }

    override fun provideActivity(): ComponentActivity {
        return _activity ?: throw ActivityNotFoundException("ComponentActivity not init")
    }
}