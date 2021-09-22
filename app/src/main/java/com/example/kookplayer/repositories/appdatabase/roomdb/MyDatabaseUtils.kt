package com.example.kookplayer.repositories.appdatabase.roomdb

import SongUtils
import androidx.lifecycle.lifecycleScope
import com.example.kookplayer.repositories.appdatabase.entities.Favorites
import com.example.kookplayer.repositories.appdatabase.entities.PlaylistModel
import com.example.kookplayer.repositories.appdatabase.entities.SongModel
import com.example.kookplayer.utlis.DatabaseConverterUtils
import com.example.kookplayer.views.Fragments.LibraryFragment
import com.example.kookplayer.views.MainActivity
import kotlinx.android.synthetic.*
import kotlinx.coroutines.*

object MyDatabaseUtils {

    private val applicationScope = CoroutineScope(SupervisorJob())
    lateinit var localDatabase: MyDatabase
    var cashedPlaylistArray = ArrayList<PlaylistModel>()
    private var cashedFavArray_Favorites = ArrayList<Favorites>()
    var cashedFavArray = ArrayList<SongModel>()


    //    ----------------------------------------------- Database ----------------------------------------------------

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


    //    ----------------------------------------------- Playlist ----------------------------------------------------

    fun updateCashedPlaylistArray()
    {
        GlobalScope.launch {
            cashedPlaylistArray = getPlaylistFromDatabase()
        }
    }

    fun removeSongFromPlaylist(playlistId: Long, songsId: String) {

        val playlist = getPlaylistById(playlistId)


        if (playlist != null) {

            val position = findPlaylistPositionInCachedArray(playlist)

            if (position >= 0) {
                //remove song from playlist object
                removeSongFromPlaylistObject(cashedPlaylistArray[position], songsId)

                //decrease count of song in playlist object
                cashedPlaylistArray[position].countOfSongs--

            }

            GlobalScope.launch {
                //update songs in database
                localDatabase.playlistDao().updateSongs(playlistId, playlist.songs)

                //update count of songs in database
                decreaseCountInDatabase(playlistId, playlist.countOfSongs)
            }

        }
    }

    fun listOfPlaylistsContainSpecificSong(songId: Long): ArrayList<Long>
    {
        var pls = arrayListOf<Long>()

        for(playlist in cashedPlaylistArray)
        {
            val ids = DatabaseConverterUtils.stringToArraylist(playlist.songs)
            for(id in ids)
            {
                if (id.toLong() == songId)
                {
                    pls.add(playlist.id)
                }
            }
        }

        return pls
    }

    fun removeSongFromPlaylistObject(playlist: PlaylistModel, songsId: String) {
        val songsInAray = DatabaseConverterUtils.stringToArraylist(playlist.songs)
        songsInAray.remove(songsId)
        val songsInString = DatabaseConverterUtils.arraylistToString(songsInAray)
        playlist.songs = songsInString
    }

    fun decreaseCountInDatabase(playlistId: Long, countOfSongs: Int) {

        GlobalScope.launch {
            localDatabase.playlistDao()
                .setCountOfSongs(playlistId, countOfSongs)

        }
    }


    fun createPlaylist(playlist: PlaylistModel)
    {
        applicationScope.launch {
            localDatabase.playlistDao().addPlaylist(playlist)
        }
        cashedPlaylistArray.add(playlist)
    }


    fun addSongsToPlaylist(playlist_name: String, songsId: String): Boolean {

        val playlist = getPlaylistById(getIdByName(playlist_name))

//        add song to playlist object
        if (playlist != null) {

            val position = findPlaylistPositionInCachedArray(playlist)

            if (position >= 0) {
                addSongsToPlaylistInObject(
                    cashedPlaylistArray[position],
                    songsId
                )
            }

            addSongsToPlaylistInDatabse(playlist, songsId)

        }

        return true
    }

    private fun addSongsToPlaylistInObject(playlist: PlaylistModel, songsId: String) {

        val position = findPlaylistPositionInCachedArray(playlist)

        cashedPlaylistArray[position].songs =
            cashedPlaylistArray[position].songs + songsId + ","

        IncreaseCountInPlaylistObject(cashedPlaylistArray[position])
    }

    private fun addSongsToPlaylistInDatabse(playlist: PlaylistModel, songsId: String) {

        runBlocking {

            if (playlist != null) {
                for (song_id in songsId) {
                    localDatabase.playlistDao().addSongToPlaylist(
                        playlist.id,
                        playlist.songs
                    )
                }
                IncreaseCountInDatabase(playlist)
            }
//            TODO(Toast : operation failed! please try later)
        }
    }

    private fun IncreaseCountInPlaylistObject(playlist: PlaylistModel) {
        playlist.countOfSongs = playlist.countOfSongs + 1
    }

    private fun IncreaseCountInDatabase(playlist: PlaylistModel) {
        GlobalScope.launch {
            localDatabase.playlistDao()
                .setCountOfSongs(playlist.id, playlist.countOfSongs)
        }
    }

    private fun findPlaylistPositionInCachedArray(playlist: PlaylistModel): Int {
        var position: Int = -1
        while (++position < cashedPlaylistArray.size) {
            if (cashedPlaylistArray[position].id == playlist.id) {
                return position
            }
        }
        return position
    }

    private fun getIdByName(name: String): Long {

        for (playlist in cashedPlaylistArray) {
            if (playlist.name == name) {
                return playlist.id
            }
        }
        return -1L
    }

    private fun getPlaylistById(id: Long): PlaylistModel? {
        for (playlist in cashedPlaylistArray) {
            if (playlist.id == id) {
                return playlist
            }
        }
        return null
    }

    private fun getPlaylistFromDatabase(): ArrayList<PlaylistModel> =
        runBlocking {
            val playlistsList = localDatabase.playlistDao().getPlaylists()
            val arrayList = arrayListOf<PlaylistModel>()
            for (playlist in playlistsList) {
                arrayList.add(playlist)
            }
            return@runBlocking arrayList
        }


    fun removePlaylist(id: Long): Boolean
    {
        applicationScope.launch {
            localDatabase.playlistDao().deletePlaylist(id)

            cashedPlaylistArray =
                localDatabase.playlistDao().getPlaylists() as ArrayList<PlaylistModel>
        }

        return true
    }


    //    ----------------------------------------------- Favorites ----------------------------------------------------

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