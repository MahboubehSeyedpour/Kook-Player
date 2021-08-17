package com.example.cassette.player

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import com.example.cassette.models.SongModel
import com.example.cassette.utlis.ImageUtils

class MediaPlayerAgent(context: Context) {


    private val context = context
    private var mediaPlayer = MediaPlayer()

    //    private val songImageViewInPanel: ImageView = imageView
//    private val songTitleTextView: TextView = textView
    private lateinit var currentPlayingSong: SongModel


    fun playMusic(data: String) {

//        song.image?.let { loadSongImage(it, songImageViewInPanel) }
//        loadSongTitle(song.title, songTitleTextView)

        val uri: Uri = Uri.parse(data)

        mediaPlayer.release()

        mediaPlayer = MediaPlayer.create(context, uri)
        mediaPlayer.start()

//        currentPlayingSong = song

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
}