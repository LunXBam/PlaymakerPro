package com.example.coaching_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlaybookRecyclerViewAdapter(private val playList: ArrayList<PlaybookModel>):RecyclerView.Adapter<PlaybookRecyclerViewAdapter.PlayViewHolder>()  {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlayViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.playbook_recycler_view_row,
            parent, false)
        return PlayViewHolder(itemView)
    }

    override fun onBindViewHolder(holder : PlayViewHolder, position: Int) {
        val currentItem = playList[position]
        holder.playImage.setImageResource(currentItem.playImage)
        holder.playTitle.text = currentItem.playTitle
    }

    override fun getItemCount(): Int {
        return playList.size
    }

    class PlayViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val playImage: ImageView = itemView.findViewById(R.id.playImageView)
        val playTitle: TextView = itemView.findViewById(R.id.titleTextView)
    }
}