package com.lingdtkhe.data.repositories

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.lingdtkhe.data.sources.StepsSource
import com.lingdtkhe.domain.repository.TrackStepSensorRepo
import com.lingdtkhe.domain.util.provideCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

/**
 * Запускает Fitness запись шагов
 * Подписываемся на сенсоры телефона для отслеживания шагов, сенсоров на телефоне может не быть
 */
internal class TrackStepSensorRepoImpl(
    private val context: Context,
    private val fitnessOptions: FitnessOptions,
    private val stepsSource: StepsSource
) : TrackStepSensorRepo {
    private val coroutineScope = Dispatchers.IO.provideCoroutineScope(this::class.simpleName)
    private val sensorManager: SensorManager by lazy {
        ContextCompat.getSystemService(
            context,
            SensorManager::class.java
        ) as SensorManager
    }
    private val sensor: Sensor? by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) }

    private val sensorEventListener: SensorEventListener by lazy {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null) return
                val step = event.values[0].toInt()
                Timber.d("Steps event.values: $step")
                coroutineScope.launch {
                    stepsSource.saveStep(step, System.currentTimeMillis())
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                Timber.d("Accuracy changed to: $accuracy")
            }
        }
    }

    private fun getGoogleAccount() = GoogleSignIn.getAccountForExtension(context, fitnessOptions)

    override fun startRecord() {
        runCatching {
            coroutineScope.launch {
                val supportedAndEnabled = sensorManager.registerListener(
                    sensorEventListener,
                    sensor,
                    SensorManager.SENSOR_DELAY_UI
                )
                Timber.d("Sensor listener registered: $supportedAndEnabled")
                Fitness.getRecordingClient(context, getGoogleAccount())
                    .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                    .await()
                Timber.d("Successfully subscribed!")
            }
        }.onFailure { Timber.e(it) }
    }


    override fun stopRecord() {
        sensorManager.unregisterListener(sensorEventListener)
    }
}