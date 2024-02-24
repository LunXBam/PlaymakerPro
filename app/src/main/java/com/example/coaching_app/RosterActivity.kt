package com.example.coaching_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coaching_app.databinding.ActivityRosterBinding

class RosterActivity : AppCompatActivity(), PlayerRecyclerViewAdapter.PlayerRecyclerViewEvent {
    private lateinit var binding: ActivityRosterBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roster)
        binding = ActivityRosterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        recyclerView = findViewById(R.id.playerListRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)


        val viewModel : RosterViewModel by viewModels()
        viewModel.getRoster().observe(this) { roster ->
            recyclerView.adapter = PlayerRecyclerViewAdapter(roster, this)
        }
    }

    override fun onItemClick(position: Int) {

    }
}