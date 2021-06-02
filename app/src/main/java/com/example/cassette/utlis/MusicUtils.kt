package com.example.cassette.utlis

import android.content.Context
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.contentValuesOf
import com.example.cassette.R
import com.example.cassette.models.Song_Model
import kotlinx.android.synthetic.main.bottom_sheet.*
import java.net.URI

object MusicUtils {
    lateinit var mediaPlayer : MediaPlayer
    lateinit var context: Context
    fun getListOfMusics(context: Context): ArrayList<Song_Model> {

        this.context = context
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
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                    song.data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                } catch (e: Exception) {
                    song.duration = "Not Defined"
                }

                musicList.add(song)
            }
        }
        mediaPlayer = MediaPlayer.create(context, R.raw.nafas)
        return musicList
    }

//    TODO(deleteMusic func)

    fun getMediaColumns(): Array<String> {
        return arrayOf(
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DATE_MODIFIED
        )
    }

    fun playMusic(content: String){
        val uri :Uri = Uri.parse(content)
        mediaPlayer.release()
        mediaPlayer = MediaPlayer.create(context, uri)
        mediaPlayer.start()
    }

}