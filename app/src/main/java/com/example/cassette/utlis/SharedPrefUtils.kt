package com.example.cassette.utlis

import com.example.cassette.R
import com.example.cassette.manager.Coordinator
import com.example.cassette.manager.StateMananger
import com.example.cassette.views.MainActivity
import com.example.cassette.views.MainActivity.Companion.activity

object SharedPrefUtils {

//    fun getLastSongId(): Long {
//        return MainActivity.sharedPreferences.getLong(
//            activity.resources.getString(R.string.preference_last_song_id),
//            -1L
//        )
//    }
//
//    fun getLastSongPosition(): Int {
//        return MainActivity.sharedPreferences.getInt(
//            activity.resources.getString(R.string.preference_last_song_position),
//            -1
//        )
//    }
//
//    fun getRepeatOneMode(): Int {
//        return MainActivity.sharedPreferences.getInt(
//            activity.resources.getString(R.string.preference_repeat_one),
//            -1
//        )
//    }
//
//    fun getShuffleMode(): Int {
//        return MainActivity.sharedPreferences.getInt(
//            activity.resources.getString(R.string.preference_shuffle_is_on),
//            -1
//        )
//    }
//
//    fun saveState() {
//        with(MainActivity.sharedPreferences.edit())
//        {
//            putInt(
//                activity.resources.getString(R.string.preference_shuffle_is_on),
//                Coordinator.getShuffleStatus()
//            )
//            putInt(
//                activity.resources.getString(R.string.preference_repeat_one),
//                Coordinator.getRepeatOneStatus()
//            )
////            putLong(
////                activity.resources.getString(R.string.preference_last_song_id),
////                Coordinator.getCurrentPlayingSong().id ?: -1
////            )
//            putLong(
//                activity.resources.getString(R.string.preference_last_song_id),
//                Coordinator.currentPlayingSong?.id ?: -1L
//            )
//            putInt(
//                activity.resources.getString(R.string.preference_last_song_position),
//                Coordinator.getCurrentSongPosition()
//            )
//            apply()
//        }
//    }
//
//    fun loadLastState() {
//        StateMananger.loadPreviousStates()
//    }

}