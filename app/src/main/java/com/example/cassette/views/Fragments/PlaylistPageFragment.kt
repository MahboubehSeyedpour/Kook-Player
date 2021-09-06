package com.example.cassette.views.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cassette.R


class PlaylistPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_playlist_page, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



    }


    override fun onResume() {
        super.onResume()

    }
}