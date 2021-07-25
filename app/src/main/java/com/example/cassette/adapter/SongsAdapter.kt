package com.example.cassette.adapter

import SongUtils
import android.app.Activity
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.cassette.R
import com.example.cassette.models.SongModel
import com.example.cassette.utlis.ImageUtils
import com.example.cassette.views.PlayerRemote
import kotlinx.android.synthetic.main.song_rv_item.view.*

class SongsAdapter(
    context: Activity,
    arrayList: ArrayList<SongModel>
) : RVBaseAdapter() {

    var context = context
    var dataset = arrayList
    var position = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val rootView: View =
            LayoutInflater.from(context).inflate(R.layout.song_rv_item, parent, false)
        return RecyclerViewViewHolder(rootView)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val song: SongModel = dataset[position]
        this.position = position
        val viewHolder = holder as RecyclerViewViewHolder
        viewHolder.title.text = song.title
        viewHolder.duration.text = song.duration
        viewHolder.artist.text = song.artist
        ImageUtils.loadImageToImageView(
            context = context,
            imageView = viewHolder.imageView,
            image = song.image
        )


        viewHolder.recyclerItem.setOnClickListener {
            PlayerRemote.player.playMusic(content = song.data)
            updatePosition(newIndex = viewHolder.adapterPosition)
        }


        viewHolder.menuBtn.setOnClickListener {
            val popUpMenu = PopupMenu(context, it)
            popUpMenu.inflate(R.menu.popup_menu)

            popUpMenu.setOnMenuItemClickListener {
//                updatePosition(viewHolder.adapterPosition)
                return@setOnMenuItemClickListener handleMenueButtonClickListener(it.itemId)
            }
            popUpMenu.show()
        }

    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun handleMenueButtonClickListener(itemId: Int): Boolean {
        when (itemId) {
            R.id.playNext_menu_item -> {
                PlayerRemote.playSongAsNextMusic(position)
            }
            R.id.addToPlayList_menu_item -> {
                PlayerRemote.addToPlaylist(position)
            }
            R.id.deleteFromDevice_menu_item -> {
                getSongUri(position)?.let { SongUtils.deletMusic(context, it) }
            }
            R.id.details_menu_item -> {
                SongUtils.showDetails(position)
            }
            R.id.share_menu_item -> {
                SongUtils.shareMusic(getSong(position))
            }
            R.id.setAsRingtone_menu_item -> {
                Toast.makeText(
                    context,
                    "add to set as ringtone in item number {$position}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> return false
        }
        return true
    }

    fun updatePosition(newIndex: Int) {
        position = newIndex
    }

    fun getCurrentPosition(): Int {
        return position
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun getCurrentSongTitle(): String {
        return dataset[position].title
    }

    fun getSongDuration(position: Int): String {
        return dataset[position].duration
    }


    fun getSongUri(position: Int): Uri? {
        return dataset[position].uri
    }

    fun getSongData(position: Int): String {
        return dataset[position].data
    }

    fun getSong(position: Int): SongModel {
        return dataset[position]
    }

    fun update() {
        dataset = SongUtils.getListOfSongs(context)
        notifyDataSetChanged()
    }

    open inner class RecyclerViewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.song_title
        val artist: TextView = itemView.song_artist
        val duration : TextView = itemView.song_duration
        val menuBtn: ImageView = itemView.music_menu_btn
        val imageView: ImageView = itemView.music_iv
        val recyclerItem: LinearLayout = itemView.sont_container
    }
}