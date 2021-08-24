package com.example.cassette.views.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.cassette.R
import com.example.cassette.databinding.AddSongToPlaylistBinding
import com.example.cassette.models.SongModel

class AddSongToPlaylistDialog(val song: SongModel) : DialogFragment() {

    lateinit var binding: AddSongToPlaylistBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner_bg);

        val view = inflater.inflate(R.layout.add_song_to_playlist, container, false)
        initBinding(view)

        return view

    }

    override fun onResume() {
        super.onResume()

        binding.acceptSelectedPlaylistBtn.setOnClickListener {
            this.dismiss()
        }

    }

    fun initBinding(view: View) {
        binding = AddSongToPlaylistBinding.bind(view)
    }
}



