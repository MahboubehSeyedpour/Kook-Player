package com.example.cassette.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.cassette.R
import com.example.cassette.models.PlaylistModel
import kotlinx.android.synthetic.main.playlist_item.view.*

class PlaylistAdapter(
    var context: Activity,
    arrayList: ArrayList<PlaylistModel>
) : RVBaseAdapter() {

    var dataset : ArrayList<PlaylistModel>

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
        val viewHolder =
            holder as PlaylistAdapter.RecyclerViewViewHolder
        viewHolder.name.text = playlist.name
        viewHolder.countOfSongs.text = playlist.countOfSongs.toString()
        viewHolder.playlistItem.setOnClickListener {

            Toast.makeText(context, "playlist ${playlist.name} was clicked", Toast.LENGTH_SHORT).show()
        }

//        viewHolder.menueBtn.setOnClickListener {
//            PlaylistUtils.deletePlaylist(context, viewHolder.)
//            dataset.remove(dataset[position])
//            Playlist.viewModel?.liveData?.value = dataset as ArrayList<Any>
//        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    open inner class RecyclerViewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.playlist_name
        val countOfSongs: TextView = itemView.songsCount
        val playlistItem: CardView = itemView.playlist_item
        val menueBtn : ImageView = itemView.playlist_menu_btn
    }

    init {
        dataset = arrayList
    }

}