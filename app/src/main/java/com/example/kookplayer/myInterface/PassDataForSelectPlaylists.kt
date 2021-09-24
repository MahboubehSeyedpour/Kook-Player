package com.example.kookplayer.myInterface

import com.example.kookplayer.db.entities.PlaylistModel

interface PassDataForSelectPlaylists {
    fun passDataToInvokingFragment(playlists : ArrayList<PlaylistModel>)
}