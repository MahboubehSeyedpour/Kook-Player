package com.example.kookplayer.repositories

import androidx.lifecycle.lifecycleScope
import com.example.kookplayer.db.MyDatabase
import com.example.kookplayer.db.entities.Favorites
import com.example.kookplayer.db.entities.PlaylistModel
import com.example.kookplayer.db.entities.SongModel
import com.example.kookplayer.myInterface.RoomRepositoryInterface
import com.example.kookplayer.utlis.DatabaseConverterUtils
import com.example.kookplayer.utlis.SongUtils
import com.example.kookplayer.views.Fragments.LibraryFragment
import com.example.kookplayer.views.activities.MainActivity
import kotlinx.android.synthetic.*
import kotlinx.coroutines.*

object RoomRepository : RoomRepositoryInterface{

    private val applicationScope = CoroutineScope(SupervisorJob())
    lateinit var localDatabase: MyDatabase
    var cachedPlaylistArray = ArrayList<PlaylistModel>()
    private var cachedFavArray_Favorites = ArrayList<Favorites>()
    var cachedFavArray = ArrayList<SongModel>()


    //    ----------------------------------------------- Database ----------------------------------------------------

    override fun createDatabase() {
        localDatabase = MyDatabase.getDatabase(
            MainActivity.activity.baseContext!!,
            MainActivity.activity.lifecycleScope
        )
        applicationScope.launch {
            cachedPlaylistArray = getPlaylistFromDatabase()
            cachedFavArray_Favorites = getFavoritesFromDatabase()
        }
    }


    //    ----------------------------------------------- Playlist ----------------------------------------------------

    override fun updateCachedPlaylist()
    {
        GlobalScope.launch {
            cachedPlaylistArray = getPlaylistFromDatabase()
        }
    }

    override fun removeSongFromPlaylist(playlistId: Long, songsId: String) {

        val playlist = getPlaylistById(playlistId)


        if (playlist != null) {

            val position = findPlaylistPositionInCachedArray(playlist)

            if (position >= 0) {
                //remove song from playlist object
                removeSongFromPlaylistObject(cachedPlaylistArray[position], songsId)

                //decrease count of song in playlist object
                cachedPlaylistArray[position].countOfSongs--

            }

            GlobalScope.launch {
                //update songs in database
                localDatabase.playlistDao().updateSongs(playlistId, playlist.songs)

                //update count of songs in database
                decreaseCountInDatabase(playlistId, playlist.countOfSongs)
            }

        }
    }

    override fun getPlaylists(): ArrayList<PlaylistModel> {
        return cachedPlaylistArray
    }

    override fun listOfPlaylistsContainSpecificSong(songId: Long): ArrayList<Long>
    {
        var pls = arrayListOf<Long>()

        for(playlist in cachedPlaylistArray)
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

    override fun removeSongFromPlaylistObject(playlist: PlaylistModel, songsId: String) {
        val songsInAray = DatabaseConverterUtils.stringToArraylist(playlist.songs)
        songsInAray.remove(songsId)
        val songsInString = DatabaseConverterUtils.arraylistToString(songsInAray)
        playlist.songs = songsInString
    }

    override fun decreaseCountInDatabase(playlistId: Long, countOfSongs: Int) {

        GlobalScope.launch {
            localDatabase.playlistDao()
                .setCountOfSongs(playlistId, countOfSongs)

        }
    }


    override fun createPlaylist(playlist: PlaylistModel)
    {
        applicationScope.launch {
            localDatabase.playlistDao().addPlaylist(playlist)
        }
        cachedPlaylistArray.add(playlist)
    }


    override fun addSongsToPlaylist(playlist_name: String, songsId: String): Boolean {

        val playlist = getPlaylistById(getIdByName(playlist_name))

//        add song to playlist object
        if (playlist != null) {

            val position = findPlaylistPositionInCachedArray(playlist)

            if (position >= 0) {
                addSongsToPlaylistInObject(
                    cachedPlaylistArray[position],
                    songsId
                )
            }

            addSongsToPlaylistInDatabase(playlist, songsId)

        }

        return true
    }

    override fun addSongsToPlaylistInObject(playlist: PlaylistModel, songsId: String) {

        val position = findPlaylistPositionInCachedArray(playlist)

        cachedPlaylistArray[position].songs =
            cachedPlaylistArray[position].songs + songsId + ","

        increaseCountInPlaylistObject(cachedPlaylistArray[position])
    }

    override fun addSongsToPlaylistInDatabase(playlist: PlaylistModel, songsId: String) {

        runBlocking {

            if (playlist != null) {
                for (song_id in songsId) {
                    localDatabase.playlistDao().addSongToPlaylist(
                        playlist.id,
                        playlist.songs
                    )
                }
                increaseCountInDatabase(playlist)
            }
//            TODO(Toast : operation failed! please try later)
        }
    }

    override fun increaseCountInPlaylistObject(playlist: PlaylistModel) {
        playlist.countOfSongs = playlist.countOfSongs + 1
    }

    override fun increaseCountInDatabase(playlist: PlaylistModel) {
        GlobalScope.launch {
            localDatabase.playlistDao()
                .setCountOfSongs(playlist.id, playlist.countOfSongs)
        }
    }

    override fun findPlaylistPositionInCachedArray(playlist: PlaylistModel): Int {
        var position: Int = -1
        while (++position < cachedPlaylistArray.size) {
            if (cachedPlaylistArray[position].id == playlist.id) {
                return position
            }
        }
        return position
    }

    override fun getIdByName(name: String): Long {

        for (playlist in cachedPlaylistArray) {
            if (playlist.name == name) {
                return playlist.id
            }
        }
        return -1L
    }

    override fun getPlaylistById(id: Long): PlaylistModel? {
        for (playlist in cachedPlaylistArray) {
            if (playlist.id == id) {
                return playlist
            }
        }
        return null
    }

    override fun getPlaylistFromDatabase(): ArrayList<PlaylistModel> =
        runBlocking {
            val playlistsList = localDatabase.playlistDao().getPlaylists()
            val arrayList = arrayListOf<PlaylistModel>()
            for (playlist in playlistsList) {
                arrayList.add(playlist)
            }
            return@runBlocking arrayList
        }


    override fun removePlaylist(id: Long): Boolean
    {
        applicationScope.launch {
            localDatabase.playlistDao().deletePlaylist(id)

            cachedPlaylistArray =
                localDatabase.playlistDao().getPlaylists() as ArrayList<PlaylistModel>
        }

        return true
    }


    //    ----------------------------------------------- Favorites ----------------------------------------------------

    override fun addSongToFavorites(songsId: Long) {
        val fav = Favorites(songsId)
        applicationScope.launch {
            localDatabase.favoriteDao().addSong(fav)
        }

        SongUtils.getSongById(songsId)?.let { cachedFavArray.add(it) }
    }

    override fun removeSongFromFavorites(song: SongModel) {

        removeSongFromDB(song)
        updateCachedFavArray(song)

    }

    private fun updateCachedFavArray(song: SongModel)
    {
        for (favSongs in cachedFavArray)
        {
            if(favSongs.id!! == song!!.id)
            {
                cachedFavArray.remove(favSongs)
            }
        }
    }

    fun removeSongFromDB(song: SongModel)
    {

        val fav = Favorites(song.id!!)
        applicationScope.launch {
            localDatabase.favoriteDao().deleteSong(fav)
        }

    }

    override fun getFavoritesFromDatabase(): ArrayList<Favorites> =
        runBlocking {
            val favSongs = localDatabase.favoriteDao().getFavs()

            val arr = arrayListOf<Favorites>()
            arr.addAll(favSongs)
            return@runBlocking arr
        }

    override fun convertFavSongsToRealSongs(): ArrayList<SongModel>
    {

        val arrayList = arrayListOf<SongModel>()
        for (favSong in cachedFavArray_Favorites) {

            val realSong = songsIdToSongModelConverter(favSong)
            if (realSong != null)
                arrayList.add(realSong)
        }

        cachedFavArray = arrayList
        return arrayList

    }

    override fun songsIdToSongModelConverter(favSong: Favorites): SongModel? {
        val allSongsInStorage = LibraryFragment.viewModel.getDataSet()

        for (song in allSongsInStorage) {
            if (song.id == favSong.songId) {
                return song
            }
        }
        return null
    }
}