package com.example.cassette.repositories

import android.content.Context
import android.provider.MediaStore
import com.example.cassette.models.PlaylistModel
import com.example.cassette.utlis.FileUtils
import com.example.cassette.utlis.PlaylistUtils

class PlaylistRepository
{

    fun getPlaylists(context: Context?): ArrayList<PlaylistModel> {
        val array = ArrayList<PlaylistModel>()

        if(context != null)
        {
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
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists._ID)).toLong()
                    val name: String =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.NAME))

                    cursor.moveToNext()

                    array.add(PlaylistModel(id, name, 0))
                }

                cursor.close()
            } else {
//            TODO(handle null cursor)
            }

            PlaylistUtils.playlists = array
        }
        return array
    }


    fun getCachedPlaylists(): ArrayList<PlaylistModel>
    {
        return PlaylistUtils.playlists
    }


}