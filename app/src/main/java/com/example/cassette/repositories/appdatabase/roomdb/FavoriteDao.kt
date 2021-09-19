package com.example.cassette.repositories.appdatabase.roomdb

import androidx.room.*
import com.example.cassette.repositories.appdatabase.entities.Favorites

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSong(song: Favorites)

    @Delete
    suspend fun deleteSong(song: Favorites)

    @Query("DELETE FROM playlist_table")
    suspend fun deleteAll()
}

//@Query("DELETE FROM favorites_table WHERE songId = :songId")