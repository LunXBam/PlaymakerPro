package com.example.coaching_app

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class TeamSelectRecyclerViewAdapter(
    private val teamList: List<Team>)//,
    //private val onItemClicked: (Team) -> Unit)
    //private val listener: TeamRecyclerViewEvent)
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

        override fun onBindViewHolder(holder : MyViewHolder, position: Int) {
            val team = teamList[position]
            val imageBytes = Base64.decode(team.logo, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            holder.ivLogo.setImageBitmap(decodedImage)
            holder.tvTeamName.text = team.teamName
            //holder.itemView.setOnClickListener{onItemClicked(team)}
        }

        override fun getItemCount(): Int {
            return teamList.size
        }

        inner class MyViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView) {


            val ivLogo: ImageView = itemView.findViewById(R.id.teamLogoImageView)
            val tvTeamName: TextView = itemView.findViewById(R.id.teamNameTextView)

            init {
                itemView.setOnClickListener {
                    listener.onItemClick(bindingAdapterPosition)
                }
            }
            /*
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

 */
        }
}