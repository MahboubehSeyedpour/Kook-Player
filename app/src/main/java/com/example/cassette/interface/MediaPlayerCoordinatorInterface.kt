package com.example.cassette.`interface`

import com.example.cassette.models.SongModel
import com.example.cassette.player.PlayerStateRepository

interface MediaPlayerCoordinatorInterface
{
//    val mediaPlayerAgent: MediaPlayerAgent

    fun playNextSong()

    fun playPrevSong()

    fun playerIsPlaying(): Boolean

    fun pauseSong()

    fun resumePlaying()

    fun changePlayerMode(newMode: PlayerStateRepository.PlayerModes)

    fun playSelectedSong(song: SongModel)
}