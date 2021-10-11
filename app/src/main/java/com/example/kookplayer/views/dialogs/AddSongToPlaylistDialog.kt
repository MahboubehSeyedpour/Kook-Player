package com.example.kookplayer.views.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kookplayer.R
import com.example.kookplayer.adapter.AddSongToPlaylistAdapter
import com.example.kookplayer.databinding.DialogAddSongToPlaylistBinding
import com.example.kookplayer.db.entities.PlaylistModel
import com.example.kookplayer.myInterface.IPassDataForSelectPlaylists
import com.example.kookplayer.utlis.ScreenSizeUtils
import kotlinx.android.synthetic.main.dialog_add_song_to_playlist.view.*

class AddSongToPlaylistDialog(val array: ArrayList<PlaylistModel>) : DialogFragment() {

    lateinit var binding: DialogAddSongToPlaylistBinding
    var playlistAdapter: AddSongToPlaylistAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner_bg);

        val view = inflater.inflate(R.layout.dialog_add_song_to_playlist, container, false)
        initBinding(view)

        playlistAdapter = activity?.let {
            AddSongToPlaylistAdapter(
                it,
                array
            )
        }

        binding.playlists.layoutManager = LinearLayoutManager(context)
        binding.playlists.adapter = playlistAdapter

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.addSongToPlaylistLayout.layoutParams.width =
            ScreenSizeUtils.getScreenWidth() * 6 / 10
        binding.addSongToPlaylistLayout.layoutParams.height =
            ScreenSizeUtils.getScreenHeight() * 6 / 10
        binding.addSongToPlaylistLayout.requestLayout()

        binding.addSongToPlaylistLayout.playlists.layoutParams.width =
            binding.addSongToPlaylistLayout.layoutParams.width * 10 / 10
        binding.addSongToPlaylistLayout.playlists.layoutParams.height =
            (binding.addSongToPlaylistLayout.layoutParams.height* 6.5 / 10).toInt()
        binding.addSongToPlaylistLayout.playlists.requestLayout()

        binding.addSongToPlaylistLayout.acceptSelectedPlaylist_btn.layoutParams.width =
            binding.addSongToPlaylistLayout.layoutParams.width * 4 / 10
        binding.addSongToPlaylistLayout.acceptSelectedPlaylist_btn.layoutParams.height =
            (binding.addSongToPlaylistLayout.layoutParams.height* 1.2 / 10).toInt()
        binding.addSongToPlaylistLayout.acceptSelectedPlaylist_btn.requestLayout()

    }

    override fun onResume() {
        super.onResume()

        binding.acceptSelectedPlaylistBtn.setOnClickListener {

            val targetFragment = targetFragment
            val IPassData: IPassDataForSelectPlaylists = targetFragment as IPassDataForSelectPlaylists
            IPassData.passDataToInvokingFragment(AddSongToPlaylistAdapter.choices)

            this.dismiss()
        }
    }

    private fun initBinding(view: View) {
        binding = DialogAddSongToPlaylistBinding.bind(view)
    }

}



