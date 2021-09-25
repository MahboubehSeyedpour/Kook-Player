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

    fun getMusicsUri(): Uri {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
//      TODO(what will happen if there is no valid storage)
    }

    fun getAlbumsUri(): String {
        return "content://media/external/audio/albumart"
    }

    fun getPlayListsUri(): Uri {
        return MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
    }
}