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

class TeamSelectRecyclerViewAdapter(
    private val teamList: List<Team>)//,
    :RecyclerView.Adapter<TeamSelectRecyclerViewAdapter.MyViewHolder>() {

    private lateinit var mListener : onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }


        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.team_select_recycler_view_row,
                parent, false)
            return MyViewHolder(itemView,mListener)
        }

//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): MyViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_layout,
//            parent, false)
//        return MyViewHolder(itemView,mListener)
//    }

        override fun onBindViewHolder(holder : MyViewHolder, position: Int) {
            val team = teamList[position]
            val logoString = team.logo
            val bitmap = base64ToBitmap(logoString)
            if (bitmap != null)
            {
                holder.ivLogo.setImageBitmap(bitmap)
            }
            else
            {
                holder.ivLogo.setImageResource(R.drawable.default_team_logo)
            }
            holder.tvTeamName.text = team.teamName
            holder.tvCityName.text = team.city
            holder.tvSportName.text = team.sport
        }

        override fun getItemCount(): Int {
            return teamList.size
        }

        inner class MyViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView) {


            val ivLogo: ImageView = itemView.findViewById(R.id.teamLogoImageView)
            val tvTeamName: TextView = itemView.findViewById(R.id.teamNameTextView)
            val tvCityName: TextView = itemView.findViewById(R.id.cityNameTextView)
            val tvSportName: TextView = itemView.findViewById(R.id.sportNameTextView)

            init {
                itemView.setOnClickListener {
                    listener.onItemClick(bindingAdapterPosition)
                }
            }
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