package com.example.cassette.viewModel

//class PlaylistViewModel : BaseViewModel() {
//
//
//    override var liveData = MutableLiveData<ArrayList<Any>>()
//    override var dataset = ArrayList<Any>()
//    lateinit var context : Context
//
//
//    init {
//        liveData = MutableLiveData<ArrayList<Any>>()
//
////        fillRecyclerView()
//    }
//
//    fun setViewModelContext(context: Context)
//    {
//        this.context = context
//        liveData.value = populateList()
//    }
//
//    override fun getMutableLiveData(): MutableLiveData<ArrayList<Any>> {
//
//        return liveData
//    }
//
//    override fun fillRecyclerView() {
//////        REST API can be called here
////
////        liveData.value = populateList()
//    }
//
//    override fun populateList(): ArrayList<Any> {
//
//        dataset = PlaylistRepository().getPlaylists(context) as ArrayList<Any>
//        return dataset
//    }
//
//    fun updateDataset(): ArrayList<Any> {
//
//        dataset = PlaylistRepository().getPlaylists(context) as ArrayList<Any>
//        return dataset
//    }
//
//}