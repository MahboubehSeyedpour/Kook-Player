package com.example.cassette.views.Fragments

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cassette.R
import com.example.cassette.adapter.SongsAdapter
import com.example.cassette.manager.Coordinator
import com.example.cassette.models.SongModel
import com.example.cassette.viewModel.PlaylistViewModel
import com.example.cassette.viewModel.SongsViewModel
import com.example.cassette.views.dialogs.AddSongToPlaylistDialog
import kotlinx.android.synthetic.main.fragment_library.*

class Library : Fragment() {

    companion object Library {

        var songsAdapter: SongsAdapter? = null

        lateinit var viewModel: SongsViewModel

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

        Coordinator.initNowPlayingQueue()

        songs_rv.layoutManager = LinearLayoutManager(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_library, container, false)

//        TODO(check if the manifest permissions had been granted)
//        TODO(take musics in Internal & External storage)


        viewModel = ViewModelProvider(this).get(SongsViewModel::class.java)
        context?.let { viewModel.setFragmentContext(it) }
        viewModel.dataset.observe(viewLifecycleOwner, songListUpdateObserver)

        songsAdapter = activity?.let {
            SongsAdapter(
                it,
                viewModel.dataset.value as ArrayList<SongModel>
            )
        }

        songsAdapter?.OnDataSend(
            object : SongsAdapter.OnDataSend {
                override fun onSend(context: Activity, songModel: SongModel) {

                    createDialogToSelectPlaylist()

//                    PlaylistUtils.addToPlaylist(
//                        context,
//                        PlaylistUtils.playlists[0].id,
//                        arrayListOf(songModel)
//                    )


//                    val playlistRepository = PlaylistRepository(context)
//                    val n = playlistRepository.getPlaylistIdByName("car")
//                    val y = playlistRepository.getPlaylists()
//                    PlaylistUtils.addToPlaylist(context, n.toLong(), arrayListOf(songModel))
//                    val ii = PlaylistUtils.getPlaylistSize(context, n.toLong())
//                    val bc = PlaylistUtils.getMusicsRelatedToSpecificPlaylist(context, n.toLong())
                    val b = 0
                }
            }
        )

        notifyDataSetChanges()

        return view
    }


    private val songListUpdateObserver = Observer<ArrayList<Any>> { dataset ->
        songsAdapter?.dataset = dataset as ArrayList<SongModel>
        songs_rv.adapter = songsAdapter
    }

    fun createDialogToSelectPlaylist()
    {
//        Playlist.viewModel?.updateDataset()
        val vm = PlaylistViewModel()
        context?.let { vm.setFragmentContext(it) }
        vm.updateDataset()
        val addSongToPlaylistDialog = AddSongToPlaylistDialog(vm.getDataSet())

        addSongToPlaylistDialog?.setTargetFragment(this, 0)
        this.fragmentManager?.let { it1 -> addSongToPlaylistDialog?.show(it1, "pl") }

    }


}