package com.example.cassette.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cassette.R
import com.example.cassette.models.Song_Model
import kotlinx.android.synthetic.main.song_rv_item.view.*
import kotlin.collections.ArrayList


//supert type for recyclerviews

abstract class RV_Base_Adapter() :
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