package com.example.kookplayer.repositories

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import com.example.kookplayer.db.entities.PlaylistModel
import com.example.kookplayer.myInterface.PlaylistRepoInterface
import com.example.kookplayer.repositories.RoomRepository.localDatabase
import com.example.kookplayer.utlis.DatabaseConverterUtils
import com.example.kookplayer.utlis.FilePathUtlis
import kotlinx.coroutines.*

class PlaylistRepository(val context: Context?) :
    PlaylistRepoInterface {

    //    ----------------------------------------------- Create Playlist ----------------------------------------------------
    override fun createPlaylist(name: String) {
        val playlist = PlaylistModel(name, 0, "")
        RoomRepository.createPlaylist(playlist)
//       TODO( createPlaylistInStorage())
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
//        removePlaylistFromDatabase(id)
//        removePlaylistFromStorage(id)

        RoomRepository.removePlaylist(id)

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
            //TODO(handle the exception)
            false
        }
    }

    //    ----------------------------------------------- Add song To Playlist ----------------------------------------------------


    fun removeSongFromPlaylist(playlistId: Long, songsId: String) {

        RoomRepository.removeSongFromPlaylist(playlistId, songsId)
    }


    //    ----------------------------------------------- Utils ----------------------------------------------------
    override fun getPlaylists(): ArrayList<PlaylistModel> {
        return RoomRepository.cachedPlaylistArray
    }

    override fun getIdOfSongsStoredInPlaylist(plylist_id: Long) =
        runBlocking {
            return@runBlocking localDatabase.playlistDao()
                .getSongsOfPlaylist(plylist_id)
        }


    override fun getPlaylistId(name: String): Long {
        TODO("Not yet implemented")
    }


    override fun getIdByName(name: String): Long {

        for (playlist in RoomRepository.cachedPlaylistArray) {
            if (playlist.name == name) {
                return playlist.id
            }
        }
        return -1L
    }


    override fun getPlaylistById(id: Long): PlaylistModel? {
        for (playlist in RoomRepository.cachedPlaylistArray) {
            if (playlist.id == id) {
                return playlist
            }
        }
        return null
    }
}