package com.example.cassette.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
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
//        super.onStartCommand(intent, flags, startId)



        val input = intent?.getStringExtra("inputExtra")
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        notificationIntent.putExtra("play", R.string.notification_action_play)

        val mediaSession = android.support.v4.media.session.MediaSessionCompat(
            baseContext,
            "notiff"
        )


        val style = androidx.media.app.NotificationCompat.MediaStyle()
            .setMediaSession(mediaSession.sessionToken)
            .setShowCancelButton(false)

//        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle(Coordinator.currentPlayingSong?.title)
//            .setNotificationSilent()
//            .setContentText(input)
//            .setStyle(style)
//            .setAutoCancel(true)
//            .setColor(R.color.new_blue)
//            .setStyle(NotificationCompat.BigPictureStyle().setSummaryText(Coordinator.currentPlayingSong?.artistName?:"").bigPicture(Coordinator.currentPlayingSong?.image).setBigContentTitle(Coordinator.currentPlayingSong?.title?:""))
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setSmallIcon(R.drawable.ic_play__2_)
//            .setLargeIcon(Coordinator.currentPlayingSong?.image)
//            .setContentIntent(pendingIntent)
//            .build()


//        val notificationBuilder = NotificationCompat.Builder(baseContext, CHANNEL_ID)
//        notificationBuilder.setContentTitle(Coordinator.currentPlayingSong?.title)
//        notificationBuilder.setNotificationSilent()
//        notificationBuilder.setContentText(input)
//        notificationBuilder.setStyle(style)
//        notificationBuilder.setAutoCancel(true)
//        notificationBuilder.setColor(R.color.new_blue)
//        notificationBuilder.setStyle(NotificationCompat.BigPictureStyle().setSummaryText(Coordinator.currentPlayingSong?.artistName?:"").bigPicture(Coordinator.currentPlayingSong?.image).setBigContentTitle(Coordinator.currentPlayingSong?.title?:""))
//        notificationBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT)
//        notificationBuilder.setSmallIcon(R.drawable.ic_play__2_)
//        notificationBuilder.setLargeIcon(Coordinator.currentPlayingSong?.image)
//        notificationBuilder.setContentIntent(pendingIntent)


        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(Coordinator.currentPlayingSong?.title)
            .setNotificationSilent()
            .setContentText(Coordinator.currentPlayingSong?.artistName ?: "")
            .setStyle(style)
            .setAutoCancel(true)
            .setColor(R.color.new_blue)
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
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.exo_icon_previous, "previous", null)
            .addAction(R.drawable.exo_icon_play, "play", null)
            .addAction(R.drawable.exo_icon_next, "next", null)
            .build()


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


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}