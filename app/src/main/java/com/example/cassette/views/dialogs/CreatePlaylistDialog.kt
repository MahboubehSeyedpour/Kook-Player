package com.example.cassette.views.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.cassette.R
import com.example.cassette.databinding.CreatePlaylistDialogBinding
import com.example.cassette.myInterface.PassData
import com.example.cassette.views.Fragments.PlaylistFragment
import com.example.cassette.views.Fragments.PlaylistPageFragment
import com.example.cassette.views.MainActivity
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

            if(binding.textField.playlist_name.text.toString().isEmpty())
            {
                val shake: Animation = AnimationUtils.loadAnimation(this@CreatePlaylistDialog.context, R.anim.shake)
                binding.addPlaylistLayout.startAnimation(shake)
                Toast.makeText(
                    context,
                    "Please enter a name",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if(isUnique(binding.textField.playlist_name.text.toString()))
            {
                val shake: Animation = AnimationUtils.loadAnimation(this@CreatePlaylistDialog.context, R.anim.shake)
                binding.addPlaylistLayout.startAnimation(shake)
                Toast.makeText(
                    context,
                    "Duplicate name",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else
            {
                val targetFragment = targetFragment
                val passData: PassData = targetFragment as PassData
                targetFragment.passDataToInvokingFragment(binding.textField.playlist_name.text.toString())

                this.dismiss()
            }
        }

        btnNegative.setOnClickListener {
            this.dismiss()
        }
    }

    private fun isUnique(name: String): Boolean
    {
        for (playlist in PlaylistFragment.viewModel?.playlistRepository?.cashedPlaylistArray!!)
        {
            if(playlist.name == name)
                return true
        }
        return false
    }
}