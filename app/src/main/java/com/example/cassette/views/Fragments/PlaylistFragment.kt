package com.example.cassette.views.Fragments

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cassette.R
import com.example.cassette.adapter.PlaylistAdapter
import com.example.cassette.myInterface.PassData
import com.example.cassette.repositories.appdatabase.entities.PlaylistModel
import com.example.cassette.viewModel.PlaylistViewModel
import com.example.cassette.views.MainActivity
import com.example.cassette.views.dialogs.CreatePlaylistDialog
import kotlinx.android.synthetic.main.fragment_playlist.*


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

                override fun openPlaylist(id: Long) {

                    val mainFragment = PlaylistPageFragment()
                    val fragmentManager: FragmentManager = MainActivity.activity.supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.addToBackStack(null)
                    transaction.add(
                        R.id.fragment_base_container,
                        mainFragment,
                        "bottom sheet container"
                    )
                        .commit()

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