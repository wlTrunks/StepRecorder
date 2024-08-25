package com.lingdtkhe.sgooglefitsaver.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.lingdtkhe.sgooglefitsaver.R
import kotlin.random.Random

internal object NotificationsHelper {

    fun removeNotification(context: Context) {
        val notificationManager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    fun createNotificationChannel(context: Context) {
        val notificationManager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            context.getString(R.string.general_notification_channel),
            context.getString(R.string.channel_general_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    fun buildNotification(context: Context): Notification {
        return NotificationCompat.Builder(context, context.getString(R.string.general_notification_channel))
            .setNumber(Random.nextInt())
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.notification_description))
            .setSmallIcon(R.drawable.ic_running)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .addAction(
                R.drawable.ic_close,
                context.getString(R.string.text_stop_service),
                PendingIntent.getService(
                    context,
                    SERVICE_CODE,
                    Intent(context, TrackStepSensorService::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .setOngoing(true)
            .build()
    }

    private const val SERVICE_CODE = 1001
}