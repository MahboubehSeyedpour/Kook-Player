package com.example.cassette.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.cassette.R
import com.example.cassette.manager.Coordinator
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

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        createNotificationChannel()
        registerReceiver(broadcastNotificationReceiver, IntentFilter("Songs"))


        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )

        val mediaSession = android.support.v4.media.session.MediaSessionCompat(
            this,
            "notif"
        )


        val style = androidx.media.app.NotificationCompat.MediaStyle()
            .setMediaSession(mediaSession.sessionToken)
            .setShowCancelButton(false)


        val intentNext = Intent(this, NotificationBroadcastReceiver::class.java)
            .setAction(getString(R.string.notification_action_next))
        val nextPendingIntent =
            PendingIntent.getBroadcast(this, 0, intentNext, PendingIntent.FLAG_UPDATE_CURRENT)

        val intentPlay = Intent(this, NotificationBroadcastReceiver::class.java)
            .setAction(getString(R.string.notification_action_play))
        val playPendingIntent =
            PendingIntent.getBroadcast(this, 0, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT)

        val intentPrev = Intent(this, NotificationBroadcastReceiver::class.java)
            .setAction(getString(R.string.notification_action_previous))
        val prevPendingIntent =
            PendingIntent.getBroadcast(this, 0, intentPrev, PendingIntent.FLAG_UPDATE_CURRENT)


        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(Coordinator.currentPlayingSong?.title)
            .setNotificationSilent()
            .setContentText(Coordinator.currentPlayingSong?.artistName ?: "")
            .setStyle(style)
            .setAutoCancel(true)
            .setColor(Color.BLUE)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(
                    0,
                    1,
                    2,
                    3
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.drawable.ic_play__2_)
            .setLargeIcon(Coordinator.currentPlayingSong?.image)
            .addAction(R.drawable.exo_icon_previous, "previous", prevPendingIntent)
            .addAction(R.drawable.exo_icon_play, "play", playPendingIntent)
            .addAction(R.drawable.exo_icon_next, "next", nextPendingIntent)
            .build()
//            .setContentIntent(pendingIntent)

        startForeground(1, notification)

        return START_NOT_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            serviceChannel.description =
                "The playing notification provides actions for play/pause etc."
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        stopSelf()
        unregisterReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
//        unregisterReceiver()
    }

    fun unregisterReceiver()
    {
        unregisterReceiver(broadcastNotificationReceiver)
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    private val broadcastNotificationReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.extras!!.getString("actionname")
            when (action) {
                getString(R.string.notification_action_next) -> Coordinator.playNextSong()
                getString(R.string.notification_action_play) -> {
                    if (Coordinator.isPlaying()) {
                        Coordinator.pause()
                    } else {
                        Coordinator.resume()
                    }
                }
                getString(R.string.notification_action_previous) -> Coordinator.playPrevSong()
            }

        }
    }
}