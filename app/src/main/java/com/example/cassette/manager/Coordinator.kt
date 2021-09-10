package com.example.cassette.manager

import android.content.Context
import android.support.v4.media.session.PlaybackStateCompat
import com.example.cassette.myInterface.CoordinatorInterface
import com.example.cassette.player.Enums
import com.example.cassette.player.MediaPlayerAgent
import com.example.cassette.repositories.appdatabase.entities.SongModel
import com.example.cassette.views.Fragments.LibraryFragment.Library.songsAdapter
import com.example.cassette.views.Fragments.LibraryFragment.Library.viewModel
import com.example.cassette.views.MainActivity

object Coordinator : CoordinatorInterface {
    override lateinit var nowPlayingQueue: ArrayList<SongModel>

    //    override lateinit var playingOrder: Enums.PlayingOrder
    override lateinit var mediaPlayerAgent: MediaPlayerAgent

    var SourceOfSelectedSong = "library" // or «playlist-name»

    var shuffleMode = PlaybackStateCompat.SHUFFLE_MODE_NONE
    var repeatMode = PlaybackStateCompat.REPEAT_MODE_NONE


    var currentPlayingSong: SongModel? = null
        set(value) {
            field = value
            MainActivity.playerPanelFragment.updatePanel()
        }


    override var position: Int = songsAdapter?.getCurrentPosition() ?: -1


//    fun getCurrentPlayingSong(): SongModel {
//        return currentPlayingSong
//    }

//    fun setCurrentPlayingSong(song: SongModel) {
//        currentPlayingSong = song
//        MainActivity.playerPanelFragment.updatePanel()
//    }


    // -------------------------------------- setup media player --------------------------------------

    override fun setup(context: Context) {
        mediaPlayerAgent = MediaPlayerAgent(context)
    }

    fun setupPlayingQueue() {
        updateNowPlayingQueue()
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
    override fun playNextSong() {
        if (hasNext())
            getNextSong().data?.let { play(it) }
        else
            mediaPlayerAgent.stop()

        updatePlayerVar(nowPlayingQueue[position])
    }

    override fun playPrevSong() {
        if (hasPrev())
            getPrevSong().data?.let { play(it) }
        else
            mediaPlayerAgent.stop()

        updatePlayerVar(nowPlayingQueue[position])
    }


    // -------------------------------------- media player functions --------------------------------------
    override fun pause() {
        mediaPlayerAgent.pauseMusic()
    }

    override fun play(song: String) {

        mediaPlayerAgent.playMusic(song)

//        currentPlayingSong = viewModel.getDataSet()[position]

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

    fun onSongCompletion() {

        val np = nowPlayingQueue
        val p = position
        val s = nowPlayingQueue[position+1]
        val pre = nowPlayingQueue[position-1]

        when (repeatMode) {
            PlaybackStateCompat.REPEAT_MODE_ONE -> {
                currentPlayingSong?.data?.let { play(it) }
            }
            PlaybackStateCompat.REPEAT_MODE_ALL -> {

                if (hasNext()) {
                    playNextSong()
                } else {
                    position = -1
                    playNextSong()
                }

            }
            PlaybackStateCompat.REPEAT_MODE_NONE -> {
                if (hasNext()) {
                    playNextSong()
                } else {
                    mediaPlayerAgent.stop()
                }
            }
        }
    }

    override fun changePlayingMode(order: Enums.PlayingOrder) {

        updateNowPlayingQueue()
    }


    // -------------------------------------- utils --------------------------------------

    override fun updateNowPlayingQueue() {
//        when (playingOrder) {
////            TODO(shuffle as an extension not a function)
//            SHUFFLE -> nowPlayingQueue =
//                LibraryFragment.viewModel.getDataSet().toList().shuffled() as ArrayList<SongModel>
//            REPEAT_ALL -> nowPlayingQueue = viewModel.getDataSet()
//        }

//        if (currentPlayingSongIsValid()) {

        when (repeatMode) {
            PlaybackStateCompat.REPEAT_MODE_ONE -> nowPlayingQueue =
                arrayListOf(currentPlayingSong!!)
            PlaybackStateCompat.REPEAT_MODE_ALL -> nowPlayingQueue = viewModel.getDataSet()
            PlaybackStateCompat.REPEAT_MODE_NONE -> nowPlayingQueue = viewModel.getDataSet()

        }


        when (shuffleMode) {
            PlaybackStateCompat.SHUFFLE_MODE_NONE -> nowPlayingQueue = viewModel.getDataSet()
            PlaybackStateCompat.SHUFFLE_MODE_ALL ->
            {

                val lst = viewModel.getDataSet().toList()
                val sh_lst = lst.shuffled()
                val p = sh_lst as ArrayList<SongModel>

                nowPlayingQueue = p
            }

        }


//        }

    }

    fun currentPlayingSongIsValid(): Boolean {
        return currentPlayingSong != null
    }

    override fun getCurrentSongPosition(): Int {
        return position
    }

    override fun playSelectedSong(song: SongModel) {
        song.data?.let { play(it) }

        position = songsAdapter?.getCurrentPosition() ?: -1

        updatePlayerVar(song)
    }

    fun updatePlayerVar(song: SongModel) {
        currentPlayingSong = song
        MainActivity.playerPanelFragment.updatePanel()
    }

    override fun getPositionInPlayer(): Int {
        return mediaPlayerAgent.getCurrentPosition()
    }

    override fun hasNext(): Boolean {
        return position < viewModel.getDataSet().size
    }

    override fun hasPrev(): Boolean {
        return position - 1 > -1
    }


    override fun getPrevSongData(): String? {
        return nowPlayingQueue[--position].data ?: ""
    }


    override fun getPrevSong(): SongModel {
        position -= 1
        val p = songsAdapter?.getCurrentPosition()
        return nowPlayingQueue[position]
    }

    override fun getNextSongData(): String? {
        return nowPlayingQueue[++position].data ?: ""
    }

    override fun getNextSong(): SongModel {
        position += 1
        val p = songsAdapter?.getCurrentPosition()
        return nowPlayingQueue[position]
    }

    override fun getSongAtPosition(position: Int): String? {
        return nowPlayingQueue[position].data
    }


}