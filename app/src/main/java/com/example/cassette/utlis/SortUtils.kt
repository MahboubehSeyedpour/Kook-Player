package com.example.cassette.utlis

import android.content.Context
import android.widget.Toast
import com.example.cassette.models.SongModel
import com.example.cassette.views.Fragments.Library

object SortUtils {

    fun sortByDateAdded() {
        Library.viewModel?.getDataSet().sortWith(compareByDescending { it.dateAdded })
        var sortedList = Library.viewModel?.getDataSet()
        sortMusicList(sortedList)
    }

    fun sortByName() {
        Library.viewModel?.getDataSet().sortWith(compareByDescending { it.title })
        var sortedList = Library.viewModel?.getDataSet()
        sortMusicList(sortedList)
    }

    fun sortByArtist() {
        Library.viewModel?.getDataSet().sortWith(compareByDescending { it.artist })
        var sortedList = Library.viewModel?.getDataSet()
        sortMusicList(sortedList)
    }

//    fun sortByGenre() {
//        Library.arraylist?.sortWith(compareByDescending { it.genre })
//        var sortedList = Library.arraylist
//        sortMusicList(sortedList)
//    }

    fun sortByDuration() {
        Library.viewModel?.getDataSet().sortWith(compareByDescending { it.duration })
        var sortedList = Library.viewModel?.getDataSet()
        sortMusicList(sortedList)
    }

    fun sortMusicList(arrayList: ArrayList<SongModel>) {
        Library.songsAdapter?.dataset = arrayList
        Library.songsAdapter?.notifyDataSetChanged()
    }

    fun changeSortingOrder(context: Context, item: String) {
        Toast.makeText(
            context,
            "$item clicked",
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