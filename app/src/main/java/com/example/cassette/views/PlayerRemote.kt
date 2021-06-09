package com.example.cassette.views

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import com.example.cassette.R
import com.example.cassette.models.Song_Model
import com.example.cassette.views.Fragments.Library
import kotlin.math.max
import kotlin.random.Random.Default.nextInt

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

    fun playNextMusic(mode: MainActivity.playerMode) {

        when (mode) {
            MainActivity.playerMode.NORMAL -> {
                var position = Library.songsAdapter?.position
                if (position != null) {
                    Library.songsAdapter?.updatePosition(++position)
                }
                if (position != null) {
                    val song: Song_Model? = Library.arraylist?.get(position)
                    playMusic((song?.data).toString())
                }
            }
            MainActivity.playerMode.SHUFFLE -> {
                shuffleMode()
            }
        }
    }

    fun playPrevMusic(mode: MainActivity.playerMode) {

        when (mode) {
            MainActivity.playerMode.NORMAL -> {
                var position = Library.songsAdapter?.position
                if (position != null) {
                    Library.songsAdapter?.updatePosition(--position)
                }
                if (position != null && position >= 0) {
                    val song: Song_Model? = Library.arraylist?.get(position)
                    playMusic((song?.data).toString())
                }
            }
        }
    }


    //Music Playing Modes
    fun shuffleMode() {
        var maxSize = Library.arraylist?.size
        if (maxSize != null) {
            var random = (0..maxSize).random()
            var newSong = Library.arraylist?.get(random)?.data
            if (newSong != null) {
                playMusic(newSong)
            }
        }
    }

    fun repeatOneMode() {

    }

    fun repeatAllMode() {

    }
}