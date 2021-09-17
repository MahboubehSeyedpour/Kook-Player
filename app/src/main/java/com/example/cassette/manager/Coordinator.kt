package com.example.cassette.manager

import android.content.Context
import android.os.Build
import android.support.v4.media.session.PlaybackStateCompat
import androidx.annotation.RequiresApi
import com.example.cassette.myInterface.CoordinatorInterface
import com.example.cassette.player.Enums
import com.example.cassette.player.MediaPlayerAgent
import com.example.cassette.repositories.appdatabase.entities.SongModel
import com.example.cassette.services.NotificationPlayerService
import com.example.cassette.views.Fragments.LibraryFragment.Library.songsAdapter
import com.example.cassette.views.MainActivity

object Coordinator : CoordinatorInterface {
    override lateinit var nowPlayingQueue: ArrayList<SongModel>

    override lateinit var mediaPlayerAgent: MediaPlayerAgent

    var SourceOfSelectedSong = "library" // or «playlist-name»
    var currentDataSource = arrayListOf<SongModel>()

    var shuffleMode = PlaybackStateCompat.SHUFFLE_MODE_NONE
    var repeatMode = PlaybackStateCompat.REPEAT_MODE_NONE


    var currentPlayingSong: SongModel? = null
        set(value) {
            field = value
            MainActivity.playerPanelFragment.updatePanel()
        }

    override var position: Int = songsAdapter?.getCurrentPosition() ?: -1


    // -------------------------------------- setup media player --------------------------------------

    override fun setup(context: Context) {
        mediaPlayerAgent = MediaPlayerAgent(context)
    }

    // -------------------------------------- media player state --------------------------------------

    override fun isPlaying(): Boolean {
        return mediaPlayerAgent.isPlaying()
    }

    fun getShuffleStatus(): Int {
        return shuffleMode
    }

    fun getRepeatOneStatus(): Int {
        return repeatMode
    }


    // -------------------------------------- Commands from buttons in UI --------------------------------------
    @RequiresApi(Build.VERSION_CODES.O)
    override fun playNextSong() {

        if (hasNext() && repeatMode != PlaybackStateCompat.REPEAT_MODE_ONE) {

            updatePlayerVar(nowPlayingQueue[position + 1])
            getNextSong().data?.let { play(it) }


        } else {

            takeActionBasedOnRepeateMode()

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun playPrevSong() {

        if (hasPrev() && repeatMode != PlaybackStateCompat.REPEAT_MODE_ONE) {

            updatePlayerVar(nowPlayingQueue[position -1])
            getPrevSong().data?.let { play(it) }


        } else {

            takeActionBasedOnRepeateMode()

        }
    }


    // -------------------------------------- media player functions --------------------------------------
    override fun pause() {
        mediaPlayerAgent.pauseMusic()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun play(song: String) {

        mediaPlayerAgent.playMusic(song)

        position = getCurrentSongPosition()

    }

    override fun resume() {
        mediaPlayerAgent.resumePlaying()
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


    // -------------------------------------- event based functions --------------------------------------

    fun takeActionBasedOnRepeateMode() {

        when (repeatMode) {
            PlaybackStateCompat.REPEAT_MODE_ONE -> {

                currentPlayingSong?.data?.let { play(it) }

            }
            PlaybackStateCompat.REPEAT_MODE_ALL -> {

                if (hasNext()) {
                    getNextSong().data?.let { play(it) }
                } else {
                    position = -1
                    getNextSong().data?.let { play(it) }
                }
                updatePlayerVar(nowPlayingQueue[position])
            }
            PlaybackStateCompat.REPEAT_MODE_NONE -> {

                mediaPlayerAgent.pauseMusic()

            }
        }
    }

    override fun changePlayingMode(order: Enums.PlayingOrder) {

        updateNowPlayingQueue()
    }


    // -------------------------------------- utils --------------------------------------

    override fun updateNowPlayingQueue() {

//      TODO(shuffle as an extension not a function)
//
//        viewModel.getDataSet()
        when (repeatMode) {
            PlaybackStateCompat.REPEAT_MODE_ONE -> nowPlayingQueue =
                arrayListOf(currentPlayingSong!!)
            PlaybackStateCompat.REPEAT_MODE_ALL -> nowPlayingQueue = currentDataSource
            PlaybackStateCompat.REPEAT_MODE_NONE -> nowPlayingQueue = currentDataSource

        }

        when (shuffleMode) {
            PlaybackStateCompat.SHUFFLE_MODE_NONE -> nowPlayingQueue = currentDataSource
            PlaybackStateCompat.SHUFFLE_MODE_ALL -> {

                val lst = currentDataSource.toList()
                val sh_lst = lst.shuffled()
                val p = sh_lst as ArrayList<SongModel>

                nowPlayingQueue = p
            }
        }

        val i = 0
    }

    override fun getCurrentSongPosition(): Int {
        return position
    }

    override fun playSelectedSong(song: SongModel) {

        updatePlayerVar(song)

        updateNowPlayingQueue()

        song.data?.let { play(it) }

        position = getPositionInNowPlayingQueue() ?: -1

    }

    fun updatePlayerVar(song: SongModel) {
        currentPlayingSong = song
        MainActivity.playerPanelFragment.updatePanel()
    }

    override fun getPositionInPlayer(): Int {
        return mediaPlayerAgent.getCurrentPosition()
    }

    override fun hasNext(): Boolean {
        return position + 1 < nowPlayingQueue.size
    }

    override fun hasPrev(): Boolean {
        return position > 0
    }


    override fun getPrevSongData(): String? {
        return nowPlayingQueue[--position].data
    }


    override fun getPrevSong(): SongModel {
        position -= 1
        val p = getPositionInNowPlayingQueue()
        return nowPlayingQueue[position]
    }

    override fun getNextSongData(): String? {
        return nowPlayingQueue[++position].data
    }

    override fun getNextSong(): SongModel {
        position += 1
        val p = getPositionInNowPlayingQueue()
        return nowPlayingQueue[position]
    }

    override fun getSongAtPosition(position: Int): String? {
        return nowPlayingQueue[position].data
    }

    fun getPositionInNowPlayingQueue(): Int {
        for (song in nowPlayingQueue) {
            if (song.id == currentPlayingSong?.id) {
                return nowPlayingQueue.indexOf(song)
            }
        }
        return -1
    }
}