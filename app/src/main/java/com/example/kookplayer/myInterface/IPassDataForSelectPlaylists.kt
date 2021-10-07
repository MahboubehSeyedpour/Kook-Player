package com.example.kookplayer.myInterface

import com.example.kookplayer.db.entities.PlaylistModel

interface IPassDataForSelectPlaylists {
    fun passDataToInvokingFragment(playlists : ArrayList<PlaylistModel>)
}