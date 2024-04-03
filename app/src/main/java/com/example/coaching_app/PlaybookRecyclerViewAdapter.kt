package com.example.coaching_app

import android.graphics.Bitmap
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
        val playString = currentItem.playImage
        val bitmap = base64ToBitmap(playString)
        if(bitmap != null)
        {
            holder.playImage.setImageBitmap(bitmap)
        }
        else
        {
            holder.playImage.setImageResource(R.drawable.playbook_image1)
        }
//        val imageBytes = Base64.decode(currentItem.playImage, Base64.DEFAULT)
//
//        if (imageBytes != null)
//        {
//            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
//            holder.playImage.setImageBitmap(decodedImage)
//        }
//        else
//        {
//            holder.playImage.setImageResource(R.drawable.playbook_image1)
//        }

        holder.playTitle.text = currentItem.playTitle
    }

    override fun getItemCount(): Int {
        return playList.size
    }

    inner class PlayViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val playImage: ImageView = itemView.findViewById(R.id.playImageView)
        val playTitle: TextView = itemView.findViewById(R.id.titleTextView)
    }


    fun base64ToBitmap(base64String: String?): Bitmap? {
        if (base64String.isNullOrEmpty()) {
            return null
        }
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            null
        }
    }



}