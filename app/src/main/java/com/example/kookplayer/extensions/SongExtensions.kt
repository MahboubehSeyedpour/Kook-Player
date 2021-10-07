package com.example.kookplayer.extensions

import com.example.kookplayer.db.entities.SongModel
import com.example.kookplayer.repositories.IRoomRepository

fun SongModel.isFavorite(): Boolean
{
    for (favSongs in IRoomRepository.cachedFavArray) {
        if (favSongs.id!!.equals(this.id)) {
            return true
        }
    }
    return false
}