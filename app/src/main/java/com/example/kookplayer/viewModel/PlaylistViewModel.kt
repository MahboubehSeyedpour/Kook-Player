package com.example.kookplayer.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.kookplayer.db.entities.PlaylistModel
import com.example.kookplayer.repositories.IPlaylistRepository

class PlaylistViewModel : BaseViewModel() {


    override var dataset: MutableLiveData<ArrayList<Any>> = MutableLiveData()
    lateinit var playlistRepository: IPlaylistRepository


    init {
        dataset.value = ArrayList()
    }


    fun setFragmentContext(context: Context) {
        playlistRepository = IPlaylistRepository(context)
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