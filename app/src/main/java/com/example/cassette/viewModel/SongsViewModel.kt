package com.example.cassette.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.cassette.models.SongModel
import com.example.cassette.repositories.SongsRepository

class SongsViewModel : BaseViewModel() {

    override var liveData = MutableLiveData<ArrayList<Any>>()
    override var dataset = ArrayList<Any>()
    lateinit var context: Context
    lateinit var songsRepository: SongsRepository


    init {
        liveData = MutableLiveData()
    }


    override fun getMutableLiveData(): MutableLiveData<ArrayList<Any>> {
        return liveData
    }

    fun setFragmentContext(context: Context) {
        this.context = context
        songsRepository = SongsRepository(context)
        fillRecyclerView()
    }


    override fun fillRecyclerView() {
//        REST API can be called here
        populateList();
        liveData.value = dataset
    }

    override fun populateList() {

//        var song = Song_Model("Nafas e ki budi to", "00:5:32")

        dataset = songsRepository.getListOfSongs()!! as ArrayList<Any>

        val i = 0
//        arrayList.add(song);
//        song = Song_Model("title", "00:5:32")
//        arrayList.add(song);
//        arrayList.add(song);
//        arrayList.add(song);
//        arrayList.add(song);
//        arrayList.add(song);
    }

    fun getDataSet(): ArrayList<SongModel>
    {
        return dataset as ArrayList<SongModel>
    }

}