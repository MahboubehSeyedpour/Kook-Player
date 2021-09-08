package com.example.cassette.viewModel

import androidx.lifecycle.MutableLiveData
import com.example.cassette.repositories.PlaylistPageRepository
import com.example.cassette.repositories.appdatabase.entities.SongModel

class PlaylistPageViewModel() : BaseViewModel() {

    override var dataset: MutableLiveData<ArrayList<Any>> = MutableLiveData()
    lateinit var playlistPageRepository: PlaylistPageRepository
    private var playlistId: Long = -1L


    init {
        dataset.value = ArrayList()
    }

    fun setPlayllistId(pId: Long)
    {
        playlistId = pId
        playlistPageRepository = PlaylistPageRepository(playlistId)
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