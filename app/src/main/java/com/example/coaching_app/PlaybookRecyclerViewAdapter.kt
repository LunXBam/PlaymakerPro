package com.example.coaching_app

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlaybookRecyclerViewAdapter(
    private val playList: List<PlaybookModel>,
    private val selectedTeam: Team?)
    :RecyclerView.Adapter<PlaybookRecyclerViewAdapter.PlayViewHolder>()  {

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
        val imageBytes = Base64.decode(currentItem.playImage, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        holder.playImage.setImageBitmap(decodedImage)
        holder.playTitle.text = currentItem.playTitle
    }

    override fun getItemCount(): Int {
        return playList.size
    }

    inner class PlayViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val playImage: ImageView = itemView.findViewById(R.id.playImageView)
        val playTitle: TextView = itemView.findViewById(R.id.titleTextView)
    }
}