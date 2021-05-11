package com.example.cassette.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cassette.models.Song
import kotlinx.android.synthetic.main.song_rv_item.view.*

class Songs(context: Activity, arrayList: ArrayList<Song>, rv_item: Int) :
    RecyclerViewAdapter(context, arrayList, rv_item) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val song: Song = arrayList.get(position)
        val viewHolder = holder as RecyclerViewViewHolder
        viewHolder.title.setText(song.title)
        viewHolder.duration.setText((song.duration).toString())
    }

    inner class RecyclerViewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val title = itemView.song_title
        val duration = itemView.song_duration
    }
}