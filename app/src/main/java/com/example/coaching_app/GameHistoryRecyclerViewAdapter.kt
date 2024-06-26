package com.example.coaching_app

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible


class GameHistoryRecyclerViewAdapter(
    private val listItems: List<GameHistory>,
    private val selectedTeam: Team?): RecyclerView.Adapter<GameHistoryRecyclerViewAdapter.GameHistViewHolder>() {

    private lateinit var mListener : onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    inner class GameHistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val opponent = itemView.findViewById<TextView>(R.id.oppTeamName)
        val score = itemView.findViewById<TextView>(R.id.score)
        val gameResult = itemView.findViewById<TextView>(R.id.result)
        val date = itemView.findViewById<TextView>(R.id.gameDay)

//        init {
//            itemView.setOnClickListener{
//                listener.onItemClick(bindingAdapterPosition)
//            }
//        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.game_history_recycler_view_, parent, false)
        return GameHistViewHolder(view)
    }


    override fun onBindViewHolder(holder: GameHistViewHolder, position: Int) {
        val gameHist = listItems[position]

        //if(gameHist.teamID == selectedTeam?.teamID){
        with(holder) {
            opponent.text = gameHist.opponent
            gameResult.text = gameHist.result
            date.text = gameHist.gameDate
            score.text = (gameHist.ourScore + " - " + gameHist.opponentScore)
        }
        //}
    }

    override fun getItemCount(): Int {
        return listItems.size
    }





}



