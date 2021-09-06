package com.example.cassette.views.Fragments

import android.os.Binder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cassette.R
import com.example.cassette.adapter.PlaylistAdapter
import com.example.cassette.adapter.PlaylistPageAdapater
import com.example.cassette.adapter.SongsAdapter
import com.example.cassette.databinding.FragmentPlaylistPageBinding
import com.example.cassette.repositories.appdatabase.entities.PlaylistModel
import com.example.cassette.repositories.appdatabase.entities.SongModel
import com.example.cassette.viewModel.PlaylistPageViewModel
import com.example.cassette.viewModel.PlaylistViewModel
import com.example.cassette.viewModel.SongsViewModel
import kotlinx.android.synthetic.main.fragment_library.*
import kotlinx.android.synthetic.main.fragment_playlist_page.*


class PlaylistPageFragment(val playlistId: Long) : Fragment() {

    lateinit var binding: FragmentPlaylistPageBinding

    companion object {

        lateinit var playlistSongsAdapter: PlaylistPageAdapater

        lateinit var viewModel: PlaylistPageViewModel

        lateinit var selectedSong: SongModel
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentPlaylistPageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val viewModel = ViewModelProvider(this).get(PlaylistPageViewModel::class.java)
        viewModel.setPlayllistId(playlistId)
        playlistSongsAdapter = context?.let { PlaylistPageAdapater(viewModel.getDataset(), it) }!!

        binding.playlistsSongsRv.layoutManager = LinearLayoutManager(context)

        binding.playlistsSongsRv.adapter = playlistSongsAdapter

    }


    override fun onResume() {
        super.onResume()

    }
}