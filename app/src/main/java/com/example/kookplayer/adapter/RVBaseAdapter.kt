package com.example.kookplayer.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


//super type for recyclerviews

abstract class RVBaseAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    abstract override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder

    abstract override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    )

    abstract fun getCurrentPosition(): Int

    abstract override fun getItemCount(): Int
    abstract inner class RecyclerViewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)
}