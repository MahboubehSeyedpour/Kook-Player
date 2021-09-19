package com.example.cassette.repositories

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import com.example.cassette.myInterface.PlaylistRepoInterface
import com.example.cassette.repositories.appdatabase.entities.PlaylistModel
import com.example.cassette.repositories.appdatabase.roomdb.DatabaseRepository
import com.example.cassette.repositories.appdatabase.roomdb.DatabaseRepository.localDatabase
import com.example.cassette.repositories.appdatabase.roomdb.MyDatabase
import com.example.cassette.utlis.DatabaseConverterUtils
import com.example.cassette.utlis.FilePathUtlis
import kotlinx.coroutines.*

class PlaylistRepository(val context: Context?) :
    PlaylistRepoInterface {

    val applicationScope = CoroutineScope(SupervisorJob())

//    val localDatabase = MyDatabase.getDatabase(context!!, applicationScope)

    companion object {
        lateinit var db: MyDatabase
        var cashedPlaylistArray = DatabaseRepository.cashedPlaylistArray
    }

    init {
        applicationScope.launch {
            cashedPlaylistArray =
                localDatabase.playlistDao().getPlaylists() as ArrayList<PlaylistModel>
            db = localDatabase
        }
    }

    //    ----------------------------------------------- Create Playlist ----------------------------------------------------
    override fun createPlaylist(name: String) {
        val playlist = PlaylistModel(name, 0, "")
        createPlaylistInDatabase(playlist)
//       TODO( createPlaylistInStorage())
    }

    private fun createPlaylistInDatabase(playlist: PlaylistModel) {
        applicationScope.launch {
            localDatabase.playlistDao().addPlaylist(playlist)

            cashedPlaylistArray =
                localDatabase.playlistDao().getPlaylists() as ArrayList<PlaylistModel>
        }
    }

    private fun createPlaylistInStorage(name: String): PlaylistModel {

        val resolver = context?.contentResolver
        val values = ContentValues()
        values.put(MediaStore.Audio.Playlists.NAME, name)
        val uri = FilePathUtlis.getPlayListsUri()
        resolver?.insert(uri, values)


        val countOfSongs = 0
        val songs = arrayListOf<String>()
        val playlist =
            PlaylistModel(name, countOfSongs, DatabaseConverterUtils.arraylistToString(songs))
        return playlist

    }


    //    ----------------------------------------------- Remove Playlist ----------------------------------------------------
    override fun removePlaylist(id: Long): Boolean {
        removePlaylistFromDatabase(id)
//        removePlaylistFromStorage(id)

        return true
    }

    private fun removePlaylistFromStorage(playlist_Id: Long): Boolean {
        return try {
            val playlistId = playlist_Id.toString()
            val resolver = context?.contentResolver
            val where = MediaStore.Audio.Playlists._ID + "=?"
            val whereVal = arrayOf(playlistId)
            resolver?.delete(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, where, whereVal)
            true

        } catch (exception: Exception) {
            //            TODO(handle the exception)
            false
        }
    }

    private fun removePlaylistFromDatabase(playlist_Id: Long) {
        applicationScope.launch {
            localDatabase.playlistDao().deletePlaylist(playlist_Id)

            cashedPlaylistArray =
                localDatabase.playlistDao().getPlaylists() as ArrayList<PlaylistModel>
        }
    }


    //    ----------------------------------------------- Add song To Playlist ----------------------------------------------------
//    override fun addSongsToPlaylist(playlist_name: String, songsId: String): Boolean {
//
//        val playlist = getPlaylistById(getIdByName(playlist_name))
//
////        add song to playlist object
//        if (playlist != null) {
//
//            val position = findPlaylistPositionInCachedArray(playlist)
//
//            if (position >= 0) {
//                addSongsToPlaylistInObject(cashedPlaylistArray[position], songsId)
//            }
//
//            addSongsToPlaylistInDatabse(playlist, songsId)
//
//        }
//
//        return true
//    }

    fun findPlaylistPositionInCachedArray(playlist: PlaylistModel): Int {
        var position: Int = -1
        while (++position < cashedPlaylistArray.size) {
            if (cashedPlaylistArray[position].id == playlist.id) {
                return position
            }
        }
        return position
    }

//    fun addSongsToPlaylistInDatabse(playlist: PlaylistModel, songsId: String) {
//
//        runBlocking {
//
//            if (playlist != null) {
//                for (song_id in songsId) {
//                    localDatabase.playlistDao().addSongToPlaylist(
//                        playlist.id,
//                        playlist.songs
//                    )
//                }
//                addCountInDatabase(playlist)
//            }
////            TODO(Toast : operation failed! please try later)
//        }
//    }

//    fun addSongsToPlaylistInObject(playlist: PlaylistModel, songsId: String) {
//
//        val position = findPlaylistPositionInCachedArray(playlist)
//
//        cashedPlaylistArray[position].songs = cashedPlaylistArray[position].songs + songsId + ","
//
//        addCountInPlaylistObject(cashedPlaylistArray[position])
//    }

//    fun addCountInDatabase(playlist: PlaylistModel) {
//        GlobalScope.launch {
//            localDatabase.playlistDao()
//                .setCountOfSongs(playlist.id, playlist.countOfSongs)
//        }
//    }

    fun addCountInPlaylistObject(playlist: PlaylistModel) {
        playlist.countOfSongs = playlist.countOfSongs + 1
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


    //    ----------------------------------------------- Synchronize Database and Storage ----------------------------------------------------
//    override fun updateDatabase(): Boolean {
//        val storage_playlists = getPlaylistFromStorage()
//        val database_playlists = getPlaylistFromDatabase()
//        if (storage_playlists != database_playlists) {
//            cashedPlaylistArray = storage_playlists
//            updateTable()
//        }
//        return true
//    }


//    @SuppressLint("Range")
//    override fun getPlaylistFromStorage(): ArrayList<PlaylistModel> {
//        val array = ArrayList<PlaylistModel>()
//
//        if (context != null) {
//            //        val cursor = context?.contentResolver?.query(uri, projection, null, null, sortOrder)
//            val cursor = FileUtils.fetchFiles(
//                fileType = FileUtils.FILE_TYPES.PLAYLIST,
//                context = context,
//                projection = arrayOf(
//                    MediaStore.Audio.Playlists._ID,
//                    MediaStore.Audio.Playlists.NAME
//                ),
//                sortOrder = "${MediaStore.Audio.Playlists.NAME} ASC"
//            )
//
//            if (cursor != null) {
//                cursor.moveToFirst()
//                while (!cursor.isAfterLast) {
//                    val id =
//                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists._ID))
//                            .toLong()
//                    val name: String =
//                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.NAME))
//                            ?: ""
//
//                    cursor.moveToNext()
//
//                    array.add(PlaylistModel(name, 0, arrayListOf<String>()))
//                }
//
//                cursor.close()
//            } else {
////            TODO(handle null cursor)
//            }
//        }
//
//        cashedPlaylistArray = array
//
//        return array
//    }

    //    ----------------------------------------------- Utils ----------------------------------------------------
    override fun getPlaylists(): ArrayList<PlaylistModel> {
        return cashedPlaylistArray
    }

//    override fun getPlaylistFromDatabase(): ArrayList<PlaylistModel> =
//        runBlocking {
//            val playlistsList = localDatabase.playlistDao().getPlaylists()
//            val arrayList = arrayListOf<PlaylistModel>()
//            for (playlist in playlistsList) {
//                arrayList.add(playlist)
//            }
//            return@runBlocking arrayList
//        }


    override fun getIdOfSongsStoredInPlaylist(plylist_id: Long) =
        runBlocking {
            return@runBlocking localDatabase.playlistDao()
                .getSongsOfPlaylist(plylist_id)
        }


    override fun getPlaylistId(name: String): Long {
        TODO("Not yet implemented")
    }


    override fun getIdByName(name: String): Long {

        for (playlist in cashedPlaylistArray) {
            if (playlist.name == name) {
                return playlist.id
            }
        }
        return -1L
    }


    override fun getPlaylistById(id: Long): PlaylistModel? {
        for (playlist in cashedPlaylistArray) {
            if (playlist.id == id) {
                return playlist
            }
        }
        return null
    }

    fun getSongsInArraylist(songs: String): ArrayList<String> {
        return DatabaseConverterUtils.stringToArraylist(songs)
    }

    fun getSongsInString(songs: ArrayList<String>): String {
        return DatabaseConverterUtils.arraylistToString(songs)
    }

}