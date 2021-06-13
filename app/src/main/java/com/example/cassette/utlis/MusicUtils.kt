//package com.example.cassette.utlis

import android.content.BroadcastReceiver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
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
            while (cursor.moveToNext()) {
                if (songIsEmpty(cursor)) {
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

//    TODO(deleteMusic func)


    fun songIsEmpty(cursor: Cursor): Boolean
    {
        val duration = milliSecToDuration(
            cursor.getLong(
                cursor.getColumnIndexOrThrow(
                    MediaStore.Audio.Media.DURATION
                )
            )
        )
        return !duration.equals("0 : 0 : 0")

    }

    fun getMusicUri(id: Long): Uri {
        val sArtworkUri = Uri.parse("content://media/external/audio/media")
        return ContentUris.withAppendedId(sArtworkUri, id)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun removeMusic(position: Int, title: Array<String>) {


//        remove from mediaStore

//       val ur =  Library.arraylist?.get(position)?.id?.toLong()?.let { getMusic(it) }
        val id = Library.arraylist?.get(position)?.id
        val id_long = id?.toLong()

        val whereClause = "${MediaStore.Audio.Media._ID} = ?"
        val selectionArgs = arrayOf(Library.arraylist?.get(position)?.id)

            context.contentResolver.delete(MediaStore.Audio.Media.getContentUri(Library.arraylist?.get(position)?.data), null, null)

        if (id_long != null) {
            val ur = getMusicUri(id_long)


//            var garbage: String =""
//            var parentPath: String = ""
//            for (str in Library.arraylist?.get(position)?.data.toString())
//            {
//                if(!str.equals('/'))
//                {
//                    garbage = garbage + str
//                }
//                if(str.equals('/'))
//                {
//                    parentPath = parentPath + garbage
//                    garbage = "/"
//                }
//            }
//
//            val path = parentPath


//            val file = File(path , Library.arraylist?.get(position)?.title)
//            file.delete()
//            Toast.makeText(context, file.exists().toString(), Toast.LENGTH_SHORT).show()

        }

//        remove file from storage
//        remove from mediaStore
    }

    fun getSongFileUri(songId: Long): Uri {
        return ContentUris.withAppendedId(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            songId.toLong()
        )
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