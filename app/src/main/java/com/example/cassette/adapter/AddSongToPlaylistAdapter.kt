package com.example.cassette.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cassette.R
import com.example.cassette.repositories.appdatabase.entities.PlaylistModel
import com.google.android.material.checkbox.MaterialCheckBox
import kotlinx.android.synthetic.main.add_song_to_playlist_dialog_item.view.*

class AddSongToPlaylistAdapter(
    var context: Activity,
    arrayList: ArrayList<PlaylistModel>
) : RVBaseAdapter() {

    var dataset: ArrayList<PlaylistModel>

    companion object {
        var choices: ArrayList<PlaylistModel> = arrayListOf()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val rootView: View =
            LayoutInflater.from(context)
                .inflate(R.layout.add_song_to_playlist_dialog_item, parent, false)
        return RecyclerViewViewHolder(rootView)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val playlist: PlaylistModel = dataset[position]
        val viewHolder =
            holder as AddSongToPlaylistAdapter.RecyclerViewViewHolder
        viewHolder.name.text = playlist.name
        viewHolder.checkkox.setOnClickListener {

            if (viewHolder.checkkox.isChecked) {
                Toast.makeText(context, "playlist ${playlist.name} selected", Toast.LENGTH_SHORT)
                    .show()
                choices?.add(playlist)
            } else {
                Toast.makeText(context, "playlist ${playlist.name} unselected", Toast.LENGTH_SHORT)
                    .show()
                choices?.remove(playlist)
            }

        }

    }

    override fun getCurrentPosition(): Int {
//        Fake!
        return -1
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    open inner class RecyclerViewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.textView2
        val checkkox: MaterialCheckBox = itemView.materialCheckBox
    }

    init {
        dataset = arrayList
    }
}