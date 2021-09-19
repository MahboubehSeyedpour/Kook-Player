package com.example.cassette.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.cassette.repositories.appdatabase.entities.PlaylistModel
import com.example.cassette.repositories.PlaylistRepository

class PlaylistViewModel : BaseViewModel() {


    override var dataset: MutableLiveData<ArrayList<Any>> = MutableLiveData()
    lateinit var context: Context
    lateinit var playlistRepository: PlaylistRepository


    init {
        dataset.value = ArrayList()
    }


    fun setFragmentContext(context: Context) {
        this.context = context
        playlistRepository = PlaylistRepository(context)
        fillRecyclerView()
    }

    override fun fillRecyclerView() {
        updateDataset()
    }

    override fun updateDataset() {

        dataset.value = playlistRepository.getPlaylists()!! as ArrayList<Any>
    }

    fun getDataSet(): ArrayList<PlaylistModel> {
        updateDataset()
        return dataset.value as ArrayList<PlaylistModel>
    }

}