package com.example.cassette.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.cassette.R
import com.example.cassette.models.PlaylistModel
import kotlinx.android.synthetic.main.playlist_item.view.*

class PlaylistAdapter(
    context: Activity,
    arrayList: ArrayList<PlaylistModel>
) : RV_Base_Adapter() {

    var context = context
    var arrayList = arrayList
    var position = 0

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

        val playlist: PlaylistModel = arrayList[position]
        this.position = position
        val viewHolder = holder as PlaylistAdapter.RecyclerViewViewHolder
        viewHolder.name.text = playlist.name
        viewHolder.countOfSongs.text = playlist.countOfSongs.toString()
        viewHolder.playlistItem.setOnClickListener {

            Toast.makeText(context, "playlist ${playlist.name} was clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    open inner class RecyclerViewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.playlist_name
        val countOfSongs: TextView = itemView.songsCount
        val playlistItem: CardView = itemView.playlist_item
    }

}