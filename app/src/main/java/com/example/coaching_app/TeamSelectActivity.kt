package com.example.coaching_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coaching_app.databinding.ActivityTeamSelectBinding

class TeamSelectActivity : AppCompatActivity(), TeamSelectRecyclerViewAdapter.TeamRecyclerViewEvent {
    private lateinit var binding: ActivityTeamSelectBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_select)
        binding = ActivityTeamSelectBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //supportActionBar?.title = "Team Roster"
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.teamListRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)


        val viewModel : TeamSelectViewModel by viewModels()
        viewModel.getTeams().observe(this) { teams ->
            recyclerView.adapter = TeamSelectRecyclerViewAdapter(teams, this)
        }

        val newTeamButton = binding.newTeamButton

        newTeamButton.setOnClickListener{
            val intent = Intent(this, CreateTeamActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onItemClick(position: Int) {

    }
}