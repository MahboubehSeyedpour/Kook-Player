package com.example.cassette.utlis

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide

object ImageUtils {

    fun loadImage(context: Context, imageView: ImageView, image: Bitmap){
        Glide.with(context).load(image).circleCrop().into(imageView)
    }
}