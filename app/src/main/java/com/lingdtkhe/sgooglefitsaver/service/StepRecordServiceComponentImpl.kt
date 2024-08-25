package com.lingdtkhe.sgooglefitsaver.service

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.lingdtkhe.domain.service.ServiceComponent
import timber.log.Timber
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Реализаяци запуска и остановки сервиса
 * Кидается интент с запуском сервиса и регистрируется броадкаст ресивер
 * в [com.lingdtkhe.sgooglefitsaver.service.StepRecordServiceStatusImpl] кидается броадкаст
 */
class StepRecordServiceComponentImpl(
    private val context: Context,
) : ServiceComponent {
    private fun broadcastReceiver(suspend: Continuation<Boolean>) = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Timber.d("onReceive intent extras ${intent.extras?.getBoolean(STATUS_SERVICE)}")
            suspend.resume(intent.getBooleanExtra(STATUS_SERVICE, false))
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this)
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun awaitServiceResult(suspend: Continuation<Boolean>) {
        LocalBroadcastManager.getInstance(context).registerReceiver(
            broadcastReceiver(suspend),
            IntentFilter(FILTER_SERVICE),
        )
    }

    override suspend fun startService(): Boolean = suspendCoroutine { suspend ->
        awaitServiceResult(suspend)
        ContextCompat.startForegroundService(
            context,
            Intent(context, TrackStepSensorService::class.java)
                .putExtra(STATUS_SERVICE, true)
        )
    }

    override suspend fun stopService(): Boolean = suspendCoroutine { suspend ->
        awaitServiceResult(suspend)
        ContextCompat.startForegroundService(
            context,
            Intent(context, TrackStepSensorService::class.java)
                .putExtra(STATUS_SERVICE, false)
        )
        context.stopService(Intent(context, TrackStepSensorService::class.java))
    }

    companion object {
        const val FILTER_SERVICE = "filter_sgooglefit"
        const val STATUS_SERVICE = "status_sgooglefit"
    }
}