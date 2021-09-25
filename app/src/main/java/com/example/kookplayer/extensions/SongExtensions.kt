package com.example.kookplayer.extensions

import com.example.kookplayer.db.entities.SongModel
import com.example.kookplayer.repositories.RoomRepository

fun SongModel.isFavorite(): Boolean
{
    for (favSongs in RoomRepository.cachedFavArray) {
        if (favSongs.id!!.equals(this.id)) {
            return true
        }
    }
    return false
}