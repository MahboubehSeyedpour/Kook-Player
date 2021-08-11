package com.example.cassette.views.Fragments

import android.app.Activity
import android.app.Application
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
import com.example.cassette.adapter.SongsAdapter
import com.example.cassette.models.SongModel
import com.example.cassette.viewModel.SongsViewModel
import kotlinx.android.synthetic.main.fragment_library.*

class Library(val application: Application) : Fragment() {

    companion object Library {

//        var dataset = arrayListOf<SongModel>()
        var songsAdapter: SongsAdapter? = null

        lateinit var viewModel: SongsViewModel

        const val DELETE_REQUEST_CODE = 2

        fun notifyDataSetChanges() {
            songsAdapter?.update()
        }
    }

//    private val viewModel: SongsViewModel by viewModels(
//        factoryProducer = { SavedStateViewModelFactory(application, this) }
//    )

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

        viewModel = ViewModelProvider(this).get(SongsViewModel::class.java)
        context?.let { viewModel.setFragmentContext(it) }
        viewModel.getMutableLiveData().observe(viewLifecycleOwner, songListUpdateObserver)


        songsAdapter = activity?.let {
            SongsAdapter(
                it,
                viewModel.dataset as ArrayList<SongModel>
            )
        }

        notifyDataSetChanges()

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