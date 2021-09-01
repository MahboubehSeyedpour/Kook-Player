package com.example.cassette.repositories.appdatabase.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cassette.repositories.appdatabase.entities.PlaylistModel

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPlaylist(playlist: PlaylistModel)


    @Query("DELETE FROM playlist_table WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Long)


    @Query("DELETE FROM playlist_table")
    suspend fun deleteAll()


    @Query("SELECT * FROM playlist_table")
    fun getPlaylists(): List<PlaylistModel>


    @Query("UPDATE playlist_table SET songs = :songs & countOfSongs = :count WHERE id = :id")
    suspend fun addSongToPlaylist(id: Long, songs: ArrayList<String>, count: Int)

    @Query("SELECT songs FROM playlist_table WHERE id = :id")
    suspend fun getSongsOfPlaylist(id: Long): List<String>
}