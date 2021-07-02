package com.example.cassette.utlis.services

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide

object ImageUtils {

    fun loadImage(context: Context, imageView: ImageView , data: String, bitmap: Bitmap){
        Glide.with(context).load(bitmap).centerCrop().into(imageView)
    }
}