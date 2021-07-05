package com.example.cassette.utlis.services

import android.content.Context
import android.widget.Toast
import com.example.cassette.models.SongModel
import com.example.cassette.views.Fragments.Library

object SortUtils {

    fun sortByDateAdded() {
        Library.arraylist?.sortWith(compareByDescending { it.dateAdded })
        var sortedList = Library.arraylist
        sortMusicList(sortedList as ArrayList<SongModel>)
    }

    fun sortByName() {
        Library.arraylist?.sortWith(compareByDescending { it.title })
        var sortedList = Library.arraylist
        sortMusicList(sortedList as ArrayList<SongModel>)
    }

    fun sortByArtist() {
        Library.arraylist?.sortWith(compareByDescending { it.artist })
        var sortedList = Library.arraylist
        sortMusicList(sortedList as ArrayList<SongModel>)
    }

//    fun sortByGenre() {
//        Library.arraylist?.sortWith(compareByDescending { it.genre })
//        var sortedList = Library.arraylist
//        sortMusicList(sortedList as ArrayList<Song_Model>)
//    }

    fun sortByDuration() {
        Library.arraylist?.sortWith(compareByDescending { it.duration })
        var sortedList = Library.arraylist
        sortMusicList(sortedList as ArrayList<SongModel>)
    }

    fun sortMusicList(arrayList: ArrayList<SongModel>) {
        Library.songsAdapter?.arrayList = arrayList
        Library.songsAdapter?.notifyDataSetChanged()
    }

    fun changeSortingOrder(context: Context, item: String) {
        Toast.makeText(
            context,
            "${item} clicked",
            Toast.LENGTH_SHORT
        ).show()

        when (item) {
            "Date modified" -> {
                sortByDateAdded()
            }
            "Name" -> {
                sortByName()
            }
            "Artist" -> {
                sortByArtist()
            }
//            "Genre" -> {
//                sortByGenre()
//            }
            "Duration" -> {
                sortByDuration()
            }
        }
    }
}