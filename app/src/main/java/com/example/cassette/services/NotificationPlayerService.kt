package com.example.cassette.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.cassette.R
import com.example.cassette.views.MainActivity


private const val CHANNEL_ID = "player_channel_id"


class NotificationPlayerService : Service() {


    companion object {
        fun startNotification(context: Context, message: String) {
            val intent = Intent(context, NotificationPlayerService::class.java)
            intent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context, intent)
        }

        fun stopNotification(context: Context) {
            val intent = Intent(context, NotificationPlayerService::class.java)
            context.stopService(intent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        super.onStartCommand(intent, flags, startId)
        val input = intent?.getStringExtra("inputExtra")
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service Kotlin Example")
            .setContentText(input)
            .setSmallIcon(R.drawable.ic_heart)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)

        return START_NOT_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}