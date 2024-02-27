package com.example.coaching_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coaching_app.databinding.ActivityRosterBinding

class RosterActivity : DrawerBaseActivity(), PlayerRecyclerViewAdapter.PlayerRecyclerViewEvent {
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


        val viewModel : RosterViewModel by viewModels()
        viewModel.getRoster().observe(this) { roster ->
            recyclerView.adapter = PlayerRecyclerViewAdapter(roster, this, selectedTeam)
        }

        val editRosterButton = binding.editRosterButton

        editRosterButton.setOnClickListener{
            val intent = Intent(this, EditRosterActivity::class.java)
            intent.putExtra("selectedTeam",selectedTeam)
            startActivity(intent)
        }
    }

    override fun onItemClick(position: Int) {

    }
}