package com.example.kookplayer.helper

import com.example.kookplayer.db.entities.SongModel

object StateMananger {

    lateinit var song: SongModel

    //----------------------load previous state---------------------

    fun loadPreviousStates() {
        loadSong()
//        TODO(loadSongPositionInMediaPlayer())
    }


    fun loadSong() {

//        for (steper in LibraryFragment.viewModel.songsRepository.getListOfSongs()) {
//            if (steper.id == SharedPrefUtils.getLastSongId()) {
//                song = steper
//                Coordinator.currentPlayingSong =song
//                song.data?.let { Coordinator.play(it) }
//            }
//        }
    }

//TODO(loadRepeatOneMode)
//TODO(loadShuffleMode)
}