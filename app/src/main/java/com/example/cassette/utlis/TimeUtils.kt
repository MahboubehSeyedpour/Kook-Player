package com.example.cassette.utlis

import com.example.cassette.views.Fragments.Library

object TimeUtils
{
    fun milliSecToDuration(duration: Long): String {
        val millisec_temp = duration / 1000
        val seconds_final = millisec_temp % 60
        val minutes_temp = millisec_temp / 60
        val minutes_final = minutes_temp % 60 + millisec_temp / 60
        val hour_final = minutes_temp / 60
//        return "$hour_final : $minutes_final : $seconds_final"
        return "$minutes_final : $seconds_final"
    }

    fun getDurationOfCurrentMusic(): String {
        return Library.songsAdapter?.dataset?.get(Library.songsAdapter!!.getCurrentPosition())?.duration.toString()
            ?: "00:00"
    }
}