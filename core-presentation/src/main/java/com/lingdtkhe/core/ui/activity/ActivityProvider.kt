package com.lingdtkhe.core.ui.activity

import androidx.activity.ComponentActivity

/**
 * Предоставляет текущее активити
 */
interface ActivityProvider {
    fun provideActivity(): ComponentActivity
}