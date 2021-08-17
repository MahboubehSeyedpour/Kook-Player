//package com.example.cassette.utlis

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.cassette.BuildConfig
import com.example.cassette.models.SongModel
import com.example.cassette.utlis.FileUtils
import com.example.cassette.views.Fragments.Library


object SongUtils {

    lateinit var context: Context

    fun getSongPosition(song: SongModel): Int{
       return Library.viewModel.getDataSet().indexOf(song)
    }


    fun shareMusic(song: SongModel) {

        val fileToBeShared = FileUtils.convertSongToFile(song.data)

        val fileUri: Uri? = FileUtils.getUriForFile(
            context,
            "${BuildConfig.APPLICATION_ID}.FileProvider",
            fileToBeShared
        )

        fileUri?.let { FileUtils.shareFile(context, it) }
    }


    @RequiresApi(Build.VERSION_CODES.R)
    fun deletMusic(activity: Activity, uri: Uri) {
        val urisToModify = mutableListOf(uri)
        val deletePendingIntent =
            MediaStore.createDeleteRequest(context.contentResolver, urisToModify)

        ActivityCompat.startIntentSenderForResult(
            activity,
            deletePendingIntent.intentSender,
            Library.DELETE_REQUEST_CODE,
            null,
            0,
            0,
            0,
            null
        )
    }

    fun shareMultipleMusics(vararg positions: ArrayList<Int>) {
//        TODO(share multiple musics)
    }

    fun showDetails(position: Int) {
//        TODO(retrieve details of the song)
    }

}