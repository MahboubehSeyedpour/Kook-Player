package com.example.kookplayer.myInterface

import com.example.kookplayer.db.entities.SongModel

interface PlaylistPageRepositoryInterface
{
    fun getSongsIdFromDatabase(): String
    fun songsIdToSongModelConverter(songId: String): SongModel?
    fun getSongs(): ArrayList<SongModel>
}