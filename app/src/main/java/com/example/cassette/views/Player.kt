package com.example.cassette.views

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.widget.ImageView
import com.example.cassette.models.SongModel
import com.example.cassette.utlis.ImageUtils

class Player(context: Context, imageView: ImageView)
{

    val context = context
    val songImageViewInPanel: ImageView = imageView

    fun playMusic(song: SongModel) {

        loadSongImage(song, songImageViewInPanel)

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

    fun loadSongImage(song: SongModel, imageView: ImageView)
    {
        ImageUtils.loadImageToImageView(context, imageView, song.image)
    }
}