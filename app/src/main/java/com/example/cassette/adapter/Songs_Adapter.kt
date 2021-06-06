package com.example.cassette.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cassette.R
import com.example.cassette.models.Song_Model
import com.example.cassette.utlis.MusicUtils
import kotlinx.android.synthetic.main.song_rv_item.view.*

class Songs_Adapter(
    context: Activity,
    arrayList: ArrayList<Song_Model>
) :
    RV_Base_Adapter() {

    var context = context
    var arrayList = arrayList

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val rootView: View =
            LayoutInflater.from(context).inflate(R.layout.song_rv_item, parent, false)
        return RecyclerViewViewHolder(rootView)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val song: Song_Model = arrayList.get(position)
        val viewHolder = holder as RecyclerViewViewHolder
        viewHolder.title.text = song.title
        viewHolder.duration.text = song.duration
        viewHolder.singer.text = song.image
        viewHolder.itemView.setOnClickListener {
            MusicUtils.changePlayingMusic(song.data)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    open inner class RecyclerViewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val title = itemView.song_title
        val duration = itemView.song_duration
        val singer = itemView.song_singer
    }
}