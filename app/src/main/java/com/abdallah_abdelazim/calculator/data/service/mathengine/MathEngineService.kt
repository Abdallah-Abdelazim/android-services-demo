package com.abdallah_abdelazim.calculator.data.service.mathengine

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import com.abdallah_abdelazim.calculator.R

class MathEngineService : Service() {

    companion object {
        private const val MATH_ENGINE_NOTIFICATION_CHANNEL_ID = "math_engine_notification_channel";
        private const val MATH_ENGINE_NOTIFICATION_ID = 1
    }

    private val binder = MathEngineBinder()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()

        startNotification()
    }

    private fun startNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MATH_ENGINE_NOTIFICATION_CHANNEL_ID,
                getString(R.string.math_engine_notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder =
            NotificationCompat.Builder(this, MATH_ENGINE_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(getString(R.string.math_engine_notification_title))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationBuilder.setSmallIcon(
                IconCompat.createWithResource(
                    this,
                    R.drawable.ic_calculate_24
                )
            )
        }

        val notification = notificationBuilder.build()

        startForeground(MATH_ENGINE_NOTIFICATION_ID, notification)
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class MathEngineBinder : Binder() {
        val service: MathEngineService
            get() = this@MathEngineService
    }
}