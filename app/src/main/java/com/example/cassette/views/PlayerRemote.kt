package com.example.cassette.views

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import com.example.cassette.R
import com.example.cassette.models.Song_Model
import com.example.cassette.views.Fragments.Library

object PlayerRemote {
    lateinit var mediaPlayer: MediaPlayer
    lateinit var context: Context

    fun setupRemote(context: Context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.nafas)
        this.context = context
    }


    fun playMusic(content: String) {
        val uri: Uri = Uri.parse(content)
        mediaPlayer.release()
        mediaPlayer = MediaPlayer.create(this.context, uri)
        mediaPlayer.start()
    }

    fun pauseMusic() {
        mediaPlayer.pause()
    }

    fun resumePlaying() {
        mediaPlayer.start()
    }

    fun playNextMusic() {
        var position = Library.songsAdapter?.position
        if (position != null) {
            Library.songsAdapter?.updatePosition(++position)
        }
        if (position != null) {
            val song: Song_Model? = Library.arraylist?.get(position)
            playMusic((song?.data).toString())
        }
    }

    fun playPrevMusic() {
        var position = Library.songsAdapter?.position
        if (position != null) {
            Library.songsAdapter?.updatePosition(--position)
        }
        if (position != null && position >= 0) {
            val song: Song_Model? = Library.arraylist?.get(position)
            playMusic((song?.data).toString())
        }
    }


    //Music Playing Modes
    fun shuffleMode() {

    }

    fun repeatOneMode() {

    }
}