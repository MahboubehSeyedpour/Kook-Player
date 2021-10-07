package com.example.kookplayer.viewModel

import androidx.lifecycle.MutableLiveData
import com.example.kookplayer.db.entities.SongModel
import com.example.kookplayer.repositories.IPlaylistPageRepository

class PlaylistPageViewModel() : BaseViewModel() {

    override var dataset: MutableLiveData<ArrayList<Any>> = MutableLiveData()
    lateinit var playlistPageRepository: IPlaylistPageRepository
    private var playlistId: Long = -1L


    init {
        dataset.value = ArrayList()
    }

    fun setPlayllistId(pId: Long)
    {
        playlistId = pId
        playlistPageRepository = IPlaylistPageRepository(playlistId)
        fillRecyclerView()
    }

    override fun fillRecyclerView() {
        updateDataset()

    }

    override fun updateDataset() {
        dataset.value = playlistPageRepository.getSongs() as ArrayList<Any>
    }

    fun getDataset(): ArrayList<SongModel>
    {
        return dataset.value as ArrayList<SongModel>
    }
}