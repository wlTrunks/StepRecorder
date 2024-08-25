package com.lingdtkhe.sgooglefitsaver.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.lingdtkhe.domain.service.InitServiceComponent
import com.lingdtkhe.domain.interactors.TrackStepSensorServiceInteractor
import com.lingdtkhe.domain.util.provideCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import org.koin.android.ext.android.inject
import timber.log.Timber

class TrackStepSensorService : Service() {

    private val coroutineScope = Dispatchers.Main.provideCoroutineScope(this::class.simpleName)
    private val trackStepSensorServiceInteractor: TrackStepSensorServiceInteractor by inject()
    private val initServiceComponent: InitServiceComponent<TrackStepSensorService> by inject()

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.extras?.getBoolean(StepRecordServiceComponentImpl.STATUS_SERVICE) == true) {
            trackStepSensorServiceInteractor.startService()
        } else {
            stopSelf()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate")
        initServiceComponent.setService(this)
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        trackStepSensorServiceInteractor.stopService()
        initServiceComponent.clear()
        super.onDestroy()
    }

    companion object {
        const val SERVICE_ID = 101
    }
}