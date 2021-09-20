package com.example.cassette.views.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import com.example.cassette.R
import com.example.cassette.databinding.CreatePlaylistDialogBinding
import com.example.cassette.myInterface.PassData
import com.example.cassette.repositories.appdatabase.roomdb.MyDatabaseUtils
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

            if(binding.textField.playlist_name.text.toString().trim().isEmpty())
            {
                val shake: Animation = AnimationUtils.loadAnimation(this@CreatePlaylistDialog.context, R.anim.shake)
                binding.addPlaylistLayout.startAnimation(shake)
                binding.textField.error = "Please enter a name"
            }
            else if(isUnique(binding.textField.playlist_name.text.toString()))
            {
                val shake: Animation = AnimationUtils.loadAnimation(this@CreatePlaylistDialog.context, R.anim.shake)
                binding.addPlaylistLayout.startAnimation(shake)
                binding.textField.error = "Duplicate name"
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
        for (playlist in MyDatabaseUtils.cashedPlaylistArray!!)
        {
            if(playlist.name == name)
                return true
        }
        return false
    }
}