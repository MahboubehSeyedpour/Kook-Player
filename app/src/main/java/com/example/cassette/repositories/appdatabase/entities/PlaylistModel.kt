package com.example.cassette.repositories.appdatabase.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.cassette.repositories.appdatabase.roomdb.Converters

@Entity(tableName = "playlist_table")
data class PlaylistModel(@ColumnInfo var name: String = "",
                    @ColumnInfo var countOfSongs: Int = 0,
                    @ColumnInfo var songs: ArrayList<String> = arrayListOf()
) {
    @PrimaryKey(autoGenerate = true ) var id: Long = 0
}