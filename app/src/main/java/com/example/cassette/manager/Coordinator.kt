package com.example.cassette.manager

import android.content.Context
import com.example.cassette.`interface`.CoordinatorInterface
import com.example.cassette.models.SongModel
import com.example.cassette.player.Enums
import com.example.cassette.player.Enums.PlayingOrder.REPEAT_ALL
import com.example.cassette.player.Enums.PlayingOrder.SHUFFLE
import com.example.cassette.player.MediaPlayerAgent
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
        if (hasNext())
            play(getNextSong())
        else
            mediaPlayerAgent.stop()
    }

    override fun playPrevSong() {
        if (hasPrev())
            play(getPrevSong())
        else
            mediaPlayerAgent.stop()
    }

    override fun pause() {
        mediaPlayerAgent.pauseMusic()
    }

    override fun play(song: String) {
        mediaPlayerAgent.playMusic(song)
    }

    fun onSongCompletion() {
        mediaPlayerAgent.reset()
        playNextSong()
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

    override fun getCurrentSongPosition(): Int {
        return position
    }

    override fun playSelectedSong() {
        position = Library.songsAdapter?.getCurrentPosition() ?: -1
        play(getSongAtPosition(position))
    }

    override fun getPositionInPlayer(): Int {
        return mediaPlayerAgent.getCurrentPosition()
    }

    override fun hasNext(): Boolean {
        return position < Library.viewModel.getDataSet().size
    }

    override fun hasPrev(): Boolean {
        return position < 0
    }


    override fun getPrevSong(): String {
        return nowPlayingQueue[--position].data
    }

    override fun getNextSong(): String {
        return nowPlayingQueue[++position].data
    }

    override fun getSongAtPosition(position: Int): String {
        return nowPlayingQueue[position].data
    }

    override fun seekTo(newPosition: Int) {
        mediaPlayerAgent.seekTo(newPosition)
    }

    override fun stop() {
        mediaPlayerAgent.stop()
    }

    override fun release() {
//        TODO("Not yet implemented")
    }

}