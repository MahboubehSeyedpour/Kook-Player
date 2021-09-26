package com.example.kookplayer.helper

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.kookplayer.services.NotificationPlayerService
import com.example.kookplayer.views.activities.MainActivity

class MediaPlayerAgent(context: Context) {


    private val context = context
    private var mediaPlayer = MediaPlayer()


    fun playMusic(data: String) {

        val uri: Uri = Uri.parse(data)

        mediaPlayer.release()

        mediaPlayer = MediaPlayer.create(context, uri)
        mediaPlayer.start()


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            playAsService()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun playAsService() {

        NotificationPlayerService.stopNotification(MainActivity.activity.baseContext)
        NotificationPlayerService.startNotification(
            MainActivity.activity.baseContext,
            "start notif"
        )

    }

    fun pauseMusic() {
        mediaPlayer.pause()
    }

    fun resumePlaying() {
        mediaPlayer.start()
    }

    fun seekTo(newPosition: Int) {

        mediaPlayer.seekTo(newPosition)
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

    fun seekToTime() {
//        TODO(use MediaPlayer.seekto())
    }

    fun stop() {
        mediaPlayer.stop()
    }
}