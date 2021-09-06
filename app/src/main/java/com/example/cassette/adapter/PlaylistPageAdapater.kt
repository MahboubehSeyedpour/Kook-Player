package com.example.cassette.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.cassette.R
import com.example.cassette.manager.Coordinator
import com.example.cassette.repositories.appdatabase.entities.SongModel
import com.example.cassette.utlis.ImageUtils
import com.example.cassette.utlis.TimeUtils
import kotlinx.android.synthetic.main.song_rv_item.view.*

class PlaylistPageAdapater(var dataset: ArrayList<SongModel>,val context: Context): RVBaseAdapter() {

    var position = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rootView: View =
            LayoutInflater.from(context).inflate(R.layout.song_rv_item, parent, false)
        return RecyclerViewViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val song: SongModel = dataset[position]

        this.position = position
        val viewHolder = holder as PlaylistPageAdapater.RecyclerViewViewHolder
        viewHolder.title.text = song.title
        viewHolder.duration.text = song.duration?.let { TimeUtils.milliSecToDuration(it) }
        viewHolder.artist.text = song.artist
        song.image?.let {
            ImageUtils.loadImageToImageView(
                context = context,
                imageView = viewHolder.imageView,
                image = it
            )
        }


        viewHolder.recyclerItem.setOnClickListener {
            updatePosition(newIndex = viewHolder.adapterPosition)
            Coordinator.playSelectedSong(dataset[position])
        }

    }

    fun updatePosition(newIndex: Int) {
        position = newIndex
    }


    override fun getCurrentPosition(): Int {
        return position
    }

    fun getSongUri(position: Int): Uri? {
        return dataset[position].uri
    }

    fun getSong(position: Int): SongModel {
        if (position < 0) {

            Toast.makeText(context, "please try later!", Toast.LENGTH_SHORT).show()
            return SongModel()
        } else {
            return dataset[position]
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    open inner class RecyclerViewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.song_title
        val artist: TextView = itemView.song_artist
        val duration: TextView = itemView.song_duration
        val menuBtn: ImageView = itemView.music_menu_btn
        val imageView: ImageView = itemView.music_iv
        val recyclerItem: ConstraintLayout = itemView.song_container
    }
}