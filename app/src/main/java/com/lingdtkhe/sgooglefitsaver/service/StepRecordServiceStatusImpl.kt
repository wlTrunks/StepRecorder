package com.lingdtkhe.sgooglefitsaver.service

import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.ServiceCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.lingdtkhe.domain.service.ServiceProvider
import com.lingdtkhe.domain.service.ServiceStatus
import com.lingdtkhe.sgooglefitsaver.service.TrackStepSensorService.Companion.SERVICE_ID

/**
 * Отправляет результат о запуске сервисе
 */
class StepRecordServiceStatusImpl(
    private val context: Context,
    private val serviceProvider: ServiceProvider<TrackStepSensorService>
) : ServiceStatus {
    private var _isRunning = false
    private val localBroadcastManager by lazy { LocalBroadcastManager.getInstance(context) }

    private fun startAsForegroundService() {
        ServiceCompat.startForeground(
            serviceProvider.provideService(),
            SERVICE_ID,
            NotificationsHelper.buildNotification(context),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH
            } else {
                0
            }
        )
        localBroadcastManager
            .sendBroadcast(
                Intent(StepRecordServiceComponentImpl.FILTER_SERVICE)
                    .putExtra(StepRecordServiceComponentImpl.STATUS_SERVICE, true)
            )
    }

    private fun stopForegroundService() {
        NotificationsHelper.removeNotification(context)
        localBroadcastManager
            .sendBroadcast(
                Intent(StepRecordServiceComponentImpl.FILTER_SERVICE)
                    .putExtra(StepRecordServiceComponentImpl.STATUS_SERVICE, false)
            )
    }

    override fun setServiceRun(isRunning: Boolean) {
        _isRunning = isRunning
        if (isRunning) startAsForegroundService() else stopForegroundService()
    }

    override fun isRunning(): Boolean = _isRunning
}