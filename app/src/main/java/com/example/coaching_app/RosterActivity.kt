package com.example.coaching_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coaching_app.databinding.ActivityRosterBinding

class RosterActivity : DrawerBaseActivity() {
    private lateinit var binding: ActivityRosterBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roster)
        binding = ActivityRosterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //supportActionBar?.title = "Team Roster"
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle: Bundle? = intent.extras
        val selectedTeam = intent.getParcelableExtra<Team>("selectedTeam")

        recyclerView = findViewById(R.id.playerListRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        binding.textView.text = selectedTeam?.teamName

        val viewModel : RosterViewModel by viewModels()

        viewModel.getRoster().observe(this) { roster ->
            val adapter = PlayerRecyclerViewAdapter(roster,selectedTeam)
            recyclerView.adapter = adapter
            adapter.setOnItemClickListener(object : PlayerRecyclerViewAdapter.onItemClickListener{
                override fun onItemClick(position: Int) {
                    val myIntent = Intent(this@RosterActivity, PlayerActivity::class.java)
                    myIntent.putExtra("selectedPlayer",roster[position])
                    startActivity(myIntent)
                }
            })
        }

        val editRosterButton = binding.editRosterButton

        editRosterButton.setOnClickListener{
            val intent = Intent(this, EditRosterActivity::class.java)
            intent.putExtra("selectedTeam",selectedTeam)
            startActivity(intent)
        }
    }
}