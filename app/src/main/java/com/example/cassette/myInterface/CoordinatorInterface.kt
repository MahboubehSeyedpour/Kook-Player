package com.example.cassette.myInterface

import android.content.Context
import com.example.cassette.player.Enums
import com.example.cassette.player.MediaPlayerAgent
import com.example.cassette.repositories.appdatabase.entities.SongModel

interface CoordinatorInterface {

//    var playingOrder: Enums.PlayingOrder
    var nowPlayingQueue: ArrayList<SongModel>
    var mediaPlayerAgent: MediaPlayerAgent
    var position: Int

    fun setup(context: Context)


//    player
    fun playNextSong()
    fun playPrevSong()
    fun pause() //pause current playing song
    fun play(song: String) //play new song
    fun resume() //resume current playing song
    fun stop()
    fun release()


//    update
    fun updateNowPlayingQueue()
    fun changePlayingMode(order : Enums.PlayingOrder) // update State & shuffle , repeat all or repeat one


//   get Status
    fun isPlaying(): Boolean


//    get Info
//    fun getCurrentPlayingSong(): SongModel
    fun getCurrentSongPosition(): Int
    fun playSelectedSong(song: SongModel)
    fun getPositionInPlayer(): Int
    fun hasNext(): Boolean
    fun hasPrev(): Boolean
    fun getPrevSongData(): String?
    fun getNextSongData(): String?
    fun getNextSong(): SongModel
    fun getPrevSong(): SongModel
    fun getSongAtPosition(position: Int): String?


//    seek bar
    fun seekTo(newPosition: Int)


}