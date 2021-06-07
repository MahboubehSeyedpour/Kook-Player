package com.example.cassette.utlis

import android.content.Context
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import com.example.cassette.R
import com.example.cassette.models.Song_Model
import kotlin.collections.ArrayList

object MusicUtils {

    lateinit var mediaPlayer: MediaPlayer
    lateinit var context: Context


    fun getListOfMusics(context: Context): ArrayList<Song_Model> {

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
        }

        return musicList
    }

    fun setupMediaPlayer(context: Context) {
        this.context = context
        mediaPlayer = MediaPlayer.create(context, R.raw.nafas)
    }

//    TODO(deleteMusic func)

    fun getMediaColumns(): Array<String> {
        return arrayOf(
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DATE_MODIFIED
        )
    }

    fun PlayMusic(content: String) {
        val uri: Uri = Uri.parse(content)
        mediaPlayer.release()
        mediaPlayer = MediaPlayer.create(context, uri)
        mediaPlayer.start()
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