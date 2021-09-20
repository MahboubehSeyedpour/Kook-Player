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
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cassette.R
import com.example.cassette.manager.Coordinator
import com.example.cassette.repositories.appdatabase.entities.SongModel
import com.example.cassette.repositories.appdatabase.roomdb.MyDatabaseUtils
import com.example.cassette.utlis.ImageUtils
import com.example.cassette.utlis.TimeUtils
import com.example.cassette.views.Fragments.LibraryFragment
import com.example.cassette.views.MainActivity
import com.example.cassette.views.dialogs.SongDetailsDialog
import kotlinx.android.synthetic.main.song_rv_item.view.*


class SongsAdapter(
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
            LayoutInflater.from(context).inflate(R.layout.song_rv_item, parent, false)
        return RecyclerViewViewHolder(rootView)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val song: SongModel = dataset[position]
        this.position = position
        viewHolder = holder as RecyclerViewViewHolder
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
            Coordinator.SourceOfSelectedSong = "library"
            Coordinator.currentDataSource = dataset


            MainActivity.activity.updateVisibility()
            Coordinator.playSelectedSong(dataset[position])

        }


        viewHolder.menuBtn.setOnClickListener { it ->
            val popUpMenu = PopupMenu(context, it)
            popUpMenu.inflate(R.menu.songs_popup_menu)

            popUpMenu.setOnMenuItemClickListener {
//                updatePosition(position)

                return@setOnMenuItemClickListener handleMenuButtonClickListener(
                    it.itemId,
                    position
                )
            }
            popUpMenu.show()
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
                SongUtils.context = LibraryFragment.mactivity.baseContext


                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                {
                    val songid = dataset[position].id

                    for (playlistId in songid?.let {
                        MyDatabaseUtils.listOfPlaylistsContainSpecificSong(
                            it
                        )
                    }!!)
                    {
                        MyDatabaseUtils.removeSongFromPlaylist(playlistId, songid.toString())
                    }

                    getSongUri(position)?.let { SongUtils.deletMusic(LibraryFragment.mactivity, it) }

                }
                else{
//                    val urisToModify = getSongUri(position)?.let {
//                        MediaStore.getDocumentUri(context,
//                            it
//                        )
//                    }
//                    urisToModify?.let { SongUtils.deletMusic(LibraryFragment.mactivity, it) }
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
        val duration: TextView = itemView.song_duration
        val menuBtn: ImageView = itemView.music_menu_btn
        val imageView: ImageView = itemView.music_iv
        val recyclerItem: ConstraintLayout = itemView.song_container
    }

    interface OnDataSend {
        fun onSend(context: Activity, songModel: SongModel)
    }

    fun OnDataSend(dataSend: OnDataSend) {
        this.dataSend = dataSend
    }

    fun getView(): View{
        return viewHolder.imageView
    }
}