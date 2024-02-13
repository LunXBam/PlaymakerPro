package com.example.coaching_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.coaching_app.databinding.ActivityRosterBinding

class RosterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRosterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roster)
        binding = ActivityRosterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}