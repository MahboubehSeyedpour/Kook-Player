package com.example.cassette.repositories

import android.content.Context
import android.provider.MediaStore
import com.example.cassette.models.PlaylistModel
import com.example.cassette.utlis.FileUtils
import com.example.cassette.utlis.PlaylistUtils

class PlaylistRepository(val context: Context?) {

    var playlistArray = ArrayList<PlaylistModel>()

    fun getPlaylists(): ArrayList<PlaylistModel> {
        val array = ArrayList<PlaylistModel>()

        if (context != null) {
            //        val cursor = context?.contentResolver?.query(uri, projection, null, null, sortOrder)
            val cursor = FileUtils.fetchFiles(
                fileType = FileUtils.FILE_TYPES.PLAYLIST,
                context = context,
                projection = arrayOf(
                    MediaStore.Audio.Playlists._ID,
                    MediaStore.Audio.Playlists.NAME
                ),
                sortOrder = "${MediaStore.Audio.Playlists.NAME} ASC"
            )

            if (cursor != null) {
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val id =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists._ID))
                            .toLong()
                    val name: String =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.NAME)) ?: ""

                    cursor.moveToNext()

                    array.add(PlaylistModel(id, name, arrayListOf()))
                }

                cursor.close()
            } else {
//            TODO(handle null cursor)
            }

            PlaylistUtils.playlists = array
        }

        playlistArray = array

        return array
    }


    fun getPlaylistIdByName(name: String): String {

        playlistArray = getPlaylists()

        for (playlist in playlistArray) {
            if (playlist.name == name) return playlist.id.toString() else continue
        }
        return "-1"
    }


}