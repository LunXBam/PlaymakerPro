package com.example.coaching_app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coaching_app.databinding.GameHistoryBinding

class GameHistoryActivity : AppCompatActivity () {

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




       val viewModel : GameHistViewModel by viewModels()
        viewModel.getGameHistory().observe(this) { gameHist ->
            recyclerView.adapter = GameHistoryRecyclerViewAdapter(this, gameHist)
        }


    }
}