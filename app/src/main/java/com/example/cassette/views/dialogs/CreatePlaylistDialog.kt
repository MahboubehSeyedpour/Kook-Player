package com.example.cassette.views.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.cassette.R
import kotlinx.android.synthetic.main.create_playlist_dialog.*


class CreatePlaylistDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner_bg);
        return inflater.inflate(R.layout.create_playlist_dialog, container, false)

    }

//    @SuppressLint("ResourceType")
//    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val alertDialog = activity?.let {
//            // Use the Builder class for convenient dialog construction
//            val builder = AlertDialog.Builder(it)
//
//                .setView(R.layout.create_playlist_dialog)
//            // Create the AlertDialog object and return it
//            builder.create()
//        } ?: throw IllegalStateException("Activity cannot be null")
//
//        return alertDialog
//    }

    override fun onResume() {
        super.onResume()

        btnPositive.setOnClickListener {
            Toast.makeText(context, "ok pressed", Toast.LENGTH_SHORT).show()
            this.dismiss()
        }
//
//        btnNegative.setOnClickListener {
//            Toast.makeText(context, "cancel pressed", Toast.LENGTH_SHORT).show()
//            this.dismiss()
//        }
    }
}