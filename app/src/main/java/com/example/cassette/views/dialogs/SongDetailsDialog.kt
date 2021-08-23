package com.example.cassette.views.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.cassette.R
import com.example.cassette.databinding.SongDetailsDialogBinding
import com.example.cassette.models.SongModel
import com.example.cassette.utlis.TimeUtils

class SongDetailsDialog(val song: SongModel) : DialogFragment() {

    lateinit var binding: SongDetailsDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner_bg);

        val view = inflater.inflate(R.layout.song_details_dialog, container, false)
        initBinding(view)

        binding.detailsFileSizeContent.text = (((song.size)?.toFloat()?.div(1024))?.div(1024)).toInt().toString() + " MB"
        binding.detailsFileNameContent.text = song.title
        binding.detailsFileBitrateContent.text = ((song.bitrate)?.toInt()?.div(1000)).toString() + " kb/s"
        binding.detailsFileLengthContent.text = song.duration?.let { TimeUtils.milliSecToDuration(it) }
        binding.detailsFilePathContent.text = song.data


        return view

    }

    override fun onResume() {
        super.onResume()

        binding.btnOk.setOnClickListener {
            this.dismiss()
        }




    }

    fun initBinding(view: View) {
        binding = SongDetailsDialogBinding.bind(view)
    }
}

