package com.example.cassette.repositories.appdatabase.roomdb

import androidx.lifecycle.lifecycleScope
import com.example.cassette.repositories.PlaylistRepository
import com.example.cassette.repositories.appdatabase.entities.PlaylistModel
import com.example.cassette.views.MainActivity
import kotlinx.coroutines.*

object DatabaseRepository {

    val applicationScope = CoroutineScope(SupervisorJob())
    lateinit var localDatabase: MyDatabase
    var cashedPlaylistArray = ArrayList<PlaylistModel>()

    fun createDatabse() {
        localDatabase = MyDatabase.getDatabase(
            MainActivity.activity.baseContext!!,
            MainActivity.activity.lifecycleScope
        )
        applicationScope.launch {
            cashedPlaylistArray = getPlaylistFromDatabase()
        }
    }

    fun addSongsToPlaylist(playlist_name: String, songsId: String): Boolean {

        val playlist = getPlaylistById(getIdByName(playlist_name))

//        add song to playlist object
        if (playlist != null) {

            val position = findPlaylistPositionInCachedArray(playlist)

            if (position >= 0) {
                addSongsToPlaylistInObject(PlaylistRepository.cashedPlaylistArray[position], songsId)
            }

            addSongsToPlaylistInDatabse(playlist, songsId)

        }

        return true
    }

    fun addSongsToPlaylistInObject(playlist: PlaylistModel, songsId: String) {

        val position = findPlaylistPositionInCachedArray(playlist)

        PlaylistRepository.cashedPlaylistArray[position].songs = PlaylistRepository.cashedPlaylistArray[position].songs + songsId + ","

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


    fun getFavorites() {

    }
}