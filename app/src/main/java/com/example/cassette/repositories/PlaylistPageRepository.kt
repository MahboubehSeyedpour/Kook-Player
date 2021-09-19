package com.example.cassette.repositories

import com.example.cassette.myInterface.PlaylistPageRepositoryInterface
import com.example.cassette.repositories.appdatabase.entities.SongModel
import com.example.cassette.utlis.DatabaseConverterUtils
import com.example.cassette.views.Fragments.LibraryFragment
import com.example.cassette.views.Fragments.PlaylistFragment
import kotlinx.coroutines.runBlocking

class PlaylistPageRepository(private val playlistId: Long) : PlaylistPageRepositoryInterface {

    override fun getSongsIdFromDatabase(): String {
        var songsOfPlaylist: String = ""
        runBlocking {
            songsOfPlaylist = PlaylistRepository.db.playlistDao().getSongsOfPlaylist(playlistId)
        }
        return songsOfPlaylist
    }

    override fun songsIdToSongModelConverter(songId: String): SongModel? {
        val allSongsInStorage = LibraryFragment.viewModel.getDataSet()

        for (song in allSongsInStorage) {
            if (song.id == songId.toLong()) {
                return song
            }
        }
        return null
    }

    override fun getSongs(): ArrayList<SongModel> {

        var songs: ArrayList<SongModel> = arrayListOf()

        val songsIdInString = getSongsIdFromDatabase()
        if (songsIdInString != null) {
            val songsIdInArraylist = convertStringToArraylist(songsIdInString)

            for (songId in songsIdInArraylist) {
                val realSong = songsIdToSongModelConverter(songId)
                if (realSong != null)
                    songs.add(realSong)
            }
        }

        return songs
    }

    fun convertStringToArraylist(songs: String): ArrayList<String> {
        return DatabaseConverterUtils.stringToArraylist(songs)
    }

    fun removeSongFromPlaylist(songId: String) {

        PlaylistFragment.viewModel?.playlistRepository?.removeSongFromPlaylist(playlistId, songId)




////          update playlist object
//            for (playlist in PlaylistFragment.viewModel?.getDataSet()!!) {
//                if (playlist.id == playlistId) {
//                    playlist.songs = playlistSongsAfterRemoveItem
//                    break
//                }
//            }
//        }
    }

}
