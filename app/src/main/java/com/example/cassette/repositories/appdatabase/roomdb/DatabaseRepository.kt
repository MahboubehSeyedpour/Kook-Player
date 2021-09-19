package com.example.cassette.repositories.appdatabase.roomdb

import androidx.lifecycle.lifecycleScope
import com.example.cassette.manager.Coordinator
import com.example.cassette.repositories.PlaylistRepository
import com.example.cassette.repositories.appdatabase.entities.Favorites
import com.example.cassette.repositories.appdatabase.entities.PlaylistModel
import com.example.cassette.repositories.appdatabase.entities.SongModel
import com.example.cassette.views.Fragments.FavoriteFragment
import com.example.cassette.views.Fragments.LibraryFragment
import com.example.cassette.views.MainActivity
import kotlinx.android.synthetic.*
import kotlinx.coroutines.*

object DatabaseRepository {

    private val applicationScope = CoroutineScope(SupervisorJob())
    lateinit var localDatabase: MyDatabase
    var cashedPlaylistArray = ArrayList<PlaylistModel>()
     var cashedFavArray_Favorites = ArrayList<Favorites>()
    var cashedFavArray = ArrayList<SongModel>()

    fun createDatabse() {
        localDatabase = MyDatabase.getDatabase(
            MainActivity.activity.baseContext!!,
            MainActivity.activity.lifecycleScope
        )
        applicationScope.launch {
            cashedPlaylistArray = getPlaylistFromDatabase()
            cashedFavArray_Favorites = getFavFromDatabase()
        }
    }

    fun updateCashedPlaylistArray()
    {
        GlobalScope.launch {
            cashedPlaylistArray = getPlaylistFromDatabase()
        }

    }

    fun addSongsToPlaylist(playlist_name: String, songsId: String): Boolean {

        val playlist = getPlaylistById(getIdByName(playlist_name))

//        add song to playlist object
        if (playlist != null) {

            val position = findPlaylistPositionInCachedArray(playlist)

            if (position >= 0) {
                addSongsToPlaylistInObject(
                    PlaylistRepository.cashedPlaylistArray[position],
                    songsId
                )
            }

            addSongsToPlaylistInDatabse(playlist, songsId)

        }

        return true
    }

    fun addSongsToPlaylistInObject(playlist: PlaylistModel, songsId: String) {

        val position = findPlaylistPositionInCachedArray(playlist)

        PlaylistRepository.cashedPlaylistArray[position].songs =
            PlaylistRepository.cashedPlaylistArray[position].songs + songsId + ","

        addCountInPlaylistObject(PlaylistRepository.cashedPlaylistArray[position])
    }

    fun addCountInPlaylistObject(playlist: PlaylistModel) {
        playlist.countOfSongs = playlist.countOfSongs + 1
    }

    fun addSongsToPlaylistInDatabse(playlist: PlaylistModel, songsId: String) {

        runBlocking {

            if (playlist != null) {
                for (song_id in songsId) {
                    localDatabase.playlistDao().addSongToPlaylist(
                        playlist.id,
                        playlist.songs
                    )
                }
                addCountInDatabase(playlist)
            }
//            TODO(Toast : operation failed! please try later)
        }
    }

    fun addCountInDatabase(playlist: PlaylistModel) {
        GlobalScope.launch {
            localDatabase.playlistDao()
                .setCountOfSongs(playlist.id, playlist.countOfSongs)
        }
    }

    fun findPlaylistPositionInCachedArray(playlist: PlaylistModel): Int {
        var position: Int = -1
        while (++position < PlaylistRepository.cashedPlaylistArray.size) {
            if (PlaylistRepository.cashedPlaylistArray[position].id == playlist.id) {
                return position
            }
        }
        return position
    }

    fun getIdByName(name: String): Long {

        for (playlist in PlaylistRepository.cashedPlaylistArray) {
            if (playlist.name == name) {
                return playlist.id
            }
        }
        return -1L
    }

    fun getPlaylistById(id: Long): PlaylistModel? {
        for (playlist in PlaylistRepository.cashedPlaylistArray) {
            if (playlist.id == id) {
                return playlist
            }
        }
        return null
    }

    fun getPlaylistFromDatabase(): ArrayList<PlaylistModel> =
        runBlocking {
            val playlistsList = localDatabase.playlistDao().getPlaylists()
            val arrayList = arrayListOf<PlaylistModel>()
            for (playlist in playlistsList) {
                arrayList.add(playlist)
            }
            return@runBlocking arrayList
        }

    fun addSongAsFav(songsId: Long) {
        val fav = Favorites(songsId)
        applicationScope.launch {
            localDatabase.favoriteDao().addSong(fav)
        }

        SongUtils.getSongById(songsId)?.let { cashedFavArray.add(it) }
    }

    fun deleteSongFromFav(song: SongModel) {
        val fav = Favorites(song.id!!)
        applicationScope.launch {
            localDatabase.favoriteDao().deleteSong(fav)
        }

        for (favSongs in cashedFavArray)
        {
            if(favSongs.id!! == song!!.id)
            {
                cashedFavArray.remove(favSongs)
            }
        }
    }

    fun songIsAlreadyLiked(song: SongModel): Boolean
    {
        for (favSongs in cashedFavArray) {
            if (favSongs.id!!.equals(song.id)) {
                return true
            }
        }
        return false
    }

    fun getFavFromDatabase(): ArrayList<Favorites> =
        runBlocking {
            val favSongs = localDatabase.favoriteDao().getFavs()

            val arr = arrayListOf<Favorites>()
            arr.addAll(favSongs)
            return@runBlocking arr
        }

    fun convertFavSongsToRealSongs(): ArrayList<SongModel>
    {

        val arrayList = arrayListOf<SongModel>()
        for (favSong in cashedFavArray_Favorites) {

            val realSong = songsIdToSongModelConverter(favSong)
            if (realSong != null)
                arrayList.add(realSong)
        }

        cashedFavArray = arrayList
        return arrayList

    }

    private fun songsIdToSongModelConverter(favSong: Favorites): SongModel? {
        val allSongsInStorage = LibraryFragment.viewModel.getDataSet()

        for (song in allSongsInStorage) {
            if (song.id == favSong.songId) {
                return song
            }
        }
        return null
    }
}