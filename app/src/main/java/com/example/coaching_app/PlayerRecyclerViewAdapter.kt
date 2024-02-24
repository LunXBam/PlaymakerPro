package com.example.coaching_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlayerRecyclerViewAdapter(
    private val playerList: List<PlayerModel>,
    private val listener: PlayerRecyclerViewEvent)
    :RecyclerView.Adapter<PlayerRecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.roster_recycler_view_row,
            parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder : MyViewHolder, position: Int) {
        val roster = playerList[position]
        val fullName = roster.firstName + " " + roster.lastName
        holder.tvName.text = fullName
        holder.tvNumber.text = roster.jerseyNumber
        holder.tvPosition.text = roster.playerPosition
    }

    override fun getItemCount(): Int {
        return playerList.size
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {


        val tvName: TextView = itemView.findViewById(R.id.nameTextView)
        val tvNumber: TextView = itemView.findViewById(R.id.numberTextView)
        val tvPosition: TextView = itemView.findViewById(R.id.positionEditText)

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

    interface PlayerRecyclerViewEvent{
        fun onItemClick(position: Int)
    }


}