package com.example.cassette.player

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import com.example.cassette.models.SongModel
import com.example.cassette.utlis.ImageUtils

class Player_progressbar(context: Context, imageView: ImageView, textView: TextView) {

    val context = context
    val songImageViewInPanel: ImageView = imageView
    val songTitleTextView: TextView = textView

    fun playMusic(song: SongModel) {

        song.image?.let { loadSongImage(it, songImageViewInPanel) }
        loadSongTitle(song.title, songTitleTextView)

        val uri: Uri = Uri.parse(song.data)

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

    fun seekTo() {
//        TODO()
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
}