package com.example.cassette.repositories

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.provider.MediaStore.Audio.AudioColumns
import com.example.cassette.extensions.getInt
import com.example.cassette.extensions.getLong
import com.example.cassette.extensions.getString
import com.example.cassette.models.SongModel
import com.example.cassette.utlis.FileUtils
import com.example.cassette.utlis.ImageUtils

class SongsRepository(val context: Context) {

    fun createSongFromCursor(cursor: Cursor): SongModel {
        val title = cursor.getString(AudioColumns.TITLE)
        val duration = cursor.getLong(AudioColumns.DURATION)
        val data = cursor.getString(AudioColumns.DATA)
        val id = cursor.getString(AudioColumns._ID)
        val dateAdded = cursor.getString(AudioColumns.DATE_ADDED)
        val artist = cursor.getString(AudioColumns.ARTIST)
//        val trackNumber = cursor.getString(AudioColumns.TRACK)
        val year = cursor.getInt(AudioColumns.YEAR)
        val dateModified = cursor.getLong(AudioColumns.DATE_MODIFIED)
        val artistId = cursor.getLong(AudioColumns.ARTIST_ID)
        val artistName = cursor.getString(AudioColumns.ARTIST)
//        val composer = cursor.getString(AudioColumns.COMPOSER)
//        val albumArtist = cursor.getString(AudioColumns.ALBUM_ARTIST)
        val uri = ContentUris
            .withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID))
            )
        val albumId = cursor.getLong(AudioColumns.ALBUM_ID)
        val size = cursor.getString(AudioColumns.SIZE)

        val image = ImageUtils.albumArtUriToBitmap(context, albumId.toLong())
            ?: ImageUtils.getDefaultAlbumArt(context)

        return SongModel(
            title,
            duration,
            data,
            dateAdded,
            artist,
            id,
            uri,
            albumId,
            size,
            image,
            "",
            year,
            dateModified,
            artistId,
            artistName,
            "",
            ""
        )
    }

    fun getListOfSongs(): ArrayList<SongModel> {

        val songs = ArrayList<SongModel>()
        val cursor = FileUtils.fetchFiles(
            fileType = FileUtils.FILE_TYPES.MUSIC,
            context = context
        )
        if (cursor != null && cursor.count != 0) {
            do {
                cursor.moveToNext()
                val i = cursor.getLong(AudioColumns.DURATION)
                if (cursor.getLong(AudioColumns.DURATION) > 60000)
                    songs.add(createSongFromCursor(cursor))
            } while (!cursor.isLast)
        } else {
//                TODO(handle null cursor)
        }
        cursor?.close()
        return songs
    }
}

