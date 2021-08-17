package com.example.cassette.player

import android.content.Context
import com.example.cassette.`interface`.CoordinatorInterface
import com.example.cassette.models.SongModel
import com.example.cassette.player.Enums.PlayingOrder.REPEAT_ALL
import com.example.cassette.player.Enums.PlayingOrder.SHUFFLE
import com.example.cassette.views.Fragments.Library
import com.example.cassette.views.Fragments.Library.Library.songsAdapter
import com.example.cassette.views.Fragments.Library.Library.viewModel

object Coordinator : CoordinatorInterface {


    override lateinit var nowPlayingQueue: ArrayList<SongModel>
    override lateinit var playingOrder: Enums.PlayingOrder
    override lateinit var mediaPlayerAgent: MediaPlayerAgent
    override var position: Int = songsAdapter?.getCurrentPosition() ?: -1

    override fun setup(context: Context) {
        mediaPlayerAgent = MediaPlayerAgent(context)
    }

    override fun initNowPlayingQueue() {
//        TODO("check for last playing mode")
        playingOrder = REPEAT_ALL
        nowPlayingQueue = viewModel.getDataSet()
    }

    override fun playNextSong() {
        play(++position)
    }

    override fun playPrevSong() {
        play(--position)
    }

    override fun pause() {
        mediaPlayerAgent.pauseMusic()
    }

    override fun play(position: Int) {
        mediaPlayerAgent.playMusic(nowPlayingQueue[position % viewModel.getDataSet().size].data)
    }

    override fun updateNowPlayingQueue() {
        when (playingOrder) {
//            TODO(shuffle as an extension not a function)
            SHUFFLE -> nowPlayingQueue =
                Library.viewModel.getDataSet().toList().shuffled() as ArrayList<SongModel>
            REPEAT_ALL -> nowPlayingQueue = viewModel.getDataSet()
        }
    }

    override fun changePlayingMode(order: Enums.PlayingOrder) {
        playingOrder = order
        updateNowPlayingQueue()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayerAgent.isPlaying()
    }

    override fun resume() {
        mediaPlayerAgent.resumePlaying()
    }

    override fun getCurrentPlayingSong(): SongModel {
        return Library.viewModel.getDataSet()[position]
    }

    override fun getNextSongPosition(): Int {
        return 1
    }

    override fun getCurrentSongPosition(): Int {
        position = Library.songsAdapter?.getCurrentPosition() ?: -1
        return position
    }

    override fun playSelectedSong() {
        position = Library.songsAdapter?.getCurrentPosition() ?: -1
        play(position)
    }

    override fun getPositionInPlayer(): Int {
        return mediaPlayerAgent.getCurrentPosition()
    }

    override fun seekTo(newPosition: Int) {
        mediaPlayerAgent.seekTo(newPosition)
    }

}