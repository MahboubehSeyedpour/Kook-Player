package com.example.cassette.datamodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cassette.models.Song
import java.util.*
import kotlin.collections.ArrayList

open class BaseViewModel() : ViewModel() {

    var liveData = MutableLiveData<ArrayList<Song>>();
    var arrayList = ArrayList<Song>();

    init {
        liveData = MutableLiveData()
        fillRecyclerView()
    }

    open fun getMutableLiveData():  MutableLiveData<ArrayList<Song>>{
        return liveData
    }


    final fun fillRecyclerView() {
//        REST API can be called here
        populateList();
        liveData.setValue(arrayList);
    }

    open fun populateList() {

        val song = Song("title", 3200)

        arrayList.add(song);
        arrayList.add(song);
        arrayList.add(song);
        arrayList.add(song);
        arrayList.add(song);
        arrayList.add(song);
    }
}