package com.example.cassette.views.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.cassette.R
import com.example.cassette.databinding.CreatePlaylistDialogBinding
import com.example.cassette.myInterface.PassData
import kotlinx.android.synthetic.main.create_playlist_dialog.*
import kotlinx.android.synthetic.main.create_playlist_dialog.view.*


class CreatePlaylistDialog : DialogFragment() {

    lateinit var binding: CreatePlaylistDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner_bg);
        val view = inflater.inflate(R.layout.create_playlist_dialog, container, false)
        binding = CreatePlaylistDialogBinding.bind(view)
        return view

    }

    override fun onResume() {
        super.onResume()


        btnPositive.setOnClickListener {

            val targetFragment = targetFragment
            val passData: PassData = targetFragment as PassData
            targetFragment.passDataToInvokingFragment(binding.textField.playlist_name.text.toString())

            this.dismiss()

        }
    }
}