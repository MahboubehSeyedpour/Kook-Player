package com.example.cassette.views.Fragments

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cassette.R
import com.example.cassette.adapter.Songs_Adapter
import com.example.cassette.datamodels.Songs_ViewModel
import com.example.cassette.models.SongModel
import kotlinx.android.synthetic.main.fragment_library.*

class Library : Fragment() {


    companion object Library {

        var arraylist = arrayListOf<SongModel>()
        var songsAdapter: Songs_Adapter? = null
        val DELETE_REQUEST_CODE = 2

        fun notifyDataSetChanges() {
            songsAdapter?.update()
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {
            2 -> if (resultCode == Activity.RESULT_OK) {
                notifyDataSetChanges()
            }
            else -> {

            }
        }
    }

    override fun onResume() {
        super.onResume()

        val viewModel = ViewModelProvider(this).get(Songs_ViewModel::class.java)
        viewModel.getMutableLiveData().observe(this, songListUpdateObserver)

        notifyDataSetChanges()

        pullToRefresh.setOnRefreshListener {
            notifyDataSetChanges()
            pullToRefresh.setRefreshing(false)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_library, container, false)

//        TODO(check if the manifest permissions had been granted)
//        TODO(take musics in Internal & External storage)

        songsAdapter = activity?.let {
            Songs_Adapter(
                it,
                arraylist
            )
        }

        return view
    }

    val songListUpdateObserver =
        object : Observer<ArrayList<Any>> {
            override fun onChanged(songArrayList: ArrayList<Any>) {
                val recyclerViewAdapter = songsAdapter
                songs_rv.layoutManager = LinearLayoutManager(context)
                songs_rv.adapter = recyclerViewAdapter
            }
        }

}