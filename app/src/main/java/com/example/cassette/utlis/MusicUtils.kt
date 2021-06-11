//package com.example.cassette.utlis

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Build.ID
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.example.cassette.R
import com.example.cassette.models.Song_Model
import com.example.cassette.utlis.FilePathUtlis
import com.example.cassette.views.Fragments.Library
import com.example.cassette.views.PlayerRemote
import kotlinx.android.synthetic.main.player_remote.*
import java.net.URI
import kotlin.collections.ArrayList
import kotlin.concurrent.timerTask

object MusicUtils {

    lateinit var context: Context
    var column_id: Long = -1

    fun getListOfMusics(context: Context): ArrayList<Song_Model> {

        val musicList = ArrayList<Song_Model>()
        this.context = context
        val cursor: Cursor? = context?.contentResolver?.query(
            FilePathUtlis.getMusicsUri(),
            null,
            null,
            null,
            null
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                if (milliSecToDuration(
                        cursor.getLong(
                            cursor.getColumnIndexOrThrow(
                                MediaStore.Audio.Media.DURATION
                            )
                        )
                    ) != "0 : 0 : 0"
                ) {
                    val song = Song_Model()
                    try {

                        song.title =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                        song.duration =
                            milliSecToDuration(
                                cursor.getLong(
                                    cursor.getColumnIndexOrThrow(
                                        MediaStore.Audio.Media.DURATION
                                    )
                                )
                            )
                        song.data =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                        song.dateAdded =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED))
                        song.artist =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                        column_id =
                            cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns._ID))

                        musicList.add(song)
                    } catch (e: Exception) {
                        song.duration = ""
                    }
                }
            }
            val song = Song_Model()
            try {
                song.title =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                song.duration =
                    milliSecToDuration(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)))
                song.data =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
            } catch (e: Exception) {
                song.duration = ""
            }

            musicList.add(song)
        }

        return musicList
    }

//    fun setupMediaPlayer(context: Context) {
//        this.context = context
////        mediaPlayer = MediaPlayer.create(context, R.raw.nafas)
//    }

//    TODO(deleteMusic func)

    @RequiresApi(Build.VERSION_CODES.R)
    fun removeMusic(position: Int, title: Array<String>) {
//        val uri : Uri = ContentUris.withAppendedId(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, column_id)
        val uri: Uri = Uri.parse("file:///" + Library.arraylist?.get(position)?.data)
        context.contentResolver.delete(uri, "MediaStore.Audio.Media.TITLE =?", title)

    }

    fun getMediaColumns(): Array<String> {
        return arrayOf(
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DATE_MODIFIED
        )
    }

    fun getDurationOfCurrentMusic(): String {
        return Library.songsAdapter?.arrayList?.get(Library.songsAdapter!!.getCurrentPosition())?.duration
            ?: "00:00:00"
    }

    fun milliSecToDuration(duration: Long): String {
        val millisec_temp = duration / 1000
        val seconds_final = millisec_temp % 60
        val minutes_temp = millisec_temp / 60
        val minutes_final = minutes_temp % 60
        val hour_final = minutes_temp / 60
        return "$hour_final : $minutes_final : $seconds_final"
    }

}