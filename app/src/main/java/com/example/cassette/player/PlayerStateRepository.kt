package com.example.cassette.player

import com.example.cassette.models.SongModel

object PlayerStateRepository
{
    lateinit var currentPlayerMode: PlayerModes
    lateinit var palyingQueue : ArrayList<SongModel>
    var mediaPlayerState : Boolean = false
    var currentMusicPosition : Int = -1

    enum class PlayerModes(mode: String) {
        SHUFFLE("shuffle"),
        NORMAL("normal"),
        REPEAT_ONE("repeat_one"),
        REPEAT_ALL("repeat_all")
    }



    fun getNextMusic() : SongModel
    {
        return palyingQueue[++currentMusicPosition]
    }

    fun getPrevMusic() : SongModel
    {
        return palyingQueue[--currentMusicPosition]
    }

}