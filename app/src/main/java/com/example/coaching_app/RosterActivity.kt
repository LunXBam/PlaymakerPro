package com.example.coaching_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coaching_app.databinding.ActivityRosterBinding

class RosterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRosterBinding

    val players = arrayListOf<PlayerModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roster)
        binding = ActivityRosterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val recyclerView : RecyclerView = findViewById(R.id.playerListRecyclerView)

        loadData()

        val adapter : Player_RecyclerViewAdapter = Player_RecyclerViewAdapter(players)
        recyclerView.layoutManager = LinearLayoutManager(this)


    }

    private fun loadData(){
        val names = arrayOf(resources.getStringArray(R.array.player_names))
        val numbers = arrayOf(resources.getStringArray(R.array.player_numbers))
        val positions = arrayOf(resources.getStringArray(R.array.player_positions))

        for(i in 0..names.size){
            players.add(PlayerModel(names[i].toString(),numbers[i].toString(),positions[i].toString()))
        }
    }
}