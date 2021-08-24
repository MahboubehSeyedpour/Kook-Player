package com.example.cassette.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    open lateinit var dataset: MutableLiveData<ArrayList<Any>>

//    abstract fun getMutableLiveData(): MutableLiveData<ArrayList<Any>>

    abstract fun fillRecyclerView()

//    abstract fun populateList(): ArrayList<Any>
}