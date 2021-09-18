package com.example.cassette.utlis

import android.graphics.Point
import com.example.cassette.views.MainActivity

object ScreenSizeUtils {
    fun getScreenHeight(): Int {
        val display = MainActivity.activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.y
    }

    fun getScreenWidth(): Int {
        val display = MainActivity.activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x
    }

}