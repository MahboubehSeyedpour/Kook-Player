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
import com.example.cassette.utlis.ImageUtils

class SongsRepository(val context: Context) {

    fun getSongFromCursor(cursor: Cursor): SongModel {
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
            image,
            "",
            year,
            dateModified,
            artistId,
            artistName,
            "",
            ""
        )
        return SongModel()
    }
}

