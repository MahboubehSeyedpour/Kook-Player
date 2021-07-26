package com.example.cassette.views

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import com.example.cassette.models.SongModel
import com.example.cassette.utlis.ImageUtils

class Player(context: Context, imageView: ImageView, textView: TextView)
{

    val context = context
    val songImageViewInPanel: ImageView = imageView
    val songTitleTextView : TextView = textView

    fun playMusic(song: SongModel) {

        loadSongImage(song.image, songImageViewInPanel)
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

    fun seekTo()
    {
//        TODO()
    }

    fun loadSongImage(image: Bitmap, imageView: ImageView)
    {
        ImageUtils.loadImageToImageView(context, imageView, image)
    }

    fun loadSongTitle(title: String, txtView: TextView)
    {
        txtView.text=title
    }
}