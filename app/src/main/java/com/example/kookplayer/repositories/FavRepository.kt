package com.example.kookplayer.repositories

import com.example.kookplayer.db.entities.SongModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class FavRepository {

    val applicationScope = CoroutineScope(SupervisorJob())

    companion object {
        var cashedFavArray = RoomRepository.cachedFavArray
    }

    init {
        applicationScope.launch {
            cashedFavArray =
                RoomRepository.cachedFavArray
        }
    }

    fun getFavSongs(): ArrayList<SongModel> {

        return RoomRepository.cachedFavArray

    }

}