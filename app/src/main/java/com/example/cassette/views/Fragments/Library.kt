package com.example.cassette.views.Fragments

import MusicUtils
import MusicUtils.context
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cassette.R
import com.example.cassette.adapter.Songs_Adapter
import com.example.cassette.datamodels.Songs_ViewModel
import com.example.cassette.models.Song_Model
import kotlinx.android.synthetic.main.fragment_library.*

class Library : Fragment() {


    companion object Library {
        var arraylist: ArrayList<Song_Model>? = null
        var songsAdapter: Songs_Adapter? = null
        val DELETE_REQUEST_CODE = 2
        lateinit var activity: Activity

        fun notifyDataSetChanges() {
            songsAdapter?.arrayList = context?.let { MusicUtils.getListOfMusics(it) }!!
            this.arraylist = songsAdapter?.arrayList
            songsAdapter?.notifyDataSetChanged()
        }


        @RequiresApi(Build.VERSION_CODES.R)
        fun deletMusic(position: Int) {
            val urisToModify = mutableListOf(Library.arraylist?.get(position)?.uri)
            val deletePendingIntent =
                MediaStore.createDeleteRequest(context.contentResolver, urisToModify)

            ActivityCompat.startIntentSenderForResult(
                activity,
                deletePendingIntent.intentSender,
                DELETE_REQUEST_CODE,
                null,
                0,
                0,
                0,
                null
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            2 -> if (resultCode == Activity.RESULT_OK) {
                notifyDataSetChanges()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val viewModel = ViewModelProvider(this).get(Songs_ViewModel::class.java)
        viewModel.getMutableLiveData().observe(this, songListUpdateObserver)

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

        arraylist = context?.let { MusicUtils.getListOfMusics(it) }

        songsAdapter = activity?.let {
            Songs_Adapter(
                it,
                arraylist as ArrayList<Song_Model>
            )
        }
        Library.activity = this!!.getActivity()!!
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