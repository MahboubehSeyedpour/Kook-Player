package com.example.kookplayer.repositories

import com.example.kookplayer.db.entities.Favorites
import com.example.kookplayer.db.entities.SongModel
import com.example.kookplayer.views.Fragments.LibraryFragment
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

//    fun unFavSong(id: Long) {
//        val favSong = Favorites(id)
//        applicationScope.launch {
//            DatabaseRepository.localDatabase.favoriteDao().deleteSong(favSong)
//
//            cashedFavArray =
//                DatabaseRepository.localDatabase.favoriteDao().getFavs() as ArrayList<Favorites>
//        }
//    }

    fun getFavSongs(): ArrayList<SongModel> {

        return RoomRepository.cachedFavArray

    }

    fun songsIdToSongModelConverter(favSong: Favorites): SongModel? {
        val allSongsInStorage = LibraryFragment.viewModel.getDataSet()

        for (song in allSongsInStorage) {
            if (song.id == favSong.songId) {
                return song
            }
        }
        return null
    }

}