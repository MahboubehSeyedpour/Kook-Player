package com.example.kookplayer.player

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.kookplayer.repositories.appdatabase.entities.SongModel
import com.example.kookplayer.services.NotificationPlayerService
import com.example.kookplayer.utlis.ImageUtils
import com.example.kookplayer.views.MainActivity

class MediaPlayerAgent(context: Context) {


    private val context = context
    private var mediaPlayer = MediaPlayer()

    var controller = MediaController(context)

    //    private val songImageViewInPanel: ImageView = imageView
//    private val songTitleTextView: TextView = textView
    private lateinit var currentPlayingSong: SongModel

    val ONGOING_NOTIFICATION_ID = 1001


    fun getMediaPlayer(): MediaPlayer {
        return mediaPlayer
    }

    fun reset() {
        mediaPlayer.reset()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun playMusic(data: String) {

//        song.image?.let { loadSongImage(it, songImageViewInPanel) }
//        loadSongTitle(song.title, songTitleTextView)

        val uri: Uri = Uri.parse(data)

        mediaPlayer.release()

        mediaPlayer = MediaPlayer.create(context, uri)
        mediaPlayer.start()


//        currentPlayingSong = song

        playAsService()


    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun playAsService() {

//        val foregroundService: PendingIntent =
//            Intent(this, ExampleActivity::class.java).let { notificationIntent ->
//                PendingIntent.getActivity(this, 0, notificationIntent, 0)
//            }
//
//        val notification: Notification = Notification.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
//            .setContentTitle(MainActivity.activity.resources.getText(R.string.notification_title))
//            .setContentText(MainActivity.activity.resources.getText(R.string.notification_message))
//            .setSmallIcon(R.drawable.icon)
//            .setContentIntent(foregroundService)
//            .setTicker(MainActivity.activity.resources.getText(R.string.ticker_text))
//            .build()
//
//        // Notification ID cannot be 0.
//

//        startForeground(ONGOING_NOTIFICATION_ID, notification)

        NotificationPlayerService.stopNotification(MainActivity.activity.baseContext)
        NotificationPlayerService.startNotification(MainActivity.activity.baseContext, "start notif")

    }

    fun pauseMusic() {
        mediaPlayer.pause()
    }

    fun resumePlaying() {
        mediaPlayer.start()
//        NotificationPlayerService.startNotification(context, "start notif")
    }

    fun seekTo(newPosition: Int) {

        mediaPlayer.seekTo(newPosition)
    }

    fun loadSongImage(image: Bitmap, imageView: ImageView) {
        ImageUtils.loadImageToImageView(
            context,
            imageView,
            image
        )
    }

    fun loadSongTitle(title: String, txtView: TextView) {
        txtView.text = title
    }

    fun getRemainingTimeInPercentage(progressInPercentage: Float): Float {
//        TODO(get current song and calculate remaining time)
        return 0f
    }

    fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }


    fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }


    fun getDuration(): Int {
        return mediaPlayer.duration
    }

    fun getCurrentPlayingSong(): SongModel {
        return currentPlayingSong
    }

    fun seekToTime() {
//        TODO(use MediaPlayer.seekto())
    }

    fun stop() {
        mediaPlayer.stop()
    }
}