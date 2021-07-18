package com.example.cassette.views.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cassette.R
import com.example.cassette.utlis.PlaylistUtils
import com.example.cassette.views.dialogs.CreatePlaylistDialog
import kotlinx.android.synthetic.main.fragment_playlist.*


class Playlist : Fragment() {
    override fun onResume() {
        super.onResume()
        button2.setOnClickListener() {
            Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show()
        }

        fab.setOnClickListener {

            val createPlaylist = CreatePlaylistDialog()

            this.fragmentManager?.beginTransaction()?.let { it -> createPlaylist.show(it, "playlist") }

            context?.let { it -> PlaylistUtils.createPlaylist(it, "me2") }

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_playlist, container, false)

        return view
    }
}