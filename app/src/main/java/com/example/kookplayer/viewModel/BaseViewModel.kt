package com.example.kookplayer.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    open lateinit var dataset: MutableLiveData<ArrayList<Any>>

    abstract fun fillRecyclerView()
    abstract fun updateDataset()
}