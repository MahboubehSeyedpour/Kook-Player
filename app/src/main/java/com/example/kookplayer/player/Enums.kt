package com.example.kookplayer.player

class Enums
{
    enum class PlayingOrder (order: String) {
        SHUFFLE ( "shuffle"),
        REPEAT_ALL ("repeatAll"),
        REPEAT_ONE ("repeat_one")
    }

    enum class PanelState (state: String)
    {
        EXPANDED ("expanded"),
        COLLAPSED ("collapsed")
    }

}