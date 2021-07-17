//package com.example.cassette.utlis

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.example.cassette.BuildConfig
import com.example.cassette.R
import com.example.cassette.models.SongModel
import com.example.cassette.utlis.FilePathUtlis
import com.example.cassette.utlis.FileUtils
import com.example.cassette.views.Fragments.Library
import java.io.File
import java.io.FileDescriptor


object SongUtils {

    lateinit var context: Context

    fun getListOfSongs(context: Context): ArrayList<SongModel> {

        val musicList = ArrayList<SongModel>()
        this.context = context
        val cursor = FileUtils.fetchFiles(
            fileType = FileUtils.FILE_TYPES.MUSIC,
            context = context,
            projection = null,
            selection = null,
            selectionArgs = null,
            sortOrder = null
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
            song.image = addCoverImageToSong(song.albumId)

        } catch (e: Exception) {
            song.duration = ""
//            TODO(handle the exception)
        }

        return song
    }

    fun addCoverImageToSong(albumId: String): Bitmap {

        val songArt = getAlbumart(albumId.toLong())

        return if (songArt != null) songArt else getDefaultAlbumArt()
    }

    fun getAlbumart(album_id: Long?): Bitmap? {
        var bm: Bitmap? = null
        val options = BitmapFactory.Options()
        try {
            val sArtworkUri =
                Uri.parse(FilePathUtlis.getAlbumsUri())


            val uri = ContentUris.withAppendedId(sArtworkUri, album_id!!)
            var pfd =
                context.contentResolver.openFileDescriptor(uri, "r")
            if (pfd != null) {
                var fd: FileDescriptor? = pfd.fileDescriptor
                bm = BitmapFactory.decodeFileDescriptor(fd, null, options)
                pfd = null
                fd = null
            }
        } catch (exception: java.lang.Exception) {
//           TODO(handle the exception)
        }

        return bm
    }


    fun getDefaultAlbumArt(): Bitmap {
        return BitmapFactory.decodeResource(
            context.getResources(),
            R.mipmap.ic_music_foreground
        )
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

    fun shareMusics(positions: ArrayList<Int>) {
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

    fun showDetails(position: Int)
    {
//        TODO(retrieve details of the song)
    }

}