//package com.example.cassette.utlis

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.cassette.BuildConfig
import com.example.cassette.models.SongModel
import com.example.cassette.utlis.FileUtils
import com.example.cassette.utlis.ImageUtils
import com.example.cassette.views.Fragments.Library


object SongUtils {

    lateinit var context: Context

    fun getListOfSongs(context: Context): ArrayList<SongModel> {

        val musicList = ArrayList<SongModel>()
        this.context = context
        val cursor = FileUtils.fetchFiles(
            fileType = FileUtils.FILE_TYPES.MUSIC,
            context = context
        )
        if (cursor != null && cursor.getCount() != 0) {
            do {
                cursor.moveToNext()
                musicList.add(createSong(cursor))
            } while (!cursor.isLast)
        } else {
//                TODO(handle null cursor)
        }
        cursor?.close()
        return musicList
    }

    fun createSong(cursor: Cursor): SongModel {
        val song = SongModel()
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
            song.data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
            song.id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
            song.dateAdded =
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED))
            song.artist =
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
            song.uri = ContentUris
                .withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID))
                )
            song.albumId =
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))

            song.image = ImageUtils.albumArtUriToBitmap(context, song.albumId.toLong())
                ?: ImageUtils.getDefaultAlbumArt(context)

        } catch (e: Exception) {
            song.duration = ""
//            TODO(handle the exception)
        }

        return song
    }

    fun shareMusic(song: SongModel) {

        val fileToBeShared = FileUtils.convertSongToFile(song.data)

        val fileUri: Uri? = FileUtils.getUriForFile(
            context,
            "${BuildConfig.APPLICATION_ID}.FileProvider",
            fileToBeShared
        )

        fileUri?.let { FileUtils.shareFile(context, it) }
    }


    @RequiresApi(Build.VERSION_CODES.R)
    fun deletMusic(activity: Activity, uri: Uri) {
        val urisToModify = mutableListOf(uri)
        val deletePendingIntent =
            MediaStore.createDeleteRequest(context.contentResolver, urisToModify)

        ActivityCompat.startIntentSenderForResult(
            activity,
            deletePendingIntent.intentSender,
            Library.DELETE_REQUEST_CODE,
            null,
            0,
            0,
            0,
            null
        )
    }

    fun shareMultipleMusics(positions: ArrayList<Int>) {
//        TODO(share multiple musics)
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

    fun showDetails(position: Int) {
//        TODO(retrieve details of the song)
    }

}