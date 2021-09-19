package com.example.cassette.repositories

import com.example.cassette.repositories.appdatabase.entities.Favorites
import com.example.cassette.repositories.appdatabase.entities.SongModel
import com.example.cassette.repositories.appdatabase.roomdb.DatabaseRepository
import com.example.cassette.views.Fragments.LibraryFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class FavRepository {

    val applicationScope = CoroutineScope(SupervisorJob())

    companion object {
        var cashedFavArray = DatabaseRepository.cashedFavArray
    }

    init {
        applicationScope.launch {
            cashedFavArray =
                DatabaseRepository.cashedFavArray
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

        return DatabaseRepository.cashedFavArray

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