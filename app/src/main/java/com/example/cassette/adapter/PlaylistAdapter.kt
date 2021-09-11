package com.example.cassette.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.cassette.R
import com.example.cassette.repositories.PlaylistRepository
import com.example.cassette.repositories.appdatabase.entities.PlaylistModel
import com.example.cassette.views.Fragments.PlaylistFragment
import kotlinx.android.synthetic.main.playlist_item.view.*

class PlaylistAdapter(
    var context: Activity,
    arrayList: ArrayList<PlaylistModel>
) : RVBaseAdapter() {

    var dataset: ArrayList<PlaylistModel>
    lateinit var viewHolder: PlaylistAdapter.RecyclerViewViewHolder
    lateinit var dataSend: PlaylistAdapter.OnDataSend

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val rootView: View =
            LayoutInflater.from(context).inflate(R.layout.playlist_item, parent, false)
        return RecyclerViewViewHolder(rootView)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val playlist: PlaylistModel = dataset[position]
        viewHolder =
            holder as PlaylistAdapter.RecyclerViewViewHolder
        viewHolder.name.text = playlist.name
        val playlistRepository = PlaylistRepository(context)
        val playlistId = playlistRepository.getIdByName(playlist.name)

        viewHolder.countOfSongs.text = playlist.countOfSongs.toString()
//            PlaylistUtils.getMusicsRelatedToSpecificPlaylist(context, playlistId.toLong()).size.toString()
        viewHolder.playlistItem.setOnClickListener {

//            open playlist
            dataSend.openPlaylist(playlist.id)

        }

        viewHolder.menueBtn.setOnClickListener {
            val popUpMenu = PopupMenu(context, it)
            popUpMenu.inflate(R.menu.playlists_popup_menu)

            popUpMenu.setOnMenuItemClickListener {
                val id = playlistRepository.getPlaylists()[position].id
//                updatePosition(viewHolder.adapterPosition)
                return@setOnMenuItemClickListener handleMenuButtonClickListener(
                    it.itemId,
                    id
                )
            }
            popUpMenu.show()
        }


//        viewHolder.menueBtn.setOnClickListener {
//            PlaylistUtils.deletePlaylist(context, viewHolder.)
//            dataset.remove(dataset[position])
//            Playlist.viewModel?.liveData?.value = dataset as ArrayList<Any>
//        }

//        countOfSongsRelatedToPlaylistss()
    }

    override fun getCurrentPosition(): Int {
//        Fake!
        return -1
    }


    private fun handleMenuButtonClickListener(itemId: Int, playlistId: Long): Boolean {
        when (itemId) {
            R.id.deletePlaylist_menu_item -> {
                PlaylistFragment.viewModel?.playlistRepository?.removePlaylist(playlistId)
                dataSend.onSend(context, playlistId)
            }
            R.id.renamePlaylist_menu_item -> {
//                TODO
            }
            else -> return false
        }
        return true
    }

    override fun getItemCount(): Int {
        return dataset.size
    }


    interface OnDataSend {
        fun onSend(context: Activity, id: Long)
        fun openPlaylist(id: Long)
    }

    fun OnDataSend(dataSend: OnDataSend) {
        this.dataSend = dataSend
    }

    open inner class RecyclerViewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.playlist_name
        val countOfSongs: TextView = itemView.songsCount
        val playlistItem: CardView = itemView.playlist_item
        val menueBtn: ImageView = itemView.playlist_menu_btn
    }

    init {
        dataset = arrayList
    }

}