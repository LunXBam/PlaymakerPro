package com.example.coaching_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coaching_app.databinding.ActivityTeamSelectBinding

class TeamSelectActivity : AppCompatActivity() {
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

        val bundle: Bundle? = intent.extras
        val userID: String? = intent.getStringExtra("userID")

        recyclerView = findViewById(R.id.teamListRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)



        val viewModel : TeamSelectViewModel by viewModels()
        viewModel.getTeams().observe(this) { teams ->
            val adapter = TeamSelectRecyclerViewAdapter(teams)
            recyclerView.adapter = adapter
            adapter.setOnItemClickListener(object : TeamSelectRecyclerViewAdapter.onItemClickListener{
                override fun onItemClick(position: Int) {
                    //Toast.makeText(this@TeamSelectActivity,teams[position].teamName,Toast.LENGTH_LONG).show()
                    val myIntent = Intent(this@TeamSelectActivity, LandingPageActivity::class.java)

                    myIntent.putExtra("selectedTeam",teams[position])
                    val datashare = teams[position]
                    DataRepo.sharedData = datashare
                    startActivity(myIntent)
                }

            })
        }



        val newTeamButton = binding.newTeamButton

        newTeamButton.setOnClickListener{
            val intent = Intent(this, CreateTeamActivity::class.java)
            startActivity(intent)
        }
    }
}