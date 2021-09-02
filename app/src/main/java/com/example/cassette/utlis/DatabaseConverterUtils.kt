package com.example.cassette.utlis

object DatabaseConverterUtils {
    fun stringToArraylist(songs: String): ArrayList<String> {
        val arr = songs.trim().splitToSequence(',')
            .filter { it.isNotEmpty() }
            .toList()

        return arr as ArrayList<String>
    }

    fun arraylistToString(songs: ArrayList<String>): String {
        var str: String = ""
        for (songId in songs) {
            str += "$songId,"
        }

        return str
    }
}