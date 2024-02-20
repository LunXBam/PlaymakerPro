package com.example.coaching_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlayerRecyclerViewAdapter(private var playerList: ArrayList<PlayerModel>):RecyclerView.Adapter<PlayerRecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlayerRecyclerViewAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.roster_recycler_view_row,
            parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlayerRecyclerViewAdapter.MyViewHolder, position: Int) {
        val currentItem = playerList[position]
        holder.tvName.text = currentItem.name
        holder.tvNumber.text = currentItem.num
        holder.tvPosition.text = currentItem.pos
    }

    override fun getItemCount(): Int {
        return playerList.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.nameTextView)
        var tvNumber: TextView = itemView.findViewById(R.id.numberTextView)
        var tvPosition: TextView = itemView.findViewById(R.id.positionTextView)
    }

}