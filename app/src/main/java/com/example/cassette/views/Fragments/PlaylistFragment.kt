package com.example.cassette.views.Fragments

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cassette.R
import com.example.cassette.myInterface.PassData
import com.example.cassette.adapter.PlaylistAdapter
import com.example.cassette.manager.Coordinator
import com.example.cassette.repositories.appdatabase.entities.PlaylistModel
import com.example.cassette.utlis.PlaylistUtils
import com.example.cassette.viewModel.PlaylistViewModel
import com.example.cassette.views.dialogs.CreatePlaylistDialog
import kotlinx.android.synthetic.main.fragment_player_panel.view.*
import kotlinx.android.synthetic.main.fragment_playlist.*
import kotlinx.android.synthetic.main.panel_header_on_collapsed.view.*


class PlaylistFragment : Fragment(), PassData {

    var playlistAdapter: PlaylistAdapter? = null

    private var newPlaylistName: String = ""

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        playlist_rv.layoutManager = GridLayoutManager(context, 2)


    }


    override fun passDataToInvokingFragment(str: String?) {
        newPlaylistName = str ?: ""

        context?.let { it ->
//            spr , storage
//            val playlist = PlaylistUtils.createPlaylist(it, newPlaylistName)
            viewModel?.playlistRepository?.createPlaylist(newPlaylistName)

            viewModel?.updateDataset()
        }
    }

    override fun onResume() {
        super.onResume()



        fab.setOnClickListener {

            val createPlaylist = CreatePlaylistDialog()

            createPlaylist.setTargetFragment(this, 0)
            this.fragmentManager?.let { it1 -> createPlaylist.show(it1, "pl") }

            }

        playlistAdapter?.OnDataSend(
            object : PlaylistAdapter.OnDataSend {
                override fun onSend(context: Activity, id: Long) {

                    viewModel?.updateDataset()
                }
            }
        )



        val mHandler = Handler()
        activity?.runOnUiThread(object : Runnable {
            override fun run() {
                viewModel?.updateDataset()
                mHandler.postDelayed(this, 1000)
            }
        })

    }

    private val playlistUpdateObserver = Observer<ArrayList<Any>> { dataset ->
        playlistAdapter?.dataset = dataset as ArrayList<PlaylistModel>
        playlist_rv.adapter = playlistAdapter
    }

}