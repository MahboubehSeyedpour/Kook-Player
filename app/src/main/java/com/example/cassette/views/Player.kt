package com.example.cassette.views

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

class Player(context: Context)
{

    val context = context

    fun playMusic(content: String) {

        val uri: Uri = Uri.parse(content)

        PlayerRemote.mediaPlayer.release()

        PlayerRemote.mediaPlayer = MediaPlayer.create(context, uri)
        PlayerRemote.mediaPlayer.start()

    }

    fun pauseMusic() {
        PlayerRemote.mediaPlayer.pause()
    }

    fun resumePlaying() {
        PlayerRemote.mediaPlayer.start()
    }

    fun seekTo()
    {
//        TODO()
    }
}