package com.example.cassette.myInterface

import com.example.cassette.repositories.appdatabase.entities.PlaylistModel

interface PassDataForSelectPlaylists {
    fun passDataToInvokingFragment(playlists : ArrayList<PlaylistModel>)
}