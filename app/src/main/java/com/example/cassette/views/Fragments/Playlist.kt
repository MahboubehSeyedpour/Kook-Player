package com.example.cassette.views.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cassette.R
import com.example.cassette.adapter.PlaylistAdapter
import com.example.cassette.datamodels.PlaylistViewModel
import com.example.cassette.models.PlaylistModel
import com.example.cassette.utlis.PlaylistUtils
import com.example.cassette.views.MainActivity
import kotlinx.android.synthetic.main.fragment_playlist.*
import com.example.cassette.views.dialogs.CreatePlaylistDialog as CreatePlaylistDialog1


class Playlist : Fragment() {

    var context: MainActivity? = null
    var playlistAdapter: PlaylistAdapter? = null

    companion object{
        var arrayList = ArrayList<PlaylistModel>()
    }


    override fun onResume() {
        super.onResume()

        val viewModel = ViewModelProvider(this).get(PlaylistViewModel::class.java)
        viewModel.getMutableLiveData().observe(this, playlistUpdateObserver)

        fab.setOnClickListener {

            val createPlaylist = CreatePlaylistDialog1()

            this.fragmentManager?.beginTransaction()
                ?.let { it -> createPlaylist.show(it, "playlist") }

            context?.let { it -> PlaylistUtils.createPlaylist(it, "me2") }

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_playlist, container, false)

        playlistAdapter = activity?.let {
            PlaylistAdapter(
                it,
                arrayList
            )
        }

        return view
    }

    val playlistUpdateObserver =
        object : Observer<ArrayList<Any>> {
            override fun onChanged(songArrayList: ArrayList<Any>) {
                val recyclerViewAdapter = playlistAdapter
                playlist_rv.layoutManager = GridLayoutManager(context, 2)
                playlist_rv.adapter = recyclerViewAdapter
            }
        }


}