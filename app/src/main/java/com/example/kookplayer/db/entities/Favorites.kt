package com.example.kookplayer.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_table")
data class Favorites(
    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo var fId : Long,
    @ColumnInfo val songId: Long
) {
}