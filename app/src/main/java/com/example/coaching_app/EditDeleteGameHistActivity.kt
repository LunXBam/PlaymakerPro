package com.example.coaching_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.coaching_app.databinding.ActivityCreateGameHistoryBinding
import com.example.coaching_app.databinding.ActivityEditDeleteGameHistBinding

class EditDeleteGameHistActivity : AppCompatActivity() {

    private lateinit var binding:ActivityEditDeleteGameHistBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDeleteGameHistBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}