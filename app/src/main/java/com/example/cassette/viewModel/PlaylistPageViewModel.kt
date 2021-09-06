package com.example.cassette.viewModel

import androidx.lifecycle.MutableLiveData
import com.example.cassette.repositories.PlaylistPageRepository
import com.example.cassette.repositories.appdatabase.entities.SongModel

class PlaylistPageViewModel() : BaseViewModel() {

    override var dataset: MutableLiveData<ArrayList<Any>> = MutableLiveData()
    private var database : ArrayList<Any> = arrayListOf()
    lateinit var playlistPageRepository: PlaylistPageRepository
    private var playlistId: Long = -1L


    fun setPlayllistId(pId: Long)
    {
        playlistId = pId
        playlistPageRepository = PlaylistPageRepository(playlistId)
        fillRecyclerView()
    }

    override fun fillRecyclerView() {
        database = playlistPageRepository.getSongs() as ArrayList<Any>
    }

    override fun updateDataset() {

    }

    fun getDataset(): ArrayList<SongModel>
    {
        return database as ArrayList<SongModel>
    }
}