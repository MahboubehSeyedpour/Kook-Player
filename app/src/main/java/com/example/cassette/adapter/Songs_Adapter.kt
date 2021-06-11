package com.example.cassette.adapter

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.cassette.R
import com.example.cassette.models.Song_Model
//import com.example.cassette.utlis.MusicUtils
import com.example.cassette.views.MainActivity
import com.example.cassette.views.PlayerRemote
import kotlinx.android.synthetic.main.song_rv_item.view.*

class Songs_Adapter(
    context: Activity,
    arrayList: ArrayList<Song_Model>
) :
    RV_Base_Adapter() {

    var context = context
    var arrayList = arrayList
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
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val song: Song_Model = arrayList.get(position)
        this.position = position
        val viewHolder = holder as RecyclerViewViewHolder
        viewHolder.title.text = song.title
        viewHolder.duration.text = song.duration
        viewHolder.artist.text = song.artist


        viewHolder.title.setOnClickListener {
            PlayerRemote.playMusic(song.data)
            updatePosition(viewHolder.adapterPosition)
        }
        viewHolder.duration.setOnClickListener {
            PlayerRemote.playMusic(song.data)
            updatePosition(viewHolder.adapterPosition)
        }
        viewHolder.artist.setOnClickListener {
            PlayerRemote.playMusic(song.data)
            updatePosition(viewHolder.adapterPosition)
        }

        viewHolder.menu_btn.setOnClickListener {
            val popUpMenu = PopupMenu(context, it)
            popUpMenu.inflate(R.menu.popup_menu)

            popUpMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.playNext_menu_item -> {
                        Toast.makeText(
                            context,
                            "play next in item number {$position}",
                            Toast.LENGTH_SHORT
                        ).show()
                        updatePosition(viewHolder.adapterPosition)
                        true
                    }
                    R.id.addToPlayList_menu_item -> {
                        updatePosition(viewHolder.adapterPosition)
                        Toast.makeText(
                            context,
                            "add to playlist in item number {$position}",
                            Toast.LENGTH_SHORT
                        ).show()
                        true
                    }
                    R.id.deleteFromDevice_menu_item -> {
                        updatePosition(viewHolder.adapterPosition)
                        val title: Array<String> =
                            (viewHolder.itemView.song_title.text).map { it.toString() }
                                .toTypedArray()
                        MusicUtils.removeMusic(position, title)
                        Toast.makeText(
                            context,
                            "add to delete from device in item number {$position}",
                            Toast.LENGTH_SHORT
                        ).show()
                        true
                    }
                    R.id.details_menu_item -> {
                        updatePosition(viewHolder.adapterPosition)
                        Toast.makeText(
                            context,
                            "add to details in item number {$position}",
                            Toast.LENGTH_SHORT
                        ).show()
                        true
                    }
                    R.id.share_menu_item -> {
                        updatePosition(viewHolder.adapterPosition)
                        Toast.makeText(
                            context,
                            "add to share in item number {$position}",
                            Toast.LENGTH_SHORT
                        ).show()
                        true
                    }
                    R.id.setAsRingtone_menu_item -> {
                        updatePosition(viewHolder.adapterPosition)
                        Toast.makeText(
                            context,
                            "add to set as ringtone in item number {$position}",
                            Toast.LENGTH_SHORT
                        ).show()
                        true
                    }
                    else -> false
                }
            }

            popUpMenu.show()

//            Toast.makeText(context , "item number {$position} was clicked" , Toast.LENGTH_SHORT).show()
        }

    }

    fun updatePosition(newIndex: Int) {
        position = newIndex
    }

    fun getCurrentPosition(): Int {
        return position
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    open inner class RecyclerViewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val title = itemView.song_title
        val duration = itemView.song_duration
        val artist = itemView.song_artist
        val menu_btn = itemView.music_menu_btn

    }
}