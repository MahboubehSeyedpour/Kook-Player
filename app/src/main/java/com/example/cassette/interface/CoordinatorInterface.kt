package com.example.cassette.`interface`

import android.content.Context
import com.example.cassette.models.SongModel
import com.example.cassette.player.Enums
import com.example.cassette.player.MediaPlayerAgent

interface CoordinatorInterface {

    var playingOrder: Enums.PlayingOrder
    var nowPlayingQueue: ArrayList<SongModel>
    var mediaPlayerAgent: MediaPlayerAgent
    var position: Int

    fun setup(context: Context)


//    player
    fun initNowPlayingQueue() //logic for shuffle , repeat all or repeat one
    fun playNextSong()
    fun playPrevSong()
    fun pause() //pause current playing song
    fun play(position: Int) //play new song
    fun resume() //resume current playing song
    fun stop()


//    update
    fun updateNowPlayingQueue()
    fun changePlayingMode(order : Enums.PlayingOrder) // update State & shuffle , repeat all or repeat one


//   get Status
    fun isPlaying(): Boolean


//    get Info
    fun getCurrentPlayingSong(): SongModel
    fun getCurrentSongPosition(): Int
    fun playSelectedSong()
    fun getPositionInPlayer(): Int


//    seek bar
    fun seekTo(newPosition: Int)


}