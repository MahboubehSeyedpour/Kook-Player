package com.example.cassette.utlis

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.example.cassette.R
import com.example.cassette.repositories.SongsRepository
import com.example.cassette.repositories.appdatabase.entities.PlaylistModel
import com.example.cassette.repositories.appdatabase.entities.SongModel
import com.example.cassette.views.Fragments.PlaylistFragment


object PlaylistUtils {

    var playlists = ArrayList<PlaylistModel>()
    lateinit var context: Context

    fun createPlaylist(context: Context, name: String): PlaylistModel {

        this.context = context

        val resolver = context?.contentResolver
        val values = ContentValues()
        values.put(MediaStore.Audio.Playlists.NAME, name)
        val uri = FilePathUtlis.getPlayListsUri()
        resolver?.insert(uri, values)


        val countOfSongs = 0
        val songs = arrayListOf<String>()
        val playlist = PlaylistModel(name, countOfSongs, DatabaseConverterUtils.arraylistToString(songs))
        return playlist
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

        /*
       *This code is correct but it has a bug, due to lack of time, I commented  it in this version so that I can check more later
       */

//        add song to the playlist's file
//        val count = getPlaylistSize(context, id)
//        val values = arrayOfNulls<ContentValues>(tracks.size)
//
//        for (i in tracks.indices) {
//            values[i] = ContentValues()
//            values[i]?.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, i + count + 1)
//            values[i]?.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, tracks[i].id)
//        }
//
//        val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", id)
//        val resolver = context.contentResolver
//        resolver.bulkInsert(uri, values)
//        resolver.bulkInsert(uri, values)
//        resolver.notifyChange(Uri.parse(uri.toString()), null)


//        content://media/external_primary/audio/media/14


//        add song to the playlist's app array
        PlaylistFragment.viewModel?.updateDataset()
        for (playlist in PlaylistFragment.viewModel?.getDataSet()!!) {
            if (playlist.id == id) {
                for (song in tracks) {
                    playlist.songs += ",${song.id.toString()}"
                }

            }
        }

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

        val cursor = context.contentResolver.query(uri, null, null, null, sortOrder)
//        val cursor =
//            FileUtils.fetchFiles(
//                FileUtils.FILE_TYPES.PLAYLIST,
//                context,
//                null,
//                null,
//                null,
//                sortOrder
//            )

        if (cursor != null) {
            cursor.moveToFirst()
//            while (cursor != null && cursor.count != 0) {
////                val id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)).toLong()
////                val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
////                val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
////                val data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
////                val duration = MusicUtils.milliSecToDuration(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)).toLong())
////                val albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toLong()
//
////                array.add(Track(id, title, artist, data, duration, albumId))
//
//                array.add(SongsRepository(context).createSongFromCursor(cursor))
//
//                cursor.moveToNext()
//            }

            if (cursor != null && cursor.count != 0) {
                do {
                    cursor.moveToNext()
                    array.add(SongsRepository(context).createSongFromCursor(cursor))
                } while (!cursor.isLast)
            } else {
//                TODO(handle null cursor)
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

}