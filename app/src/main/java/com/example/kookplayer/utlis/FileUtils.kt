package com.example.kookplayer.utlis

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import java.io.File

object FileUtils {

    enum class FILE_TYPES {
        MUSIC {
            val path = FilePathUtlis.getMusicsUri()
        },
        PLAYLIST
    }

    fun shareFile(context: Context, fileUri: Uri) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.type = "audio/*"
        context.startActivity(Intent.createChooser(shareIntent, "share audio"))
    }


    fun convertSongToFile(fileUri: String): File {
        return File(fileUri)
    }

    fun fetchFiles(
        fileType: FILE_TYPES,
        context: Context,
        projection: Array<String>? = null,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        sortOrder: String? = null
    ): Cursor? {

        val path: Uri = when (fileType) {
            FILE_TYPES.MUSIC -> FilePathUtlis.getMusicsUri()
            FILE_TYPES.PLAYLIST -> FilePathUtlis.getPlayListsUri()
        }

        try {
            return context?.contentResolver?.query(
                path,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )
        } catch (exception: java.lang.Exception) {
//          TODO(handle the exception)
        }
        return null
    }


}
