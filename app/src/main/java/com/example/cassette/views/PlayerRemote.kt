package com.example.cassette.views

import android.content.Context
import android.media.MediaPlayer
import android.widget.ImageView
import android.widget.TextView
import com.example.cassette.models.SongModel
import com.example.cassette.views.Fragments.Library

object PlayerRemote {

    var mediaPlayer = MediaPlayer()
    lateinit var context: Context
    val player: Player by lazy { Player(context, imageView, textView) }
    lateinit var imageView: ImageView
    lateinit var textView: TextView

    enum class playerMode(mode: String) {
        SHUFFLE("shuffle"),
        NORMAL("normal"),
        REPEAT_ONE("repeat_one"),
        REPEAT_ALL("repeat_all")
    }

    fun setupRemote(context: Context, songImageViewInPanel: ImageView, textView: TextView) {
        this.context = context
        imageView = songImageViewInPanel
        this.textView = textView
    }

    fun playNextMusic(mode: playerMode) {

        when (mode) {
            playerMode.NORMAL -> {
                var position = Library.songsAdapter?.position
                if (position != null) {
                    Library.songsAdapter?.updatePosition(newIndex = ++position)
                }
                if (position != null) {
                    val song: SongModel? = Library.songsAdapter?.getSong(position)
                    player.playMusic(song!!)
                }
            }
            playerMode.SHUFFLE -> {
                shuffleMode()
            }
        }
    }

    fun playPrevMusic(mode: playerMode) {

        when (mode) {
            playerMode.NORMAL -> {
                var position = Library.songsAdapter?.position
                if (position != null) {
                    Library.songsAdapter?.updatePosition(newIndex = --position)
                }
                if (position != null && position >= 0) {
                    val song: SongModel? = Library.songsAdapter?.getSong(position)
                    player.playMusic(song!!)
                }
            }
        }
    }


    //Music Playing Modes
    fun shuffleMode() {
//        var maxSize = Library.arraylist?.size
//        if (maxSize != null) {
//            var random = (0..maxSize).random()
//            var newSong = Library.arraylist?.get(random)?.data
//            var newSong = Library.songsAdapter
//            if (newSong != null) {
//                playMusic(newSong)
//            }
//        }
    }

    fun repeatOneMode() {

    }

    fun repeatAllMode() {

    }

    fun playSongAsNextMusic(position: Int) {
//        TODO(play song as next music)
    }

    fun addToPlaylist(position: Int) {
//        TODO(add song to play list)
    }
}