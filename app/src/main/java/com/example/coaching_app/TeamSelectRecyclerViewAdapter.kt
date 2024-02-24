package com.example.coaching_app

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TeamSelectRecyclerViewAdapter(
    private val teamList: List<Team>,
    private val listener: TeamRecyclerViewEvent)
    :RecyclerView.Adapter<TeamSelectRecyclerViewAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.team_select_recycler_view_row,
                parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder : MyViewHolder, position: Int) {
            val teams = teamList[position]
            //val imageBytes = Base64.decode(teams.logo, Base64.DEFAULT)
            //val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            //holder.ivLogo.setImageBitmap(decodedImage)
            holder.tvTeamName.text = teams.teamName
        }

        override fun getItemCount(): Int {
            return teamList.size
        }

        inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {


            //val ivLogo: ImageView = itemView.findViewById(R.id.teamLogoImageView)
            val tvTeamName: TextView = itemView.findViewById(R.id.teamNameTextView)

            init {
                itemView.setOnClickListener(this)
            }

            override fun onClick(p0: View?) {
                val position = bindingAdapterPosition
                if(position != RecyclerView.NO_POSITION){
                    listener.onItemClick(position)
                }
            }
        }

        interface TeamRecyclerViewEvent{
            fun onItemClick(position: Int)
        }
}