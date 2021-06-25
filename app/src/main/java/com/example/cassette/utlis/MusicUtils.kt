//package com.example.cassette.utlis

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.example.cassette.BuildConfig
import com.example.cassette.models.Song_Model
import com.example.cassette.utlis.FilePathUtlis
import com.example.cassette.views.Fragments.Library
import java.io.File


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
            do {
                cursor.moveToNext()
                if (!songIsEmpty(cursor)) {
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
                        song.id =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                        song.dateAdded =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED))
                        song.artist =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                        column_id =
                            cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns._ID))

                        song.uri = ContentUris
                            .withAppendedId(
                                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID))
                            )
                        musicList.add(song)
                    } catch (e: Exception) {
                        song.duration = ""
                    }
                }
            } while (!cursor.isLast)
        }
        return musicList
    }

    fun songIsEmpty(cursor: Cursor): Boolean {
        val duration = milliSecToDuration(
            cursor.getLong(
                cursor.getColumnIndexOrThrow(
                    MediaStore.Audio.Media.DURATION
                )
            )
        )
        return duration.equals("0 : 0 : 0")
    }

    fun shareMusic(position: Int) {

        val requestFile = File(Library.arraylist?.get(position)?.data)

        val fileUri: Uri? = try {
            FileProvider.getUriForFile(
                context,
                "${BuildConfig.APPLICATION_ID}.FileProvider",
                requestFile
            )
        } catch (e: IllegalArgumentException) {
            Log.e(
                "File Selector",
                "The selected file can't be shared: $requestFile"
            )
            null
        }

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.type = "audio/*"
        context.startActivity(Intent.createChooser(shareIntent, "share audio"))
    }

    fun shareMusic(positions: ArrayList<Int>) {
//        TODO(share multiple musics)
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