package com.example.cassette.utlis

import MusicUtils
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.example.cassette.models.PlaylistModel
import com.example.cassette.models.SongModel

object PlaylistUtils {

    fun createPlaylist(context: Context, name: String) {
        val resolver = context?.contentResolver
        val values = ContentValues()
        values.put(MediaStore.Audio.Playlists.NAME, name)
        val uri = FilePathUtlis.getPlayListsUri()
        resolver?.insert(uri, values)
    }

    fun getPlaylists(context: Context): ArrayList<PlaylistModel> {
        val array = ArrayList<PlaylistModel>()

        val uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Playlists._ID,
            MediaStore.Audio.Playlists.NAME
        )

        val sortOrder = "${MediaStore.Audio.Playlists.NAME} ASC"

        val cursor = context?.contentResolver?.query(uri, projection, null, null, sortOrder)

        if (cursor != null) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val id =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists._ID)).toLong()
                val name: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.NAME))

                cursor.moveToNext()

                array.add(PlaylistModel(id, name))
            }

            cursor.close()
        }

        return array
    }

    fun deletePlaylist(context: Context, id: Long) {
        try {
            val playlistId = id.toString()
            val resolver = context?.contentResolver
            val where = MediaStore.Audio.Playlists._ID + "=?"
            val whereVal = arrayOf(playlistId)
            resolver?.delete(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, where, whereVal)
        } catch (exception: Exception) {
            //            TODO(handle the exception)
        }
    }


    fun addMusicToPlaylist(context: Context, id: Long, tracks: ArrayList<SongModel>) {
        val count = getPlaylistSize(context, id)
        val values = arrayOfNulls<ContentValues>(tracks.size)

        for (i in tracks.indices) {
            values[i] = ContentValues()
            values[i]?.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, i + count + 1)
            values[i]?.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, tracks[i].id)
        }

        val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", id)
        val resolver = context.contentResolver
        resolver.bulkInsert(uri, values)
        resolver.notifyChange(Uri.parse("content://media"), null)
    }


    fun getPlaylistSize(context: Context, id: Long): Int {
        var count = 0
        val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", id)

        val projection = arrayOf(MediaStore.Audio.Playlists.Members._ID)
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"

        val cursor: Cursor? = context.contentResolver.query(uri, projection, selection, null, null)

        if (cursor != null) {
            cursor.moveToFirst()

            while (!cursor.isAfterLast) {
                count++
                cursor.moveToNext()
            }

            cursor.close()
        }

        return count
    }


    fun getPlaylistMusics(context: Context, id: Long): ArrayList<SongModel> {
        val array = ArrayList<SongModel>()

        val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", id)

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC}  != 0"
        val sortOrder = "${MediaStore.Audio.Playlists.Members.PLAY_ORDER} ASC"

        val cursor = context.contentResolver.query(uri, projection, selection, null, sortOrder)

        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor!!.isLast) {
//                val id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)).toLong()
//                val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
//                val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
//                val data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
//                val duration = MusicUtils.milliSecToDuration(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)).toLong())
//                val albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toLong()

//                array.add(Track(id, title, artist, data, duration, albumId))

                MusicUtils.getMusic(cursor)

                cursor.moveToNext()
            }

            cursor.close()
        }

        return array
    }

    fun deletePlaylistTrack(context: Context, playlistId: Long, trackId: Long) {

        try {
            val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId)
            val where = MediaStore.Audio.Playlists.Members._ID + "=?"
            val whereval = arrayOf(trackId.toString())
            context.contentResolver.delete(uri, where, whereval)
        } catch (exception: java.lang.Exception) {
            //            TODO(handle the exception)
        }
    }

    fun playlistItemReorder(context: Context, playlistId: Long, oldPos: Int, newPos: Int) {
        MediaStore.Audio.Playlists.Members.moveItem(
            context.contentResolver,
            playlistId,
            oldPos,
            newPos
        )
    }

}