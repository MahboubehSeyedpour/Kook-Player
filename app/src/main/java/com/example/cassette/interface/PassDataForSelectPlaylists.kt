package com.example.cassette.`interface`

import com.example.cassette.models.PlaylistModel

interface PassDataForSelectPlaylists {
    fun passDataToInvokingFragment(playlists : ArrayList<PlaylistModel>)
}