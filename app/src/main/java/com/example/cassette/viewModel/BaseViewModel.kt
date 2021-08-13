package com.example.cassette.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    open lateinit var liveData: MutableLiveData<ArrayList<Any>>
    open lateinit var dataset: ArrayList<Any>

    abstract fun getMutableLiveData(): MutableLiveData<ArrayList<Any>>

    abstract fun fillRecyclerView()

    abstract fun populateList(): ArrayList<Any>
}