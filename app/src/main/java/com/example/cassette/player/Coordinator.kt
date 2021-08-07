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
        PlayerStateRepository.updatePlayingQueue()
        mediaPlayerAgent.playMusic(PlayerStateRepository.getNextMusic())
    }

    override fun playPrevSong()
    {
        mediaPlayerAgent.playMusic(PlayerStateRepository.getPrevMusic())
    }

    override fun playerIsPlaying(): Boolean
    {
        return mediaPlayerAgent.isPlaying() ?: false
    }

    override fun pauseSong()
    {
        mediaPlayerAgent.pauseMusic()
    }

    override fun resumePlaying()
    {
        mediaPlayerAgent.resumePlaying()
    }

    override fun changePlayerMode(newMode: PlayerStateRepository.PlayerModes)
    {
//        PlayerStateRepository.playingQueue.clear()
        PlayerStateRepository.updatePlayingQueue()
        PlayerStateRepository.currentPlayerMode = newMode

    }

    override fun playSelectedSong(song: SongModel, position: Int) {
        PlayerStateRepository.currentMusicPosition = position
        mediaPlayerAgent.playMusic(song)
    }


    fun getCurrentMediaPlayerPosition(): Int
    {
        return mediaPlayerAgent.getCurrentPosition()
    }

    fun getMediaPlayerDuration(): Int{
        return mediaPlayerAgent.getDuration()
    }

    fun getCurrentPlayingSong(): SongModel
    {
        return mediaPlayerAgent.getCurrentPlayingSong()
    }

}