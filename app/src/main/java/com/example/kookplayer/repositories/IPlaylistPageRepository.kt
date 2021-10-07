package com.example.kookplayer.repositories

import com.example.kookplayer.db.entities.SongModel
import com.example.kookplayer.myInterface.IPlaylistPageRepository
import com.example.kookplayer.utlis.DatabaseConverterUtils
import com.example.kookplayer.views.Fragments.LibraryFragment
import com.example.kookplayer.views.Fragments.PlaylistFragment
import kotlinx.coroutines.runBlocking

class IPlaylistPageRepository(private val playlistId: Long) : IPlaylistPageRepository {

    override fun getSongsIdFromDatabase(): String {
        var songsOfPlaylist: String = ""
        runBlocking {
            songsOfPlaylist = IRoomRepository.localDatabase.playlistDao().getSongsOfPlaylist(playlistId)
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

    }

}
