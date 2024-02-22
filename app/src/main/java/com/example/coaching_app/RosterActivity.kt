package com.example.coaching_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coaching_app.databinding.ActivityRosterBinding

class RosterActivity : AppCompatActivity(){
    private lateinit var binding: ActivityRosterBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var playerList : ArrayList<PlayerModel>
    private lateinit var nameList:Array<String>
    private lateinit var numList:Array<String>
    private lateinit var posList:Array<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roster)
        binding = ActivityRosterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        recyclerView = findViewById(R.id.playerListRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        playerList = arrayListOf<PlayerModel>()

        loadData()
    }

    private fun loadData(){
        nameList = resources.getStringArray(R.array.player_names)
        numList = resources.getStringArray(R.array.player_numbers)
        posList = resources.getStringArray(R.array.player_positions)

        for(i in nameList.indices){
            playerList.add(PlayerModel(nameList[i].toString(),"#" + numList[i].toString(),posList[i].toString()))
        }
        recyclerView.adapter = PlayerRecyclerViewAdapter(playerList)
    }
}