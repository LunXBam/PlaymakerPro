package com.example.coaching_app
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.coaching_app.databinding.ActivityCreateStatsBinding
import com.google.firebase.auth.FirebaseAuth

class createStatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateStatsBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_stats)
        binding = ActivityCreateStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.createStats.setOnClickListener {  }

        binding.backButton.setOnClickListener {
            val intent = Intent(this, PlaybookActivity::class.java)
            startActivity(intent)
        }


    }
}

