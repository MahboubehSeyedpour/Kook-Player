package com.example.kookplayer.myInterface

import com.example.kookplayer.db.entities.PlaylistModel

interface IPlaylistRepo {

//    val localDatabase: MyDatabase


    fun createPlaylist(name: String)
//    fun addSongsToPlaylist(playlist_name: String, songsId: String): Boolean


    //    TODO(fun renamePlaylist())
    fun getPlaylists(): ArrayList<PlaylistModel>
    fun getIdOfSongsStoredInPlaylist(plylist_id: Long): String
    fun getPlaylistId(name: String): Long

    //    fun updateDatabase(): Boolean
    fun removePlaylist(id: Long): Boolean

    //    fun getPlaylistFromStorage(): ArrayList<PlaylistModel>
//    fun getPlaylistFromDatabase(): ArrayList<PlaylistModel>

    //    utils
    fun getIdByName(name: String): Long
    fun getPlaylistById(id: Long): PlaylistModel?


//    fun updateTable()

}