package com.example.kookplayer.utlis

import androidx.appcompat.app.AppCompatDelegate

object ThemeUtils {
    fun forceActivityToUseLightMode()
    {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}