package com.example.kookplayer.repositories.appdatabase.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistModel(@ColumnInfo var name: String = "",
                    @ColumnInfo var countOfSongs: Int = 0,
                    @ColumnInfo var songs: String
) {
    @PrimaryKey(autoGenerate = true ) var id: Long = 0
}
//@Entity(tableName = "playlist_table",
//    foreignKeys = arrayOf(ForeignKey(entity = Favorites::class,
//        parentColumns = arrayOf("fId"),
//        childColumns = arrayOf("songId"),
//        onDelete = ForeignKey.SET_NULL)))
//data class PlaylistModel(@ColumnInfo var name: String = "",
//                         @ColumnInfo var countOfSongs: Int = 0,
//                         @ColumnInfo var songs: String
//) {
//    @PrimaryKey(autoGenerate = true ) var id: Long = 0
//}