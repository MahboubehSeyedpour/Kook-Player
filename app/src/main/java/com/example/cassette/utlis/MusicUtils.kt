//package com.example.cassette.utlis

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.example.cassette.models.Song_Model
import com.example.cassette.utlis.FilePathUtlis
import com.example.cassette.views.Fragments.Library

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

//    fun setupMediaPlayer(context: Context) {
//        this.context = context
////        mediaPlayer = MediaPlayer.create(context, R.raw.nafas)
//    }

//    TODO(deleteMusic func)


    fun getMusicUri(id: Long): Uri {
        val sArtworkUri = Uri.parse("content://media/external/audio/media")
        return ContentUris.withAppendedId(sArtworkUri, id)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun removeMusic(position: Int, title: Array<String>) {

//       val ur =  Library.arraylist?.get(position)?.id?.toLong()?.let { getMusic(it) }
        val id = Library.arraylist?.get(position)?.id
        val id_long = id?.toLong()

        val whereClause = "${MediaStore.Audio.Media.TITLE} = ?"
        val selectionArgs = arrayOf("Toro Doost Daram.mp3".toString())

            if (id_long != null) {
                val ur = getMusicUri(id_long)
                if (ur != null) {
                    context.contentResolver.delete(ur, whereClause, selectionArgs)
                }
            }


//        val uri: Uri? = Library.arraylist?.get(position)?.data?.let {
//            MediaStore.Audio.Media.getContentUriForPath(
//                it
//            )
//        }


//        remove file from storage

//        try {
//            val uri = URI(Library.arraylist?.get(position)?.data)

//        if (uri != null) {
//            val whereclause: String = MediaStore.Audio.Media.TITLE + "=?"
//            context.contentResolver.delete(uri, null, null)
//            val i = 0
//        }
        val i = 0
//                val file = File(uri.path)
//
//                try {
//                    file.delete()
//                    Toast.makeText(context, file.exists().toString(), Toast.LENGTH_SHORT).show()
//
//                } catch (exception: Exception) {
//                    exception.printStackTrace()

////                Toast.makeText(context, exception.toString(), Toast.LENGTH_SHORT).show()
//                }
////            Toast.makeText(context, file.delete().toString(), Toast.LENGTH_SHORT).show()
////            Toast.makeText(context, file.exists().toString(), Toast.LENGTH_SHORT).show()
//
//
////            remove from mediaStore
//                var canonicalPath : String = ""
//                try {
//                    canonicalPath = file.canonicalPath
//                } catch (exception: IOException) {
//                    canonicalPath = file.absolutePath
//                }
//
//                val m_uri: Uri? = Library.arraylist?.get(position)?.data?.let {
//                    MediaStore.Files.getContentUri(
//                        it
//                    )
//                }
//                val whereclause1: String = MediaStore.Files.FileColumns.DATA + "=?"
//                    if (m_uri != null) {
//                        context.contentResolver.delete(m_uri, whereclause1, arrayOf(Library.arraylist?.get(position)?.data))
//                    }
//
//            }


//        } catch (exception: Exception) {
//            Toast.makeText(context, "a problem has occurred, please try later", Toast.LENGTH_SHORT)
//                .show()
//        }

//                val result = context.contentResolver.delete(m_uri, null, null)


//            if (FileUtils.checkFileExistance(file)) {
//                Toast.makeText(context, "فایل هست", Toast.LENGTH_SHORT).show()
//                file.delete()
//
//                if (FileUtils.checkFileExistance(file)) {
//                    Toast.makeText(context, "فایل همچنان هست", Toast.LENGTH_SHORT).show()
//                    if (FileUtils.checkFileExistance(file)) {
//                        Toast.makeText(context, "هنوزم هست", Toast.LENGTH_SHORT).show()
//                        context.deleteFile(file.name)
//
//                        if (FileUtils.checkFileExistance(file)) {
//                            Toast.makeText(
//                                context,
//                                "شاید باورت نشه ولی الانم هست، سمج چسبنده بی ریخت",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        } else {
//                            Toast.makeText(context, "file removed successfully", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//
//
//                    } else {
//                        Toast.makeText(context, "file removed successfully", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//
//
//                } else {
//                    Toast.makeText(context, "file removed successfully", Toast.LENGTH_SHORT).show()
//                }
//
//            } else {
//                Toast.makeText(context, "file does not exist", Toast.LENGTH_SHORT).show()
//            }


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