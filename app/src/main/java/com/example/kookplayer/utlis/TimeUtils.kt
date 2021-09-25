package com.example.kookplayer.utlis

import com.example.kookplayer.views.Fragments.LibraryFragment
import java.util.*

object TimeUtils {
    fun getReadableDuration(durationInMilliSeconds: Long): String {

        val minutes = durationInMilliSeconds / 1000 / 60
        val seconds = durationInMilliSeconds / 1000 % 60

        return if (minutes < 60)
            String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        else
            String.format(Locale.getDefault(), "%03d:%02d", minutes, seconds)

    }

    fun getDurationOfCurrentMusic(): String {
        return LibraryFragment.songsAdapter?.dataset?.get(LibraryFragment.songsAdapter!!.getCurrentPosition())?.duration.toString()
            ?: "00:00"
    }
}