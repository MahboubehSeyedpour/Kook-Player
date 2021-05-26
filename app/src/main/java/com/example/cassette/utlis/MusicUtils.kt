package com.example.cassette.utlis

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.example.cassette.models.Song_Model

object MusicUtils {
    fun getListOfMusics(context: Context?): ArrayList<Song_Model> {
        val musicList = ArrayList<Song_Model>()
        val cursor: Cursor? = context?.contentResolver?.query(
            FilePathUtlis.getMusicsUri(),
            null,
            null,
            null,
            null
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val song = Song_Model()
                song.title =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                try {
                    song.duration =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                } catch (e: Exception) {
                    song.duration = "Not Defined"
                }

                musicList.add(song)
            }
        }

        return musicList
    }

//    TODO(deleteMusic func)

    fun getMediaColumns(): Array<String> {
        return arrayOf(
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DATE_MODIFIED
        )
    }
}