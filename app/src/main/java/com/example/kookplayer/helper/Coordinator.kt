package com.example.kookplayer.helper

import android.content.Context
import android.support.v4.media.session.PlaybackStateCompat
import com.example.kookplayer.R
import com.example.kookplayer.db.entities.SongModel
import com.example.kookplayer.myInterface.CoordinatorInterface
import com.example.kookplayer.views.Fragments.LibraryFragment.Library.songsAdapter
import com.example.kookplayer.views.activities.MainActivity

object Coordinator : CoordinatorInterface {
    override lateinit var nowPlayingQueue: ArrayList<SongModel>

    override lateinit var mediaPlayerAgent: MediaPlayerAgent

    var SourceOfSelectedSong = "library" // or «playlist-name» or "fav"
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


    // -------------------------------------- Commands from buttons in UI --------------------------------------
    override fun playNextSong() {

        takeActionBasedOnRepeatMode(
            MainActivity.activity.getString(R.string.onBtnClicked),
            MainActivity.activity.getString(R.string.play_next)
        )

    }

    override fun playPrevSong() {

        takeActionBasedOnRepeatMode(
            MainActivity.activity.getString(R.string.onBtnClicked),
            MainActivity.activity.getString(R.string.play_prev)
        )
    }


    // -------------------------------------- media player functions --------------------------------------
    override fun pause() {
        mediaPlayerAgent.pauseMusic()
    }


    override fun play(song: String) {

        mediaPlayerAgent.playMusic(song)

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

    fun takeActionBasedOnRepeatMode(actionSource: String, requestedAction: String) {

        when (repeatMode) {
            PlaybackStateCompat.REPEAT_MODE_ONE -> {

                currentPlayingSong?.data?.let { play(it) }

            }
            PlaybackStateCompat.REPEAT_MODE_ALL -> {

                when (requestedAction) {
                    MainActivity.activity.getString(R.string.play_next) -> {
                        if (!hasNext()) {
                            position = -1
                        }
                        getNextSong().data?.let { play(it) }
                        updatePlayerVar(nowPlayingQueue[position])
                    }
                    MainActivity.activity.getString(R.string.play_prev) -> {
                        if (!hasPrev()) {
                            position = nowPlayingQueue.size
                        }
                        getPrevSong().data?.let { play(it) }
                        updatePlayerVar(nowPlayingQueue[position])
                    }
                }

            }
            PlaybackStateCompat.REPEAT_MODE_NONE -> {

                when (actionSource) {
                    MainActivity.activity.getString(R.string.onSongCompletion) -> {
                        when (requestedAction) {
                            MainActivity.activity.getString(R.string.play_next) -> {
                                if (!hasNext()) {
                                    mediaPlayerAgent.pauseMusic()
                                } else {
                                    getNextSong().data?.let { play(it) }
                                    updatePlayerVar(nowPlayingQueue[position])
                                }
                            }
                        }
                    }
                    MainActivity.activity.getString(R.string.onBtnClicked) -> {
                        when (requestedAction) {
                            MainActivity.activity.getString(R.string.play_next) -> {
                                if (!hasNext()) {
//                                    resetPosition
                                    position = -1
                                }
                                getNextSong().data?.let { play(it) }
                                updatePlayerVar(nowPlayingQueue[position])

                            }
                            MainActivity.activity.getString(R.string.play_prev) -> {
                                if (!hasPrev()) {
//                                    resetPosition
                                    position = nowPlayingQueue.size
                                }
                                getPrevSong().data?.let { play(it) }
                                updatePlayerVar(nowPlayingQueue[position])
                            }
                        }

                    }
                }
            }
        }
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
            PlaybackStateCompat.SHUFFLE_MODE_NONE -> {
                nowPlayingQueue = currentDataSource
                updateCurrentPlayingSongPosition()
            }
            PlaybackStateCompat.SHUFFLE_MODE_ALL -> {

                val lst = nowPlayingQueue.toList()
                val sh_lst = lst.shuffled()
                val p = sh_lst as ArrayList<SongModel>

                nowPlayingQueue = p
                updateCurrentPlayingSongPosition()
            }
        }
    }

    fun updateCurrentPlayingSongPosition() {
        position = nowPlayingQueue.indexOf(currentPlayingSong)
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
        return nowPlayingQueue.indexOf(currentPlayingSong)
    }
}