package com.example.kookplayer.utlis

import android.net.Uri
import android.os.Environment
import android.os.Environment.DIRECTORY_MUSIC
import android.provider.MediaStore
import java.io.File

//find file paths for shared/external storage
//using android.os.Environment / getExternalStorageDirectory / etc.

object FilePathUtlis {
    private val MUSICS_INTERNAL_STORAGE: File =
        Environment.getExternalStoragePublicDirectory(DIRECTORY_MUSIC)

    //TODO("find path for external storage")
    val MUSIC_CANONICAL_PATH: String = MUSICS_INTERNAL_STORAGE.canonicalPath

    fun getMusicsUri(): Uri {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
//        when {
//            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> return MediaStore.Audio.Media.getContentUri(
//                MediaStore.VOLUME_EXTERNAL
//            )
//            else -> return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
//        }

//        TODO(what will happen if there is no valid storage)
    }

    fun getAlbumsUri(): String {
        return "content://media/external/audio/albumart"
    }

    fun getPlayListsUri(): Uri {
        return MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
    }
}