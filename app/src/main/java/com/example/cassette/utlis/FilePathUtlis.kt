package com.example.cassette.utlis

import android.net.Uri
import android.os.Environment
import android.os.Environment.DIRECTORY_MUSIC
import android.provider.MediaStore
import java.io.File

//find file paths for shared/external storage
//using android.os.Environment / getExternalStorageDirectory / etc.

object FilePathUtlis {
    val MUSICS_INTERNAL_STORAGE: File =
        Environment.getExternalStoragePublicDirectory(DIRECTORY_MUSIC)

    //TODO("findpath for external storage")
    val MUSIC_CANONICAL_PATH: String = MUSICS_INTERNAL_STORAGE.canonicalPath

    fun getMusicsUri(): Uri {
        val i = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        return i
    }
}