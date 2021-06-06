package com.example.cassette.views.Fragments

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cassette.R
import kotlinx.android.synthetic.main.fragment_playlist.*


class Playlist : Fragment() {
    override fun onResume() {
        super.onResume()
        button2.setOnClickListener(){



            Toast.makeText(context,"clecked" , Toast.LENGTH_SHORT).show()
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