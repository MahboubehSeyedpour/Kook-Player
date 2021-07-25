package com.example.cassette.datamodels

import androidx.lifecycle.MutableLiveData
import com.example.cassette.views.Fragments.Library

class SongsViewModel : BaseViewModel() {

    override var liveData = MutableLiveData<ArrayList<Any>>()
    override var dataset = ArrayList<Any>()

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
        liveData.value = dataset
    }

    override fun populateList() {

//        var song = Song_Model("Nafas e ki budi to", "00:5:32")

        dataset = Library.dataset!!  as ArrayList<Any>

//        arrayList.add(song);
//        song = Song_Model("title", "00:5:32")
//        arrayList.add(song);
//        arrayList.add(song);
//        arrayList.add(song);
//        arrayList.add(song);
//        arrayList.add(song);
    }

}