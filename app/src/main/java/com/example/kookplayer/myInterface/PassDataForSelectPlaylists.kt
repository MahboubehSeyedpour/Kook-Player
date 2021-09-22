package com.example.kookplayer.myInterface

import com.example.kookplayer.repositories.appdatabase.entities.PlaylistModel

interface PassDataForSelectPlaylists {
    fun passDataToInvokingFragment(playlists : ArrayList<PlaylistModel>)
}