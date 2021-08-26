package com.example.cassette.utlis

import com.example.cassette.views.Fragments.LibraryFragment

object TimeUtils
{
    fun milliSecToDuration(duration: Long): String {
        val minutes = duration / 1000 / 60
        val seconds = duration / 1000 % 60

        if (minutes <10)
        {
            if(seconds <10)
            {
                return "0$minutes:0$seconds"
            }
            return "0$minutes:$seconds"
        }
        return "$minutes:$seconds"
    }

    fun getDurationOfCurrentMusic(): String {
        return LibraryFragment.songsAdapter?.dataset?.get(LibraryFragment.songsAdapter!!.getCurrentPosition())?.duration.toString()
            ?: "00:00"
    }
}