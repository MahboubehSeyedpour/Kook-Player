package com.example.cassette.player

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.example.cassette.`interface`.MediaPlayerCoordinatorInterface
import com.example.cassette.models.SongModel

object Coordinator : MediaPlayerCoordinatorInterface {

    private lateinit var mediaPlayerAgent : MediaPlayerAgent

    fun setupMediaPlayerAgent(context: Context, imageView: ImageView, textView: TextView)
    {
        mediaPlayerAgent = context?.let { MediaPlayerAgent(it, imageView, textView) }
    }

    override fun playNextSong()
    {
        mediaPlayerAgent?.playMusic(PlayerStateRepository.getNextMusic())
    }

    override fun playPrevSong()
    {
        mediaPlayerAgent?.playMusic(PlayerStateRepository.getNextMusic())
    }

    override fun playerIsPlaying(): Boolean
    {
        return mediaPlayerAgent?.isPlaying() ?: false
    }

    override fun pauseSong()
    {
        mediaPlayerAgent?.pauseMusic()
    }

    override fun resumePlaying()
    {
        mediaPlayerAgent.resumePlaying()
    }

    override fun changePlayerMode(newMode: PlayerStateRepository.PlayerModes)
    {
        PlayerStateRepository.currentPlayerMode = newMode
    }

    override fun playSelectedSong(song: SongModel) {
        mediaPlayerAgent.playMusic(song)
    }
}