package com.example.coaching_app

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coaching_app.databinding.ActivityPlaybookBinding

class PlaybookActivity : DrawerBaseActivity() {

    private lateinit var binding: ActivityPlaybookBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playbook)
        binding = ActivityPlaybookBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val bundle: Bundle? = intent.extras
        val selectedTeam = intent.getParcelableExtra<Team>("selectedTeam")

        recyclerView = findViewById(R.id.playRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val viewModel : PlaybookViewModel by viewModels()
        viewModel.getPlayBook().observe(this) { playbook ->
            recyclerView.adapter = PlaybookRecyclerViewAdapter(playbook, selectedTeam)
        }
    }
}