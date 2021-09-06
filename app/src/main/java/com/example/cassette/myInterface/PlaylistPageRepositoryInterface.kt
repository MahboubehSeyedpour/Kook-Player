package com.example.cassette.myInterface

import com.example.cassette.repositories.appdatabase.entities.SongModel

interface PlaylistPageRepositoryInterface
{
    fun getSongsIdFromDatabase(): String
    fun songsIdToSongModelConverter(songId: String): SongModel?
    fun getSongs(): ArrayList<SongModel>
}