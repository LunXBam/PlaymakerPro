package com.example.coaching_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlayerRecyclerViewAdapter(private val playerList: ArrayList<PlayerModel>):RecyclerView.Adapter<PlayerRecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.roster_recycler_view_row,
            parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder : MyViewHolder, position: Int) {
        val currentItem = playerList[position]
        holder.tvName.text = currentItem.fName
        holder.tvNumber.text = currentItem.num
        holder.tvPosition.text = currentItem.pos
    }

    override fun getItemCount(): Int {
        return playerList.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.nameTextView)
        val tvNumber: TextView = itemView.findViewById(R.id.numberTextView)
        val tvPosition: TextView = itemView.findViewById(R.id.positionTextView)
    }

}