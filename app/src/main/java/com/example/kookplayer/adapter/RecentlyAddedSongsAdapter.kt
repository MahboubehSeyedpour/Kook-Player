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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kookplayer.R
import com.example.kookplayer.db.entities.SongModel
import com.example.kookplayer.helper.Coordinator
import com.example.kookplayer.repositories.RoomRepository
import com.example.kookplayer.utlis.ImageUtils
import com.example.kookplayer.utlis.SongUtils
import com.example.kookplayer.views.Fragments.LibraryFragment
import com.example.kookplayer.views.activities.MainActivity
import com.example.kookplayer.views.dialogs.SongDetailsDialog
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.recently_added_rv_item.view.*
import kotlinx.android.synthetic.main.song_rv_item.view.music_iv
import kotlinx.android.synthetic.main.song_rv_item.view.song_artist
import kotlinx.android.synthetic.main.song_rv_item.view.song_title

class RecentlyAddedSongsAdapter (
        val context: Activity,
        var dataset: ArrayList<SongModel>
    ) : RVBaseAdapter() {

        var position = 0

        lateinit var dataSend: OnDataSend

        lateinit var viewHolder: RecyclerViewViewHolder

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerView.ViewHolder {
            val rootView: View =
                LayoutInflater.from(context).inflate(R.layout.recently_added_rv_item, parent, false)
            return RecyclerViewViewHolder(rootView)
        }

        @RequiresApi(Build.VERSION_CODES.R)
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val song: SongModel = dataset[position]
            this.position = position
            viewHolder = holder as RecyclerViewViewHolder
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
                Coordinator.SourceOfSelectedSong = "library"
                Coordinator.currentDataSource = dataset


                MainActivity.activity.updateVisibility()
                Coordinator.playSelectedSong(dataset[position])

            }


        }

        @RequiresApi(Build.VERSION_CODES.R)
        fun handleMenuButtonClickListener(itemId: Int, position: Int): Boolean {
            when (itemId) {
//            R.id.playNext_menu_item -> {
//                PlayerRemote.playSongAsNextMusic(position)
//            }
                R.id.addToPlayList_menu_item -> {

                    if(position>=0)
                    {
                        dataSend.onSend(context, getSong(position))
                    }

                    else{
                        Toast.makeText(context, "please try again", Toast.LENGTH_SHORT).show()
                    }

                }
                R.id.deleteFromDevice_menu_item -> {
//                com.example.kookplayer.utlis.SongUtils.context = LibraryFragment.mactivity.baseContext


                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                    {
                        val songid = dataset[position].id

                        for (playlistId in songid?.let {
                            RoomRepository.listOfPlaylistsContainSpecificSong(
                                it
                            )
                        }!!)
                        {
                            RoomRepository.removeSongFromPlaylist(playlistId, songid.toString())
                        }

                        RoomRepository.removeSongFromFavorites(dataset[position])

                        getSongUri(position)?.let { SongUtils.deleteMusic(LibraryFragment.mactivity.baseContext, LibraryFragment.mactivity, it) }

                    }
                    else{
                        getSongUri(position)?.let { SongUtils.del(getSong(position).id.toString(), it) }
                    }

                }
                R.id.details_menu_item -> {


                    val songDetailsDialog = SongDetailsDialog(getSong(position))

                    val manager: FragmentManager =
                        (context as AppCompatActivity).supportFragmentManager

                    manager?.beginTransaction()
                        ?.let { it -> songDetailsDialog.show(it, "songDetails") }


                }
                R.id.share_menu_item -> {
                    SongUtils.shareMusic(context, getSong(position))
                }
//            R.id.setAsRingtone_menu_item -> {
//            }
                else -> return false
            }
            return true
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
            val title: TextView = itemView.song_title
            val artist: TextView = itemView.song_artist
            val imageView: ImageView = itemView.music_iv
            val recyclerItem: MaterialCardView = itemView.song_container_
        }

        interface OnDataSend {
            fun onSend(context: Activity, songModel: SongModel)
        }

        fun OnDataSend(dataSend: OnDataSend) {
            this.dataSend = dataSend
        }

        fun getView(): View {
            return viewHolder.imageView
        }
    }