package com.example.kookplayer.views.Fragments

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kookplayer.R
import com.example.kookplayer.adapter.SongsAdapter
import com.example.kookplayer.db.entities.PlaylistModel
import com.example.kookplayer.db.entities.SongModel
import com.example.kookplayer.helper.Coordinator
import com.example.kookplayer.myInterface.PassDataForSelectPlaylists
import com.example.kookplayer.repositories.RoomRepository
import com.example.kookplayer.viewModel.SongsViewModel
import com.example.kookplayer.views.dialogs.AddSongToPlaylistDialog
import kotlinx.android.synthetic.main.fragment_library.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LibraryFragment : Fragment(), PassDataForSelectPlaylists {

    companion object Library {

        var songsAdapter: SongsAdapter? = null

        lateinit var viewModel: SongsViewModel

        lateinit var mactivity: FragmentActivity

        lateinit var selectedSong: SongModel
        lateinit var selectedPlaylists: ArrayList<PlaylistModel>

        const val DELETE_REQUEST_CODE = 2

        fun notifyDataSetChanges() {
            viewModel.updateDataset()
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {
            2 -> if (resultCode == Activity.RESULT_OK) {
                notifyDataSetChanges()
            }
            else -> {
            }
        }
    }

    override fun onResume() {
        super.onResume()

        pullToRefresh.setOnRefreshListener {
            notifyDataSetChanges()
            pullToRefresh.isRefreshing = false
        }

        songs_rv.layoutManager = LinearLayoutManager(context)

        viewModel.updateDataset()

        mactivity = requireActivity()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_library, container, false)

//        TODO(check if the manifest permissions had been granted)
//        TODO(take musics in Internal & External storage)
//

        viewModel = ViewModelProvider(this).get(SongsViewModel::class.java)
            .apply {
                context?.let { setFragmentContext(it) }
            }.apply {
                dataset.observe(viewLifecycleOwner, songListUpdateObserver)
            }


        songsAdapter = activity?.let {
            SongsAdapter(
                it,
                viewModel.dataset.value as ArrayList<SongModel>
            )
        }

        songsAdapter?.OnDataSend(
            object : SongsAdapter.OnDataSend {
                override fun onSend(context: Activity, songModel: SongModel) {

                    selectedSong = songModel

                    if (RoomRepository.cachedPlaylistArray != null) {
                        if (RoomRepository.cachedPlaylistArray.size > 0) {
                            createDialogToSelectPlaylist()
                        } else {
                            val i = RoomRepository.cachedPlaylistArray
                            Toast.makeText(
                                requireActivity().baseContext,
                                getString(R.string.createPlaylist_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            requireActivity().baseContext,
                            getString(R.string.createPlaylist_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        )

        notifyDataSetChanges()

        RoomRepository.convertFavSongsToRealSongs()

        return view
    }


    private val songListUpdateObserver = Observer<ArrayList<Any>> { dataset ->
        songsAdapter?.dataset = dataset as ArrayList<SongModel>
        songs_rv.adapter = songsAdapter
    }

    fun createDialogToSelectPlaylist() {

        RoomRepository.updateCachedPlaylist()

        val addSongToPlaylistDialog = RoomRepository.cachedPlaylistArray?.let {
            AddSongToPlaylistDialog(
                it
            )
        }


        addSongToPlaylistDialog?.setTargetFragment(this, 0)
        this.fragmentManager?.let { it1 -> addSongToPlaylistDialog?.show(it1, "pl") }

    }

    override fun passDataToInvokingFragment(playlists: ArrayList<PlaylistModel>) {

        selectedPlaylists = playlists

        addSongToSelectedPlaylist()

        selectedPlaylists.clear()

    }

    private fun addSongToSelectedPlaylist() {

        for (playlist in selectedPlaylists) {
            addSongToPlaylist(playlist)
        }
    }

    fun addSongToPlaylist(playlist: PlaylistModel) {
        GlobalScope.launch {

            RoomRepository.addSongsToPlaylist(
                playlist.name,
                selectedSong.id.toString()
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Coordinator.updateNowPlayingQueue()

    }
}
