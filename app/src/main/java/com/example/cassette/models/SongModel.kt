package com.example.cassette.models

import android.graphics.Bitmap
import android.net.Uri


class SongModel(
    var title: String = "",
    val duration: Long = -1,
    val data: String = "",
    val dateAdded : String = "",
    val artist : String = "",
    val id : String = "",
    val uri : Uri? = null,
    val albumId : Long = -1, //TODO(change to long)
    val image : Bitmap? = null,
    val trackNumber: String = "",
    val year: Int = -1,
    val dateModified: Long = -1,
    val artistId: Long = -1,
    val artistName: String = "",
    val composer: String? = "",
    val albumArtist: String?= ""
) {
}
