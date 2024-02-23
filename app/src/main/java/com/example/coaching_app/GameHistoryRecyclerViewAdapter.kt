package com.example.coaching_app

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class GameHistoryRecyclerViewAdapter(
    val context: Context,
    val listItems: List<GameHistory>): RecyclerView.Adapter<GameHistoryRecyclerViewAdapter.GameHistViewHolder>() {

    inner class GameHistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val opponent = itemView.findViewById<TextView>(R.id.oppTeamName)
        val score = itemView.findViewById<TextView>(R.id.score)
        val gameResult = itemView.findViewById<TextView>(R.id.result)
        val date = itemView.findViewById<TextView>(R.id.gameDay)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.game_history_recycler_view_, parent, false)
        return GameHistViewHolder(view)
    }


    override fun onBindViewHolder(holder: GameHistViewHolder, position: Int) {
        val gameHist = listItems[position]


        with(holder){
            opponent.text = gameHist.opponent
            gameResult.text = gameHist.result
            date.text = gameHist.gameDate
            score.text = (gameHist.ourScore + " - " + gameHist.opponentScore )



        }



    }

    override fun getItemCount(): Int {
        return listItems.size
    }
}



