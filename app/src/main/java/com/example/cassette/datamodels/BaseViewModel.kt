package com.example.cassette.datamodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.collections.ArrayList

abstract class BaseViewModel : ViewModel() {

     open lateinit var liveData : MutableLiveData<ArrayList<Any>>
     open lateinit var arrayList : ArrayList<Any>

    abstract fun getMutableLiveData():  MutableLiveData<ArrayList<Any>>

    abstract fun fillRecyclerView()

    abstract fun populateList()
}