package com.example.cassette.views.dialogs

import android.app.AlertDialog.Builder
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.cassette.R


class CreatePlaylistDialog : DialogFragment() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = Builder(it)

                .setView(R.layout.create_playlist_dialog)
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }

//    override fun onResume() {
//        super.onResume()
//
//        btnPositive.setOnClickListener {
//            Toast.makeText(context, "ok pressed", Toast.LENGTH_SHORT).show()
//            this.dismiss()
//        }
//
//        btnNegative.setOnClickListener {
//            Toast.makeText(context, "cancel pressed", Toast.LENGTH_SHORT).show()
//            this.dismiss()
//        }
//    }
}