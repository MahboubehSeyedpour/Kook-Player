package com.example.cassette.viewModel

import androidx.lifecycle.MutableLiveData
import com.example.cassette.utlis.PlaylistUtils

class PlaylistViewModel : BaseViewModel() {

    override var liveData = MutableLiveData<ArrayList<Any>>()
    override var dataset = ArrayList<Any>()

    override fun getMutableLiveData(): MutableLiveData<ArrayList<Any>> {
        return liveData
    }

    override fun fillRecyclerView() {
//        REST API can be called here

        liveData.value = populateList()
    }

    override fun populateList(): ArrayList<Any> {

        dataset = PlaylistUtils.getCachedPlaylists() as ArrayList<Any>
        return dataset
    }

    init {
        liveData = MutableLiveData<ArrayList<Any>>()
        fillRecyclerView()
    }

}