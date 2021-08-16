package com.example.cassette.player

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.example.cassette.`interface`.MediaPlayerCoordinatorInterface
import com.example.cassette.models.SongModel
import com.example.cassette.player.PlayerStateRepository.PlayerModes.*
import com.example.cassette.player.PlayerStateRepository.currentPlayerMode
import com.example.cassette.views.Fragments.Library

object Coordinator : MediaPlayerCoordinatorInterface {

    private lateinit var mediaPlayerAgent: MediaPlayerAgent
    private lateinit var nowPlayingQueue: ArrayList<SongModel>
    private var currentMusicPosition: Int = Library.songsAdapter?.getCurrentPosition() ?: -1


    fun setupMediaPlayerAgent(context: Context, imageView: ImageView, textView: TextView) {
        mediaPlayerAgent = MediaPlayerAgent(context, imageView, textView)
    }

    override fun playNextSong() {
        mediaPlayerAgent.playMusic(getNextMusic())
    }

    fun getNextMusic(): SongModel {
        return when (currentPlayerMode) {
            REPEAT_ALL -> nowPlayingQueue[++currentMusicPosition]
            REPEAT_ONE -> getCurrentPlayingSong()
            SHUFFLE -> nowPlayingQueue[++currentMusicPosition]
        }
    }

    override fun playPrevSong() {
        mediaPlayerAgent.playMusic(PlayerStateRepository.getPrevMusic())
    }

    override fun playerIsPlaying(): Boolean {
        return mediaPlayerAgent.isPlaying() ?: false
    }

    override fun pauseSong() {
        mediaPlayerAgent.pauseMusic()
    }

    override fun resumePlaying() {
        mediaPlayerAgent.resumePlaying()
    }

    override fun changePlayerMode(newMode: PlayerStateRepository.PlayerModes) {
        currentPlayerMode = newMode
        nowPlayingQueue = updateNowPlayingQueue(newMode)
    }

    private fun updateNowPlayingQueue(newMode: PlayerStateRepository.PlayerModes): ArrayList<SongModel> {

        when (newMode) {
            SHUFFLE -> {
                return Library.viewModel.getDataSet().toList().shuffled() as ArrayList<SongModel>
            }
            REPEAT_ALL -> Library.viewModel.getDataSet()
            else -> Library.viewModel.getDataSet().shuffle()
        }
        return arrayListOf()
    }

    override fun playSelectedSong(song: SongModel, position: Int) {
        PlayerStateRepository.currentMusicPosition = position
        mediaPlayerAgent.playMusic(song)
    }


    fun getCurrentMediaPlayerPosition(): Int {
        return mediaPlayerAgent.getCurrentPosition()
    }

    fun getMediaPlayerDuration(): Int {
        return mediaPlayerAgent.getDuration()
    }

    fun getCurrentPlayingSong(): SongModel {
        return mediaPlayerAgent.getCurrentPlayingSong()
    }
}