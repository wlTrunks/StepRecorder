package com.lingdtkhe.sgooglefitsaver

import android.app.Application
import com.lingdtkhe.data.di.DataModule
import com.lingdtkhe.domain.di.DomainModule
import com.lingdtkhe.sgooglefitsaver.di.UiModule
import com.lingdtkhe.sgooglefitsaver.service.NotificationsHelper
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Инициализация Koin модулей и канала для нотификации сервиса
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                DataModule,
                DomainModule,
                UiModule,
            )
        }
        NotificationsHelper.createNotificationChannel(this)
    }
}