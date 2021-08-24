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
import com.example.cassette.models.PlaylistModel
import com.example.cassette.utlis.PlaylistUtils
import com.example.cassette.viewModel.PlaylistViewModel
import com.example.cassette.views.dialogs.CreatePlaylistDialog
import kotlinx.android.synthetic.main.fragment_playlist.*


class Playlist : Fragment() {

    var playlistAdapter: PlaylistAdapter? = null

    companion object {
        var viewModel: PlaylistViewModel? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_playlist, container, false)

        viewModel = ViewModelProvider(this).get(PlaylistViewModel::class.java)
        context?.let { viewModel?.setFragmentContext(it) }
        viewModel!!.dataset.observe(viewLifecycleOwner, playlistUpdateObserver)

        playlistAdapter = activity?.let {
            PlaylistAdapter(
                it,
                viewModel?.dataset?.value as ArrayList<PlaylistModel>
            )
        }
        return view
    }


    override fun onResume() {
        super.onResume()

        playlist_rv.layoutManager = GridLayoutManager(context, 2)

        fab.setOnClickListener {

            val createPlaylist = CreatePlaylistDialog()

            this.fragmentManager?.beginTransaction()
                ?.let { it -> createPlaylist.show(it, "playlist") }

            context?.let { it ->
                PlaylistUtils.createPlaylist(it, "test playlist0")

                viewModel?.updateDataset()
            }

        }
        viewModel?.updateDataset()
    }

    val playlistUpdateObserver = Observer<ArrayList<Any>> { dataset ->
        playlistAdapter?.dataset = dataset as ArrayList<PlaylistModel>
        playlist_rv.adapter = playlistAdapter
    }
}