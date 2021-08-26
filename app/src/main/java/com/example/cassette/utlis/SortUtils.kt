package com.example.cassette.utlis

import android.content.Context
import android.widget.Toast
import com.example.cassette.models.SongModel
import com.example.cassette.views.Fragments.LibraryFragment

object SortUtils {

    fun sortByDateAdded() {
        LibraryFragment.viewModel?.getDataSet().sortWith(compareByDescending { it.dateAdded })
        var sortedList = LibraryFragment.viewModel?.getDataSet()
        sortMusicList(sortedList)
    }

    fun sortByName() {
        LibraryFragment.viewModel?.getDataSet().sortWith(compareByDescending { it.title })
        var sortedList = LibraryFragment.viewModel?.getDataSet()
        sortMusicList(sortedList)
    }

    fun sortByArtist() {
        LibraryFragment.viewModel?.getDataSet().sortWith(compareByDescending { it.artist })
        var sortedList = LibraryFragment.viewModel?.getDataSet()
        sortMusicList(sortedList)
    }

//    fun sortByGenre() {
//        Library.arraylist?.sortWith(compareByDescending { it.genre })
//        var sortedList = Library.arraylist
//        sortMusicList(sortedList)
//    }

    fun sortByDuration() {
        LibraryFragment.viewModel?.getDataSet().sortWith(compareByDescending { it.duration })
        var sortedList = LibraryFragment.viewModel?.getDataSet()
        sortMusicList(sortedList)
    }

    fun sortMusicList(arrayList: ArrayList<SongModel>) {
        LibraryFragment.songsAdapter?.dataset = arrayList
        LibraryFragment.songsAdapter?.notifyDataSetChanged()
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