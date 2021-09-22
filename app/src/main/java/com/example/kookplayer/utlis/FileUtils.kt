package com.example.kookplayer.utlis

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File

object FileUtils {

    enum class FILE_TYPES {
        MUSIC {
            val path = FilePathUtlis.getMusicsUri()
        },
        PLAYLIST
    }

    fun fileType(file: File): String {
        return when {
            file.isDirectory -> "${file.name} + is directory"
            file.isFile -> "${file.name} + is file"
            else -> "${file.name} not file, not dir"
        }
    }

    fun shareFile(context: Context, fileUri: Uri)
    {
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

    fun getUriForFile(context: Context, authority: String, fileToBeShared: File): Uri?
    {
        return try {
            val uri =  FileProvider.getUriForFile(
                context,
                authority,
                fileToBeShared
            )
            uri
        } catch (e: IllegalArgumentException) {
            Log.e(
                "File Selector",
                "The selected file can't be shared: $fileToBeShared"
            )
            null
        }
    }

    fun checkFileExistence(file: File): Boolean {
        return file.exists()
    }

    fun fetchFiles(
        fileType: FILE_TYPES,
        context: Context,
        projection: Array<String>? = null,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        sortOrder: String? = null
    ): Cursor? {

        val path: Uri

        path = when (fileType) {
            FILE_TYPES.MUSIC -> FilePathUtlis.getMusicsUri()
            FILE_TYPES.PLAYLIST -> FilePathUtlis.getPlayListsUri()
        }

        try {
            val cursor: Cursor? = context?.contentResolver?.query(
                path,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )
            return cursor
        } catch (exception: java.lang.Exception) {
//          TODO(handle the exception)
        }
        return null
    }



}
