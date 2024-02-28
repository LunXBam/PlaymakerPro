package com.example.coaching_app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coaching_app.databinding.GameHistoryBinding

class GameHistoryActivity : DrawerBaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: GameHistoryBinding


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_history)
        binding = GameHistoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        recyclerView = findViewById(R.id.gameHistRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val bundle: Bundle? = intent.extras
        val selectedTeam = intent.getParcelableExtra<Team>("selectedTeam")


       val viewModel : GameHistViewModel by viewModels()
        viewModel.getGameHistory().observe(this) { gameHist ->
            recyclerView.adapter = GameHistoryRecyclerViewAdapter(this, gameHist, selectedTeam)
        }

        binding.addGame.setOnClickListener{
            val intent = Intent(this, CreateGameHistoryActivity::class.java)
            intent.putExtra("selectedTeam",selectedTeam)
            startActivity(intent)
        }

        binding.goBack.setOnClickListener{
            val intent = Intent(this,LandingPageActivity::class.java)
            startActivity(intent)
        }



    }
}