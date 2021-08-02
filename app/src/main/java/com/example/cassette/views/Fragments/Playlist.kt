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
import com.example.cassette.views.dialogs.CreatePlaylistDialog
import kotlinx.android.synthetic.main.fragment_playlist.*


class Playlist : Fragment() {
    
    var playlistAdapter: PlaylistAdapter? = null

    companion object
    {
        var viewModel: PlaylistViewModel? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_playlist, container, false)

        viewModel = ViewModelProvider(this).get(PlaylistViewModel::class.java)
        viewModel!!.getMutableLiveData().observe(this, playlistUpdateObserver)


        viewModel?.dataset = PlaylistUtils.getPlaylists(context) as ArrayList<Any>

        playlistAdapter = activity?.let {
            PlaylistAdapter(
                it,
                viewModel?.dataset as ArrayList<PlaylistModel>
            )
        }
        return view
    }

    fun notifyDatasetChanged() {
        playlistAdapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()

        fab.setOnClickListener {

            val createPlaylist = CreatePlaylistDialog()

            this.fragmentManager?.beginTransaction()
                ?.let { it -> createPlaylist.show(it, "playlist") }

//            context?.let { it -> PlaylistUtils.createPlaylist(it, "me2") }

            viewModel?.liveData?.value = PlaylistUtils.getPlaylists(context) as ArrayList<Any>
        }
    }


    val playlistUpdateObserver =
        object : Observer<ArrayList<Any>> {
            override fun onChanged(plalistArrayList: ArrayList<Any>) {
                val recyclerViewAdapter = playlistAdapter
                playlist_rv.layoutManager = GridLayoutManager(context, 2)
                playlist_rv.adapter = recyclerViewAdapter
            }
        }
}