package com.example.cassette.utlis

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.example.cassette.R
import com.example.cassette.models.PlaylistModel
import com.example.cassette.models.SongModel
import com.example.cassette.repositories.SongsRepository

object PlaylistUtils {

    var playlists = ArrayList<PlaylistModel>()
    lateinit var context : Context

    fun createPlaylist(context: Context, name: String) {

        this.context = context

        val resolver = context?.contentResolver
        val values = ContentValues()
        values.put(MediaStore.Audio.Playlists.NAME, name)
        val uri = FilePathUtlis.getPlayListsUri()
        resolver?.insert(uri, values)

//        Create new playlist :/
    }

    fun getPlaylists(context: Context?): ArrayList<PlaylistModel> {
        val array = ArrayList<PlaylistModel>()

        if(context != null)
        {
            //        val cursor = context?.contentResolver?.query(uri, projection, null, null, sortOrder)
            val cursor = FileUtils.fetchFiles(
                fileType = FileUtils.FILE_TYPES.PLAYLIST,
                context = context,
                projection = arrayOf(
                    MediaStore.Audio.Playlists._ID,
                    MediaStore.Audio.Playlists.NAME
                ),
                sortOrder = "${MediaStore.Audio.Playlists.NAME} ASC"
            )

            if (cursor != null) {
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val id =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists._ID)).toLong()
                    val name: String =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.NAME))

                    cursor.moveToNext()

                    array.add(PlaylistModel(id, name, 0))
                }

                cursor.close()
            } else {
//            TODO(handle null cursor)
            }

            playlists = array
        }
        return array
    }

    fun getCachedPlaylists(): ArrayList<PlaylistModel>
    {
       return playlists
    }


    fun deletePlaylist(context: Context, playlist_Id: Long) {
        try {
            val playlistId = playlist_Id.toString()
            val resolver = context?.contentResolver
            val where = MediaStore.Audio.Playlists._ID + "=?"
            val whereVal = arrayOf(playlistId)
            resolver?.delete(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, where, whereVal)
        } catch (exception: Exception) {
//            TODO(handle the exception)
        }
    }


    fun addToPlaylist(context: Context, id: Long, tracks: ArrayList<SongModel>) {
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


    fun getPlaylistSize(context: Context, playlisId: Long): Int {
        var count = 0
        val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlisId)

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


    fun getMusicsRelatedToSpecificPlaylist(
        context: Context,
        playlistId: Long
    ): ArrayList<SongModel> {

        val array = ArrayList<SongModel>()

        val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId)

//        val projection = arrayOf(
//            MediaStore.Audio.Media._ID,
//            MediaStore.Audio.Media.TITLE,
//            MediaStore.Audio.Media.ARTIST,
//            MediaStore.Audio.Media.DATA,
//            MediaStore.Audio.Media.DURATION,
//            MediaStore.Audio.Media.ALBUM_ID
//        )

        val projection = context.resources.getStringArray(R.array.playlist_projection)

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

                SongsRepository(context).createSongFromCursor(cursor)

                cursor.moveToNext()
            }

            cursor.close()
        }

        return array
    }

    fun deleteFromPlaylist(context: Context, playlistId: Long, trackId: Long) {

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

    fun renamePlaylist()
    {

    }

    fun moveItem()
    {

    }

}