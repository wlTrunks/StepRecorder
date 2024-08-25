package com.lingdtkhe.core.ui.screen

/**
 * Вариация жизненого цикла цикла
 */
interface ScreenLifeCycle {
    fun onCreate() {}
    fun onDestroy() {}
    fun onResume() {}
    fun onPause() {}
}
