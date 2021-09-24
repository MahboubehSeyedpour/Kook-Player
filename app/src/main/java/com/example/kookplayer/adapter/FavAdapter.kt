package com.example.kookplayer.adapter

import android.app.Activity
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.kookplayer.R
import com.example.kookplayer.helper.Coordinator
import com.example.kookplayer.db.entities.SongModel
import com.example.kookplayer.db.MyDatabaseUtils
import com.example.kookplayer.utlis.ImageUtils
import com.example.kookplayer.views.activities.MainActivity
import kotlinx.android.synthetic.main.fav_rv_item.view.*

class FavAdapter(
    val context: Activity,
    var dataset: ArrayList<SongModel>
) : RVBaseAdapter() {

    var position = 0

    lateinit var dataSend: OnDataSend


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val rootView: View =
            LayoutInflater.from(context).inflate(R.layout.fav_rv_item, parent, false)
        return RecyclerViewViewHolder(rootView)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val song: SongModel = dataset[position]
        this.position = position
        val viewHolder = holder as RecyclerViewViewHolder
        viewHolder.title.text = song.title
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
            Coordinator.SourceOfSelectedSong = "fav"
            Coordinator.currentDataSource = dataset


            MainActivity.activity.updateVisibility()
            Coordinator.playSelectedSong(dataset[position])

        }


        viewHolder.likeButton.setOnClickListener { it ->
            viewHolder.likeButton.setImageResource(R.drawable.ic_unfavored)
            MyDatabaseUtils.deleteSongFromFav(dataset[position])
        }
    }

    fun updatePosition(newIndex: Int) {
        position = newIndex
    }

    override fun getCurrentPosition(): Int {
        return position
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun getSongUri(position: Int): Uri? {
        return dataset[position].uri
    }

    fun getSong(position: Int): SongModel {
        if (position < 0) {

            Toast.makeText(context, "please try again!", Toast.LENGTH_SHORT).show()
            return SongModel()
        } else {
            return dataset[position]
        }
    }

    open inner class RecyclerViewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.fav_song_title
        val artist: TextView = itemView.fav_song_artist
        val likeButton: ImageView = itemView.like_btn
        val imageView: ImageView = itemView.fav_music_iv
        val recyclerItem: ConstraintLayout = itemView.fav_song_container
    }

    interface OnDataSend {
        fun onSend(context: Activity, songModel: SongModel)
    }

    fun OnDataSend(dataSend: OnDataSend) {
        this.dataSend = dataSend
    }
}