package com.example.cassette.utlis

import android.content.Context
import android.media.MediaPlayer
import androidx.core.content.contentValuesOf
import com.example.cassette.R
import kotlinx.android.synthetic.main.bottom_sheet.*

object MusicPlayer : MediaPlayer() {
    lateinit var musicplayer : MediaPlayer
    lateinit var context : Context

    fun init(context: Context){
        musicplayer = MediaPlayer.create(context, R.raw.nafas)
        this.context = context
    }


    fun playMusic(){
        if(!musicplayer.isPlaying){
            musicplayer.start()
        }
        else{
            musicplayer.pause()
        }
    }
}