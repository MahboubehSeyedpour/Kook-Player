package com.example.cassette.player

import com.example.cassette.models.SongModel
import com.example.cassette.views.Fragments.Library

object PlayerStateRepository
{
    var currentPlayerMode: PlayerModes = PlayerModes.REPEAT_ALL
    lateinit var playingQueue : ArrayList<SongModel>
    var mediaPlayerState : Boolean = false
    var currentMusicPosition : Int = -1

    enum class PlayerModes(mode: String) {
        SHUFFLE("shuffle"),
        REPEAT_ONE("repeat_one"),
        REPEAT_ALL("repeat_all")
    }

    fun updatePlayingQueue()
    {
        when(currentPlayerMode)
        {
            PlayerModes.REPEAT_ALL -> playingQueue = Library.songsAdapter?.dataset!!
            PlayerModes.REPEAT_ONE -> playingQueue = arrayListOf(Library.songsAdapter?.dataset!![currentMusicPosition])
            PlayerModes.SHUFFLE ->
            {
                val vv = Library.songsAdapter?.dataset!!.toList().shuffled()
                playingQueue = vv.toCollection(playingQueue)

            }

        }
    }


    fun getNextMusic() : SongModel
    {
        return when(currentPlayerMode)
        {
            PlayerModes.REPEAT_ONE -> playingQueue[currentMusicPosition]
            else -> playingQueue[++currentMusicPosition]
        }
    }

    fun getPrevMusic() : SongModel
    {
        return when(currentPlayerMode)
        {
            PlayerModes.REPEAT_ONE -> playingQueue[currentMusicPosition]
            else -> playingQueue[--currentMusicPosition]
        }
    }

}