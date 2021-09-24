package com.example.kookplayer.views.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import com.example.kookplayer.R
import com.example.kookplayer.databinding.CreatePlaylistDialogBinding
import com.example.kookplayer.db.MyDatabaseUtils
import com.example.kookplayer.myInterface.PassData
import com.example.kookplayer.utlis.ScreenSizeUtils
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.addPlaylistLayout.layoutParams.width = ScreenSizeUtils.getScreenWidth() * 8 / 10
        binding.addPlaylistLayout.requestLayout()

        binding.addPlaylistLayout.textField.layoutParams.width =
            binding.addPlaylistLayout.layoutParams.width * 8 / 10
        binding.addPlaylistLayout.textField.requestLayout()

        binding.addPlaylistLayout.btnNegative.layoutParams.width =
            (binding.addPlaylistLayout.layoutParams.width * 3 / 10)
        binding.addPlaylistLayout.btnNegative.requestLayout()

        binding.addPlaylistLayout.btnPositive.layoutParams.width =
            binding.addPlaylistLayout.layoutParams.width * 5 / 10
        binding.addPlaylistLayout.btnNegative.requestLayout()

    }

    override fun onResume() {
        super.onResume()


        btnPositive.setOnClickListener {

            if (binding.textField.playlist_name.text.toString().trim().isEmpty()) {
                val shake: Animation =
                    AnimationUtils.loadAnimation(this@CreatePlaylistDialog.context, R.anim.shake)
                binding.addPlaylistLayout.startAnimation(shake)
                binding.textField.error = "Please enter a name"
            } else if (isUnique(binding.textField.playlist_name.text.toString())) {
                val shake: Animation =
                    AnimationUtils.loadAnimation(this@CreatePlaylistDialog.context, R.anim.shake)
                binding.addPlaylistLayout.startAnimation(shake)
                binding.textField.error = "Duplicate name"
            } else {
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

    private fun isUnique(name: String): Boolean {
        for (playlist in MyDatabaseUtils.cashedPlaylistArray!!) {
            if (playlist.name == name)
                return true
        }
        return false
    }
}