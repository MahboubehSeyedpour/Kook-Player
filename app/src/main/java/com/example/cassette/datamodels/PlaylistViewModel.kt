package com.example.cassette.datamodels

import androidx.lifecycle.MutableLiveData
import com.example.cassette.models.PlaylistModel
import com.example.cassette.views.Fragments.Playlist

class PlaylistViewModel : BaseViewModel() {

    override var liveData = MutableLiveData<ArrayList<Any>>()
    override var arrayList = ArrayList<Any>()

    init {
        liveData = MutableLiveData()
        fillRecyclerView()
    }


    override fun getMutableLiveData(): MutableLiveData<ArrayList<Any>> {
        return liveData
    }


    override fun fillRecyclerView() {
//        REST API can be called here
        populateList();
        liveData.value = arrayList
    }

    override fun populateList() {

        val playlist1 = PlaylistModel(1, "Road Trip" , 12)
        Playlist.arrayList.add(playlist1)

    }
}