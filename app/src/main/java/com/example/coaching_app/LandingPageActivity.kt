package com.example.coaching_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coaching_app.databinding.ActivityLandingPageBinding

class LandingPageActivity : DrawerBaseActivity() {

    private lateinit var binding: ActivityLandingPageBinding
    private lateinit var recyclerView: RecyclerView

    @SuppressWarnings("deprecation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)
        binding = ActivityLandingPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val bundle: Bundle? = intent.extras
        val selectedTeam = intent.getParcelableExtra<Team>("selectedTeam")

        val title = binding.nameLandingTextView
        title.text = selectedTeam?.teamName

        recyclerView = findViewById(R.id.gameLandingRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val viewModel : GameHistViewModel by viewModels()
        viewModel.getGameHistory().observe(this) { gameHist ->
            recyclerView.adapter = GameHistoryRecyclerViewAdapter(this, gameHist)
        }

        binding.landingToRosterButton.setOnClickListener{
            val intent = Intent(this, RosterActivity::class.java)
            startActivity(intent)
        }

        binding.landingToGamesButton.setOnClickListener{
            val intent = Intent(this, GameHistoryActivity::class.java)
            startActivity(intent)
        }

        binding.landingToPlaysButton.setOnClickListener{
            val intent = Intent(this, PlaybookActivity::class.java)
            startActivity(intent)
        }

    }
}