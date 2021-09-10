//package com.example.cassette.utlis

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.cassette.BuildConfig
import com.example.cassette.repositories.appdatabase.entities.SongModel
import com.example.cassette.utlis.FilePathUtlis
import com.example.cassette.utlis.FileUtils
import com.example.cassette.views.Fragments.LibraryFragment
import com.example.cassette.views.MainActivity
import java.io.File


object SongUtils {

    lateinit var context: Context

    fun getSongPosition(song: SongModel): Int {
        return LibraryFragment.viewModel.getDataSet().indexOf(song)
    }


    fun shareMusic(context: Context, song: SongModel) {

        val fileToBeShared = song.data?.let { FileUtils.convertSongToFile(it) }

        val fileUri: Uri? = fileToBeShared?.let {
            FileUtils.getUriForFile(
                context,
                "${BuildConfig.APPLICATION_ID}.FileProvider",
                it
            )
        }

        fileUri?.let { FileUtils.shareFile(context, it) }
    }



    fun deletMusic(activity: Activity, uri: Uri) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val urisToModify = mutableListOf(uri)
            val deletePendingIntent =
                MediaStore.createDeleteRequest(context.contentResolver, urisToModify)

            ActivityCompat.startIntentSenderForResult(
                activity,
                deletePendingIntent.intentSender,
                LibraryFragment.DELETE_REQUEST_CODE,
                null,
                0,
                0,
                0,
                null
            )
        } else {
          context.contentResolver.delete(uri, null, null)
        }

    }

    fun del(songId: String, uris: Uri)
    {
        try {
            val where = "${MediaStore.Audio.AudioColumns._ID} = ?"
            val args = arrayOf(songId)
            val uri = FilePathUtlis.getMusicsUri()
            MainActivity.activity.baseContext.contentResolver.delete(uri, where, args)
            LibraryFragment.viewModel.updateDataset()
        } catch (ignored: Exception) {
        }
    }

    fun shareMultipleMusics(vararg positions: ArrayList<Int>) {
//        TODO(share multiple musics)
    }


    fun getSongById(id: Long): SongModel? {
        for (song in LibraryFragment.viewModel.getDataSet()) {
            if (song.id == id)
                return song
        }

        return null
    }

}