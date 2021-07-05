package com.example.cassette.models

import android.graphics.Bitmap
import android.net.Uri


class SongModel(
//    val id: Long,

//    val trackNumber: Int,
//    val year: Int,

//    val data: String,
//    val dateModified: Long,
//    val albumId: Long,
//    val albumName: String,
//    val artistId: Long,
//    val artistName: String,
//    val composer: String?,
//    val albumArtist: String?
) {
    var title: String = ""
    var duration: String = ""
    var data: String = ""
    var dateAdded : String = ""
    var artist : String = ""
    var id : String = ""
    var uri : Uri? = null
    var albumId : String = ""
    lateinit var image : Bitmap
}
