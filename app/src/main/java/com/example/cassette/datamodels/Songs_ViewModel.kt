package com.example.cassette.datamodels

import androidx.lifecycle.MutableLiveData
import com.example.cassette.models.Song_Model
import com.example.cassette.views.Fragments.Library

class Songs_ViewModel : BaseViewModel() {
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
        liveData.setValue(arrayList);
    }

    override fun populateList() {

//        var song = Song_Model("Nafas e ki budi to", "00:5:32")

        arrayList = Library.arraylist!!  as ArrayList<Any>

//        arrayList.add(song);
//        song = Song_Model("title", "00:5:32")
//        arrayList.add(song);
//        arrayList.add(song);
//        arrayList.add(song);
//        arrayList.add(song);
//        arrayList.add(song);
    }

}