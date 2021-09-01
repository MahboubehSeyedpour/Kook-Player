package com.example.cassette.manager

import android.content.Context
import com.example.cassette.myInterface.CoordinatorInterface
import com.example.cassette.repositories.appdatabase.entities.SongModel
import com.example.cassette.player.Enums
import com.example.cassette.player.Enums.PlayingOrder.REPEAT_ALL
import com.example.cassette.player.Enums.PlayingOrder.SHUFFLE
import com.example.cassette.player.MediaPlayerAgent
import com.example.cassette.views.Fragments.LibraryFragment
import com.example.cassette.views.Fragments.LibraryFragment.Library.songsAdapter
import com.example.cassette.views.Fragments.LibraryFragment.Library.viewModel

object Coordinator : CoordinatorInterface {
    override lateinit var nowPlayingQueue: ArrayList<SongModel>
    override lateinit var playingOrder: Enums.PlayingOrder
    override lateinit var mediaPlayerAgent: MediaPlayerAgent
    override var position: Int = songsAdapter?.getCurrentPosition() ?: -1

//    lateinit var notificationManager: NotificationManager

    override fun setup(context: Context) {
        mediaPlayerAgent = MediaPlayerAgent(context)
    }


//    fun notificationSetup(context: Context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createChannel(context)
//            context.registerReceiver(broadcastReceiver, IntentFilter("TRACKS_TRACKS"))
//            context.startService(Intent(context, OnClearFromRecentService::class.java))
//        }
//    }

//    private fun createChannel(context: Context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                context.getString(R.string.notification_channel_id),
//                "KOD Dev", NotificationManager.IMPORTANCE_LOW
//            )
//            notificationManager = context.getSystemService(NotificationManager::class.java)!!
//            if (notificationManager != null) {
//                notificationManager.createNotificationChannel(channel)
//            }
//        }
//    }

//    var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            val action = intent.extras!!.getString("actionname")
//            when (action) {
//                context.getString(R.string.notification_action_previous) -> onTrackPrevious(context)
//                context.getString(R.string.notification_action_play) -> if (isPlaying()) {
//                    onTrackPause(context)
//                } else {
//                    onTrackPlay(context)
//                }
//                context.getString(R.string.notification_action_next) -> onTrackNext(context)
//            }
//        }
//    }

    override fun initNowPlayingQueue() {
//        TODO("check for last playing mode")
        playingOrder = REPEAT_ALL
        nowPlayingQueue = viewModel.getDataSet()
    }

    override fun playNextSong() {
        if (hasNext())
            getNextSongData()?.let { play(it) }
        else
            mediaPlayerAgent.stop()
    }

    override fun playPrevSong() {
        if (hasPrev())
            getPrevSongData()?.let { play(it) }
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
                LibraryFragment.viewModel.getDataSet().toList().shuffled() as ArrayList<SongModel>
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
        return LibraryFragment.viewModel.getDataSet()[position]
    }

    override fun getCurrentSongPosition(): Int {
        return position
    }

    override fun playSelectedSong() {
        position = LibraryFragment.songsAdapter?.getCurrentPosition() ?: -1
        getSongAtPosition(position)?.let { play(it) }
    }

    override fun getPositionInPlayer(): Int {
        return mediaPlayerAgent.getCurrentPosition()
    }

    override fun hasNext(): Boolean {
        return position < LibraryFragment.viewModel.getDataSet().size
    }

    override fun hasPrev(): Boolean {
        return position < 0
    }


    override fun getPrevSongData(): String? {
        return nowPlayingQueue[--position].data ?: ""
    }

    override fun getPrevSong(): SongModel {
        return nowPlayingQueue[--position]
    }

    override fun getNextSongData(): String? {
        return nowPlayingQueue[++position].data ?: ""
    }

    override fun getNextSong(): SongModel {
        return nowPlayingQueue[++position]
    }

    override fun getSongAtPosition(position: Int): String? {
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