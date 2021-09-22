package com.example.kookplayer.views.Fragments

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kookplayer.adapter.PlaylistPageAdapater
import com.example.kookplayer.databinding.FragmentPlaylistPageBinding
import com.example.kookplayer.repositories.appdatabase.entities.SongModel
import com.example.kookplayer.viewModel.PlaylistPageViewModel


class PlaylistPageFragment(val playlistId: Long) : Fragment() {

    lateinit var binding: FragmentPlaylistPageBinding



    companion object {
        lateinit var viewModel: PlaylistPageViewModel
        lateinit var playlistSongsAdapter: PlaylistPageAdapater
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentPlaylistPageBinding.inflate(inflater, container, false)


        viewModel = ViewModelProvider(this).get(PlaylistPageViewModel::class.java)
        viewModel.setPlayllistId(playlistId)
        viewModel!!.dataset.observe(viewLifecycleOwner, playlistSongsObserer)

        playlistSongsAdapter = context?.let {
            PlaylistPageAdapater(
                viewModel.getDataset(),
                it as Activity
            )
        }!!

        playlistSongsAdapter.playlist_name = getPlaylistName(playlistId)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val playlists = PlaylistFragment.viewModel?.getDataSet()
        for (playlist in playlists!!) {
            if (playlist.id == playlistId) {
                binding.playlistNameTv.text = playlist.name
            }
        }


        binding.playlistsSongsRv.layoutManager = LinearLayoutManager(context)

//        binding.playlistsSongsRv.adapter = playlistSongsAdapter


        binding.playlistBackBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        playlistSongsAdapter?.OnDataSend(
            object : PlaylistPageAdapater.OnDataSend {
                override fun onSend(context: Activity, id: String) {
                    viewModel.updateDataset()
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

    fun getViewModel(): PlaylistPageViewModel
    {
        return viewModel
    }


    override fun onResume() {
        super.onResume()


    }


    private val playlistSongsObserer = Observer<ArrayList<Any>> { dataset ->
        playlistSongsAdapter?.dataset = dataset as ArrayList<SongModel>
        binding.playlistsSongsRv.adapter = playlistSongsAdapter
    }

    fun getPlaylistName(id: Long): String {
        for (playlist in PlaylistFragment.viewModel?.getDataSet()!!) {
            if (playlist.id == id) {
                return playlist.name
            }
        }
        return ""
    }

}