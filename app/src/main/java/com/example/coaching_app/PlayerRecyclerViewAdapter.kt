package com.example.coaching_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlayerRecyclerViewAdapter(
    private val playerList: List<PlayerModel>,
    private val selectedTeam: Team?)
    :RecyclerView.Adapter<PlayerRecyclerViewAdapter.MyViewHolder>() {

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
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.roster_recycler_view_row,
            parent, false)
        return MyViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder : MyViewHolder, position: Int) {
        val roster = playerList[position]
        if(roster.teamID == selectedTeam?.teamID) {
            val fullName = roster.firstName + " " + roster.lastName
            val number = "#" + roster.jerseyNumber
            holder.tvName.text = fullName
            holder.tvNumber.text = number
            holder.tvPosition.text = roster.playerPosition
        }
    }

    override fun getItemCount(): Int {
        return playerList.size
    }

    inner class MyViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView) {


        val tvName: TextView = itemView.findViewById(R.id.nameTextView)
        val tvNumber: TextView = itemView.findViewById(R.id.numberTextView)
        val tvPosition: TextView = itemView.findViewById(R.id.positionEditText)


        init {
            itemView.setOnClickListener {
                listener.onItemClick(bindingAdapterPosition)
            }
        }
        /*init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = bindingAdapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    interface PlayerRecyclerViewEvent{
        fun onItemClick(position: Int)
    }

         */
    }


}