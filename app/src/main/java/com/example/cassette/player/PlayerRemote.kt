package com.example.cassette.player

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class PlayerRemote(val view: View) {

//    var mediaPlayer = MediaPlayer()
    lateinit var context: Context
    var currentPlayerMode: PlayerStateRepository.PlayerModes = PlayerStateRepository.PlayerModes.REPEAT_ALL

    val MEDIA_PLAYER_AGENT: MediaPlayerAgent by lazy {
        MediaPlayerAgent(
            context,
            imageView,
            textView
        )
    }
    lateinit var imageView: ImageView
    lateinit var textView: TextView



//    fun setupRemote(context: Context, songImageViewInPanel: ImageView, textView: TextView) {
//        PlayerRemote.context = context
//        imageView = songImageViewInPanel
//        PlayerRemote.textView = textView
//    }

//    fun playNextMusic(mode: PlayerStateRepository.PlayerModes) {
//
//        when (mode) {
//            PlayerStateRepository.PlayerModes.REPEAT_ALL -> {
//                var position = Library.songsAdapter?.position
//                if (position != null) {
//                    Library.songsAdapter?.updatePosition(newIndex = ++position)
//                }
//                if (position != null) {
//                    val song: SongModel? = Library.songsAdapter?.getSong(position)
//                    MEDIA_PLAYER_AGENT.playMusic(song!!)
//                }
//            }
//            PlayerStateRepository.PlayerModes.SHUFFLE -> {
//                shuffleMode()
//            }
//            PlayerStateRepository.PlayerModes.REPEAT_ONE -> {
//                Library.songsAdapter?.position?.let { Library.songsAdapter?.updatePosition(newIndex = it) }
//            }
//        }
//    }

//    fun playPrevMusic(mode: PlayerStateRepository.PlayerModes) {
//
//        when (mode) {
//            PlayerStateRepository.PlayerModes.REPEAT_ALL -> {
//                var position = Library.songsAdapter?.position
//                if (position != null) {
//                    Library.songsAdapter?.updatePosition(newIndex = --position)
//                }
//                if (position != null && position >= 0) {
//                    val song: SongModel? = Library.songsAdapter?.getSong(position)
//                    MEDIA_PLAYER_AGENT.playMusic(song!!)
//                }
//            }
//            PlayerStateRepository.PlayerModes.REPEAT_ONE -> {
//                Library.songsAdapter?.position?.let { Library.songsAdapter?.updatePosition(newIndex = it) }
//            }
//        }
//    }


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