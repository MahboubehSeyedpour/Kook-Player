package com.example.cassette.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


//supert type for recyclerviews

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

    abstract override fun getItemCount(): Int
    abstract inner class RecyclerViewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)
}