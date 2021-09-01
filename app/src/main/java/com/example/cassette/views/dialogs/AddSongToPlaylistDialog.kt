package com.example.cassette.views.dialogs

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cassette.R
import com.example.cassette.myInterface.PassDataForSelectPlaylists
import com.example.cassette.adapter.AddSongToPlaylistAdapter
import com.example.cassette.databinding.AddSongToPlaylistBinding
import com.example.cassette.repositories.appdatabase.entities.PlaylistModel
import com.example.cassette.repositories.appdatabase.entities.SongModel

class AddSongToPlaylistDialog(val array: ArrayList<PlaylistModel>) : DialogFragment() {

    lateinit var binding: AddSongToPlaylistBinding
    var playlistAdapter: AddSongToPlaylistAdapter? = null
    lateinit var dataSend: OnDataSend


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner_bg);

        val view = inflater.inflate(R.layout.add_song_to_playlist, container, false)
        initBinding(view)

//        viewModel.updateDataset()

        playlistAdapter = activity?.let {
            AddSongToPlaylistAdapter(
                it,
                array
            )
        }

        binding.playlists.layoutManager = LinearLayoutManager(context)
        binding.playlists.adapter = playlistAdapter


        return view

    }

    override fun onResume() {
        super.onResume()

        binding.acceptSelectedPlaylistBtn.setOnClickListener {

            val targetFragment = targetFragment
            val passData : PassDataForSelectPlaylists = targetFragment as PassDataForSelectPlaylists
            targetFragment.passDataToInvokingFragment(AddSongToPlaylistAdapter.choices)

            this.dismiss()
        }

    }

    fun initBinding(view: View) {
        binding = AddSongToPlaylistBinding.bind(view)
    }

    interface OnDataSend {
        fun onSend(context: Activity, songModel: SongModel)
    }

    fun OnDataSend(dataSend: OnDataSend) {
        this.dataSend = dataSend
    }

}



