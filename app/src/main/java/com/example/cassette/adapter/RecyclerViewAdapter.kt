package com.example.cassette.adapter

import android.R
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cassette.models.Song
import java.util.*
import kotlin.collections.ArrayList


//supert type for recyclerviews

open class RecyclerViewAdapter(
    var context: Activity,
    arrayList: ArrayList<Song>,
    val rv_item: Int
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var arrayList: ArrayList<Song> = arrayList

    final override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val rootView: View =
            LayoutInflater.from(context).inflate(rv_item, parent, false)
        return RecyclerViewViewHolder(rootView)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {}

    final override fun getItemCount(): Int {
        return arrayList.size
    }

    open inner class RecyclerViewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }
}